package frc.robot.AutonomousActions;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import frc.robot.AutonomousAction;
import frc.robot.RobotMotors;

/**
 * testestsetets
 */
public class AutonMoveForward extends AutonomousAction {
    private double speed;
    private boolean isFirstTimeRunning;
    private RobotMotors motors;
    private double targetDistance;

    public AutonMoveForward(double distance, double speed) {
        this.targetDistance = distance;
        this.speed = speed;
        this.isFirstTimeRunning = true;
    }

    @Override
    public void passMotors(RobotMotors motors) {
        this.motors = motors;
    }

    @Override
    public boolean executeAndIsDone() {
        if (isFirstTimeRunning) {
            System.out.println("Attempting to move forward");
            if (targetDistance < 0) {
                targetDistance *= -1;
                speed *= -1;
            }
            ChassisSpeeds chassisSpeeds = new ChassisSpeeds(speed, 0, 0);

            MecanumDriveWheelSpeeds wheelSpeeds = kinematics.toWheelSpeeds(chassisSpeeds);

            spinMotors(wheelSpeeds, motors);
            // Get the individual wheel speeds

            motors.getFrontLeftMotor().setEncoderPosition(0);
            isFirstTimeRunning = false;
            return false;
        }

        if (Math.abs(getFeetTraveled()) >= Math.abs(targetDistance)) {
            motors.stopAllMotors();
            return true;
        }
        return false;

    }

    private double getFeetTraveled() {
        return motors.getBackLeftMotor().getEncoderPosition() * (8.0 * Math.PI / 12.0);
    }

}