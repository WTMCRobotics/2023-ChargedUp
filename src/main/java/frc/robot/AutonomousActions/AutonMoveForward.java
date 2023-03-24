package frc.robot.AutonomousActions;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import frc.robot.AutonomousAction;
import frc.robot.RobotMotors;

public class AutonMoveForward extends AutonomousAction {
    private double speed;
    private boolean isFirstTimeRunning;
    private RobotMotors motors;
    private double targetDistance;

    /**
     * Moves a specified distance at a specified speed.
     * 
     * @param distance The distance to move, measure in feet. Positive is forward, negative is
     *        backwards.
     * @param speed The speed to move at, in feet per second.
     */
    public AutonMoveForward(double distance, double speed, RobotMotors motors) {
        this.targetDistance = distance;
        this.speed = speed;
        this.isFirstTimeRunning = true;
        this.motors = motors;
    }

    @Override
    public boolean executeAndIsDone() {
        if (isFirstTimeRunning) {
            System.out.println("Attempting to move forward Encoder position is "
                    + motors.getFrontLeftMotor().getEncoderPosition());
            if (targetDistance < 0) {
                targetDistance *= -1;
                speed *= -1;
            }
            // Convert from f/s to m/s
            speed /= 3.281;
            ChassisSpeeds chassisSpeeds = new ChassisSpeeds(speed, 0, 0);

            MecanumDriveWheelSpeeds wheelSpeeds = kinematics.toWheelSpeeds(chassisSpeeds);

            spinMotors(wheelSpeeds, motors, false);
            // Get the individual wheel speeds
            System.out.println("Resettingfront left  motor encoder values to 0!");
            motors.getFrontLeftMotor().setEncoderPosition(0);
            motors.getFrontRightMotor().setEncoderPosition(0);
            motors.getBackLeftMotor().setEncoderPosition(0);
            motors.getBackRightMotor().setEncoderPosition(0);
            isFirstTimeRunning = false;
            return false;
        }

        if (Math.abs(getFeetTraveled()) >= Math.abs(targetDistance)) {
            System.out.println("The robot has moved enough, as " + getFeetTraveled()
                    + " is greater than " + targetDistance);
            motors.stopDriveMotors();
            return true;
        }
        return false;

    }

    private double getFeetTraveled() {
        return ((Math.abs(motors.getFrontLeftMotor().getEncoderPosition()) * (8.0 * Math.PI / 12.0)
                + Math.abs(motors.getFrontRightMotor().getEncoderPosition())
                        * (8.0 * Math.PI / 12.0)
                + Math.abs(motors.getBackLeftMotor().getEncoderPosition()) * (8.0 * Math.PI / 12.0)
                + Math.abs(motors.getBackRightMotor().getEncoderPosition())
                        * (8.0 * Math.PI / 12.0))
                / 4) + 1;
    }

}
