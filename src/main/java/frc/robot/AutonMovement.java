package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
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
    Translation2d frontLeftLocation = new Translation2d(10.75, 10.5);
    Translation2d frontRightLocation = new Translation2d(10.75, -10.5);
    Translation2d backLeftLocation = new Translation2d(-10.75, 10.5);
    Translation2d backRightLocation = new Translation2d(-10.75, -10.5);

    MecanumDriveKinematics kinematics = new MecanumDriveKinematics(frontLeftLocation,
            frontRightLocation, backLeftLocation, backRightLocation);



    public AutonMovement(MecanumDrive drive) {
        this.drive = drive;
    }

    public void AutoTurn(double angle, double speed) {
        drive.driveCartesian(0, 0, speed);

        // ChassisSpeeds arguments are:
        // chassis meters per second on the x-axis, positive is forward
        // chassis meters per second on the y-axis, positive is left
        // angular velocity of the robot in radians per second, positive is counterclockwise
        ChassisSpeeds chassisSpeeds = new ChassisSpeeds(1.0, 3.0, 1.5);

        // Convert to wheel speeds
        MecanumDriveWheelSpeeds wheelSpeeds = kinematics.toWheelSpeeds(chassisSpeeds);

        // Get the individual wheel speeds
        double frontLeft = wheelSpeeds.frontLeftMetersPerSecond;
        double frontRight = wheelSpeeds.frontRightMetersPerSecond;
        double backLeft = wheelSpeeds.rearLeftMetersPerSecond;
        double backRight = wheelSpeeds.rearRightMetersPerSecond;
    }

}
