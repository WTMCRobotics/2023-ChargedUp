package frc.robot;

import edu.wpi.first.math.kinematics.MecanumDriveKinematics;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;

public abstract class AutonomousAction {

    public MecanumDriveKinematics kinematics = new MecanumDriveKinematics(
            Constants.FRONT_LEFT_WHEEL_LOCATION, Constants.FRONT_RIGHT_WHEEL_LOCATION,
            Constants.BACK_LEFT_WHEEL_LOCATION, Constants.BACK_RIGHT_WHEEL_LOCATION);

    public abstract boolean executeAndIsDone();

    public abstract void passMotors(RobotMotors motors);

    public void spinMotors(MecanumDriveWheelSpeeds wheelSpeeds, RobotMotors motors) {

        double frontLefSpeed = wheelSpeeds.frontLeftMetersPerSecond;
        double frontRightSpeed = wheelSpeeds.frontRightMetersPerSecond;
        double backLeftSpeed = wheelSpeeds.rearLeftMetersPerSecond;
        double backRightSpeed = wheelSpeeds.rearRightMetersPerSecond;

        motors.getFrontLeftMotor().set(frontLefSpeed);
        motors.getFrontRightMotor().set(frontRightSpeed);
        motors.getBackLeftMotor().set(backLeftSpeed);
        motors.getBackRightMotor().set(backRightSpeed);
    }

    public void spinMotors(MecanumDriveWheelSpeeds wheelSpeeds, RobotMotors motors, boolean inverseHalf) {

        double frontLefSpeed = wheelSpeeds.frontLeftMetersPerSecond;
        double frontRightSpeed = wheelSpeeds.frontRightMetersPerSecond;
        double backLeftSpeed = wheelSpeeds.rearLeftMetersPerSecond;
        double backRightSpeed = wheelSpeeds.rearRightMetersPerSecond;

        motors.getFrontLeftMotor().set(frontLefSpeed);
        motors.getFrontRightMotor().set(-frontRightSpeed);
        motors.getBackLeftMotor().set(backLeftSpeed);
        motors.getBackRightMotor().set(-backRightSpeed);
    }
}
