package frc.robot.AutonomousActions;

import com.kauailabs.navx.frc.AHRS;
import frc.robot.AutonomousAction;
import frc.robot.Constants;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import edu.wpi.first.wpilibj.SPI;
import frc.robot.RobotMotors;

public class AutonBalance extends AutonomousAction {
    MovementDirection movementDirection;
    private boolean isFirstTimeRunning;
    private RobotMotors motors;
    private AHRS gyro = new AHRS(SPI.Port.kMXP);
    private boolean wasUnbalanced = false;
    private double currentDebounceTime = 0;
    private double currentBalanceDebouceTime = 0;

    /**
     * Used to automatically balance the robot on the charging station, within a
     * certain degree.
     * 
     * @param position Whether to start the robot moving forwards, or backwards
     */
    public AutonBalance(MovementDirection movementDirection) {
        this.movementDirection = movementDirection;
        this.wasUnbalanced = false;
    }

    @Override
    public boolean executeAndIsDone() {
        if (isFirstTimeRunning) {

            gyro.calibrate();
            if (movementDirection == MovementDirection.FORWARDS) {
                spinMotorsAtSpeed(Constants.ROBOT_SPEED_WHILE_BALANCING_BEFORE_CHARGE_STATION);
            } else {
                spinMotorsAtSpeed(-Constants.ROBOT_SPEED_WHILE_BALANCING_BEFORE_CHARGE_STATION);
            }

            isFirstTimeRunning = false;
            return false;
        }

        if (!this.isProbablyBalanced()) {
            currentDebounceTime += .02;
            if (currentDebounceTime >= Constants.BEING_UNBALANCED_DEBOUNCE_TIME) {
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

        if (wasUnbalanced && isProbablyBalanced()) {
            currentBalanceDebouceTime += .02;
            if (currentBalanceDebouceTime > Constants.BALANCING_DEBOUNCE_TIME) {
                motors.stopDriveMotors();
                gyro.close();
                return true;
            }
        } else {
            currentBalanceDebouceTime = 0;
        }

        return false;
    }

    @Override
    public void passMotors(RobotMotors motors) {
        this.motors = motors;
    }

    private void spinMotorsAtSpeed(double speed) {
        MecanumDriveWheelSpeeds wheelSpeeds = new MecanumDriveWheelSpeeds(speed, speed, speed, speed);
        spinMotors(wheelSpeeds, motors, true);
    }

    /**
     * Returns whether the robot is currently balanced or not. Is it accurate?
     * Maybe.
     * 
     * @return Whether or not the robot is balanced or not (probably)
     */
    private boolean isProbablyBalanced() {
        // If the gyro reading is greater than negative half the buffer, and less than
        // positive half
        // the buffer, then it's maybe balanced!
        double halfOfMargianOfError = Constants.BALANCING_MARGAIN_OF_ERROR / 2.0;

        return -halfOfMargianOfError <= gyro.getPitch() && gyro.getPitch() <= halfOfMargianOfError;
    }

    public enum MovementDirection {
        FORWARDS, BACKWARDS
    }

}
