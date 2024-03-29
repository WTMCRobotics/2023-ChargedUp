package frc.robot.AutonomousActions;

import org.opencv.core.Mat;
import frc.robot.AutonomousAction;
import frc.robot.RobotMotors;

public class AutonMoveInches extends AutonomousAction {


    MoveInchesDirection direction;
    double inches;
    RobotMotors motors;
    boolean isFirstTimeRunning;
    double isDoneDebounceTime;
    final double rootTwo = Math.sqrt(2);

    /**
     * move a distance in s straight line
     * 
     * @param direction Specifies which direction to move to robot in. possible options are FORWARD,
     *        BACKWARD, LEFT, and RIGHT
     * @param inches the distance in inches to move forward (use negative number to go backward)
     * @param motors the motors in which to move
     * @return true when done
     */

    public AutonMoveInches(MoveInchesDirection direction, double inches, RobotMotors motors) {
        this.direction = direction;
        this.inches = inches;
        this.motors = motors;
        this.isFirstTimeRunning = true;
        this.isDoneDebounceTime = 0;
    }


    public boolean executeAndIsDone() {
        if (isFirstTimeRunning) {
            resetDriveTrainEncoders();
            isFirstTimeRunning = false;
        }
        if (direction == MoveInchesDirection.FORWARD) {
            motors.getFrontLeftMotor().setDistance(inches);
            motors.getBackLeftMotor().setDistance(inches);
            motors.getFrontRightMotor().setDistance(inches);
            motors.getBackRightMotor().setDistance(inches);
        } else if (direction == MoveInchesDirection.BACKWARD) {
            motors.getFrontLeftMotor().setDistance(-inches);
            motors.getBackLeftMotor().setDistance(-inches);
            motors.getFrontRightMotor().setDistance(-inches);
            motors.getBackRightMotor().setDistance(-inches);
        } else if (direction == MoveInchesDirection.LEFT) {
            final double strafingInches = inches * rootTwo;
            motors.getFrontLeftMotor().setDistance(-strafingInches);
            motors.getBackLeftMotor().setDistance(strafingInches);
            motors.getFrontRightMotor().setDistance(strafingInches);
            motors.getBackRightMotor().setDistance(-strafingInches);
        } else if (direction == MoveInchesDirection.RIGHT) {
            final double strafingInches = inches * rootTwo;
            motors.getFrontLeftMotor().setDistance(strafingInches);
            motors.getBackLeftMotor().setDistance(-strafingInches);
            motors.getFrontRightMotor().setDistance(-strafingInches);
            motors.getBackRightMotor().setDistance(strafingInches);
        }

        if (getMaxTrajectoryVelocity() < 0.05) {
            isDoneDebounceTime += 0.02;
        } else {
            isDoneDebounceTime = 0;
        }

        if (isDoneDebounceTime > 0.6) {
            System.out.println("We moved the correct amount of inches!");
            return true;
        }

        /*
         * if (Math.abs(motors.getFrontLeftMotor().getEncoderPosition() - (inches /
         * Constants.WHEEL_CIRCUMFERENCE_INCHES)) < Constants.MARGIN_OF_ERROR_INCHES &&
         * Math.abs(motors.getFrontLeftMotor() .getActiveTrajectoryVelocity()) < (1.0 /
         * Constants.WHEEL_CIRCUMFERENCE_INCHES) * 10 &&
         * Math.abs(motors.getFrontRightMotor().getEncoderPosition() - (inches /
         * Constants.WHEEL_CIRCUMFERENCE_INCHES)) < Constants.MARGIN_OF_ERROR_INCHES &&
         * Math.abs(motors.getFrontRightMotor() .getActiveTrajectoryVelocity()) < (1.0 /
         * Constants.WHEEL_CIRCUMFERENCE_INCHES) * 10 &&
         * Math.abs(motors.getBackLeftMotor().getEncoderPosition() - (inches /
         * Constants.WHEEL_CIRCUMFERENCE_INCHES)) < Constants.MARGIN_OF_ERROR_INCHES &&
         * Math.abs(motors.getBackLeftMotor() .getActiveTrajectoryVelocity()) < (1.0 /
         * Constants.WHEEL_CIRCUMFERENCE_INCHES) * 10 &&
         * Math.abs(motors.getBackRightMotor().getEncoderPosition() - (inches /
         * Constants.WHEEL_CIRCUMFERENCE_INCHES)) < Constants.MARGIN_OF_ERROR_INCHES &&
         * Math.abs(motors.getBackRightMotor().getActiveTrajectoryVelocity()) < (1.0 /
         * Constants.WHEEL_CIRCUMFERENCE_INCHES) * 10) { return true;
         * 
         * }
         */

        return false;
    }

    public static enum MoveInchesDirection {
        FORWARD, BACKWARD, LEFT, RIGHT
    }

    /**
     * @return The active trajectory velocity of all of the averaged motors
     */
    private double getMaxTrajectoryVelocity() {
        double higherLeft =
                Math.max(Math.abs(motors.getFrontLeftMotor().getActiveTrajectoryVelocity()),
                        Math.abs(motors.getBackLeftMotor().getActiveTrajectoryVelocity()));
        double higherRight =
                Math.max(Math.abs(motors.getFrontRightMotor().getActiveTrajectoryVelocity()),
                        Math.abs(motors.getBackRightMotor().getActiveTrajectoryVelocity()));
        return Math.max(higherLeft, higherRight);
        // return (Math.abs(motors.getFrontLeftMotor().getActiveTrajectoryVelocity())
        // + Math.abs(motors.getFrontRightMotor().getActiveTrajectoryVelocity())
        // + Math.abs(motors.getBackLeftMotor().getActiveTrajectoryVelocity())
        // + Math.abs(motors.getBackRightMotor().getActiveTrajectoryVelocity())) / 4;
    }

    private void resetDriveTrainEncoders() {
        motors.getFrontLeftMotor().setEncoderPosition(0.0);
        motors.getFrontRightMotor().setEncoderPosition(0.0);
        motors.getBackRightMotor().setEncoderPosition(0.0);
        motors.getBackLeftMotor().setEncoderPosition(0.0);
    }

}
