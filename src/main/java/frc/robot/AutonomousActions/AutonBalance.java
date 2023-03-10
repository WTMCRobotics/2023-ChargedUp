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

    /**
     * Used to automatically balance the robot on the charging station, within a certain degree.
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

            MecanumDriveWheelSpeeds wheelSpeeds;
            if (movementDirection == MovementDirection.FORWARDS) {
                wheelSpeeds = new MecanumDriveWheelSpeeds(Constants.ROBOT_SPEED_WHILE_BALANCING,
                        Constants.ROBOT_SPEED_WHILE_BALANCING,
                        Constants.ROBOT_SPEED_WHILE_BALANCING,
                        Constants.ROBOT_SPEED_WHILE_BALANCING);
            } else {
                wheelSpeeds = new MecanumDriveWheelSpeeds(-Constants.ROBOT_SPEED_WHILE_BALANCING,
                        -Constants.ROBOT_SPEED_WHILE_BALANCING,
                        -Constants.ROBOT_SPEED_WHILE_BALANCING,
                        -Constants.ROBOT_SPEED_WHILE_BALANCING);
            }

            spinMotors(wheelSpeeds, motors);

            isFirstTimeRunning = false;
            return false;
        }

        if (!this.isProbablyBalanced()) {
            wasUnbalanced = true;
            return false;
        }

        if (wasUnbalanced && isProbablyBalanced()) {
            motors.stopAllMotors();
            gyro.close();
            return true;
        }

        return false;
    }

    @Override
    public void passMotors(RobotMotors motors) {
        this.motors = motors;
    }

    /**
     * Returns whether the robot is currently balanced or not. Is it accurate? Maybe.
     * 
     * @return Whether or not the robot is balanced or not (probably)
     */
    private boolean isProbablyBalanced() {
        // If the gyro reading is greater than negative half the buffer, and less than positive half
        // the buffer, then it's maybe balanced!
        double halfOfMargianOfError = Constants.BALANCING_MARGAIN_OF_ERROR / 2.0;

        return -halfOfMargianOfError <= gyro.getPitch() && gyro.getPitch() <= halfOfMargianOfError;
    }

    public enum MovementDirection {
        FORWARDS, BACKWARDS
    }

}

