package frc.robot.AutonomousActions;

import com.kauailabs.navx.frc.AHRS;
import frc.robot.AutonomousAction;
import frc.robot.Constants;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import frc.robot.RobotMotors;

public class AutonBalance extends AutonomousAction {
    MovementDirection movementDirection;
    private boolean isFirstTimeRunning;
    private RobotMotors motors;
    private AHRS gyro;
    private boolean wasUnbalanced = false;
    private double currentDebounceTime = 0;
    private double currentBalanceDebouceTime = 0;

    /**
     * Used to automatically balance the robot on the charging station, within a certain degree.
     * 
     * @param position Whether to start the robot moving forwards, or backwards
     */
    public AutonBalance(MovementDirection movementDirection, AHRS gyro, RobotMotors motors) {
        System.out.println("Balnce thingy");
        this.gyro = gyro;
        this.movementDirection = movementDirection;
        this.isFirstTimeRunning = true;
        this.wasUnbalanced = false;
        this.motors = motors;
    }

    // Bottom slab = 11 & 12 degrees
    // Bigger panel = 15 degrees

    // FRONT SIDE OF ROBOT UPWARDS = Roll goes positive

    @Override
    public boolean executeAndIsDone() {
        if (isFirstTimeRunning) {

            if (movementDirection == MovementDirection.FORWARDS) {
                spinMotorsAtSpeed(Constants.ROBOT_SPEED_WHILE_BALANCING_BEFORE_CHARGE_STATION);
            } else {
                spinMotorsAtSpeed(-Constants.ROBOT_SPEED_WHILE_BALANCING_BEFORE_CHARGE_STATION);
            }

            isFirstTimeRunning = false;
            return false;
        }

        if (!this.isProbablyBalanced() && !wasUnbalanced) {
            currentDebounceTime += .02;
            if (currentDebounceTime >= Constants.BEING_UNBALANCED_DEBOUNCE_TIME) {
                System.out.println("It is not balanced rn!");
                if (movementDirection == MovementDirection.FORWARDS) {
                    spinMotorsAtSpeed(Constants.ROBOT_SPEED_WHILE_BALANCING_ON_CHARGE_STATION);
                } else {
                    spinMotorsAtSpeed(-Constants.ROBOT_SPEED_WHILE_BALANCING_ON_CHARGE_STATION);
                }
                wasUnbalanced = true;
                return false;
            }

        } else {
            currentDebounceTime = 0;
        }

        if (wasUnbalanced) {
            if (gyro.getRoll() > Constants.BALANCING_MARGAIN_OF_ERROR_ON_STATION) {
                spinMotorsAtSpeed(Constants.ROBOT_SPEED_WHILE_BALANCING_ON_CHARGE_STATION);
            } else if (gyro.getRoll() < -Constants.BALANCING_MARGAIN_OF_ERROR_ON_STATION) {
                spinMotorsAtSpeed(-Constants.ROBOT_SPEED_WHILE_BALANCING_ON_CHARGE_STATION);
            } else {
                spinMotorsAtSpeed(0.0);
            }

            if (isProbablyBalanced()) {
                currentBalanceDebouceTime += .02;
                if (currentBalanceDebouceTime > Constants.BALANCING_DEBOUNCE_TIME) {
                    System.out.println("Yea it all balanced");
                    motors.stopDriveMotors();
                    return true;
                }
            } else {
                currentBalanceDebouceTime = 0;
            }
        }

        return false;
    }

    private void spinMotorsAtSpeed(double speed) {
        MecanumDriveWheelSpeeds wheelSpeeds =
                new MecanumDriveWheelSpeeds(speed, speed, speed, speed);
        spinMotors(wheelSpeeds, motors, false);
    }

    /**
     * Returns whether the robot is currently balanced or not. Is it accurate? Maybe.
     * 
     * @return Whether or not the robot is balanced or not (probably)
     */
    private boolean isProbablyBalanced() {
        // If the gyro reading is greater than negative half the buffer, and less than
        // positive half
        // the buffer, then it's maybe balanced!
        // System.out.println(
        // "The robot is balanced, because " + (-halfOfMargianOfError) + " is less than
        // or equal to " + gyroRoll);

        return gyro.getRoll() >= -Constants.BALANCING_MARGIN_OF_ERROR
                && gyro.getRoll() <= Constants.BALANCING_MARGIN_OF_ERROR;
    }

    public enum MovementDirection {
        FORWARDS, BACKWARDS
    }

}
