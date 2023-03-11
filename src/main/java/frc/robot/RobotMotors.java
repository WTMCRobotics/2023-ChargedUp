package frc.robot;

import frc.robot.motor.MotorController;

public class RobotMotors {

    private MotorController frontLeft;
    private MotorController frontRight;
    private MotorController backLeft;
    private MotorController backRight;
    private MotorController gribber;
    private MotorController arm;

    public MotorController getFrontLeftMotor() {
        return frontLeft;
    }

    public MotorController getFrontRightMotor() {
        return frontRight;
    }

    public MotorController getBackLeftMotor() {
        return backLeft;
    }

    public MotorController getBackRightMotor() {
        return backRight;
    }

    public MotorController getGribberMotor() {
        return gribber;
    }

    public MotorController getArmMotor() {
        return arm;
    }

    public RobotMotors(MotorController frontLeft, MotorController frontRight,
            MotorController backLeft, MotorController backRight, MotorController gribber,
            MotorController arm) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.gribber = gribber;
        this.arm = arm;

    }

    public void stopAllMotors() {
        frontLeft.set(0);
        frontRight.set(0);
        backLeft.set(0);
        backRight.set(0);
        gribber.set(0);
        arm.set(0);
    }

    public void stopDriveMotors() {
        frontLeft.set(0);
        frontRight.set(0);
        backLeft.set(0);
        backRight.set(0);
    }
}
