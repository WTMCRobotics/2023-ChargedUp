package frc.robot;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.MecanumDriveKinematics;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import edu.wpi.first.wpilibj.drive.MecanumDrive;



public class AutonMovement {
    MecanumDrive drive;
    // wheels are 10.75 inches away from the center along the x-axis and 10.5 inches away from the
    // center along the y-axis
    // x-axis is forward and backward; y-axis is left and right
    // positive x is forward, positive y is left


    MecanumDriveKinematics kinematics = new MecanumDriveKinematics(
            Constants.FRONT_LEFT_WHEEL_LOCATION, Constants.FRONT_RIGHT_WHEEL_LOCATION,
            Constants.BACK_LEFT_WHEEL_LOCATION, Constants.BACK_RIGHT_WHEEL_LOCATION);



    public AutonMovement(MecanumDrive drive) {
        this.drive = drive;
    }

    public void AutoTurn(double angle, double speed) {
        // drive.driveCartesian(0, 0, speed); probably don't need this

        // ChassisSpeeds arguments are:
        // chassis meters per second on the x-axis, positive is forward
        // chassis meters per second on the y-axis, positive is left
        // angular velocity of the robot in radians per second, positive is counterclockwise

        ChassisSpeeds chassisSpeeds = new ChassisSpeeds(1.0, 3.0, 1.5);

        // Convert to wheel speeds
        MecanumDriveWheelSpeeds wheelSpeeds = kinematics.toWheelSpeeds(chassisSpeeds);

        // Get the individual wheel speeds
        double frontLefSpeed = wheelSpeeds.frontLeftMetersPerSecond;
        double frontRightSpeed = wheelSpeeds.frontRightMetersPerSecond;
        double backLeftSpeed = wheelSpeeds.rearLeftMetersPerSecond;
        double backRightSpeed = wheelSpeeds.rearRightMetersPerSecond;



    }

}
