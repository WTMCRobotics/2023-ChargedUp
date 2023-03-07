package frc.robot;

import frc.robot.motor.MotorController;

public class RobotMotors {

    private MotorController frontLeft;
    private MotorController frontRight;
    private MotorController backLeft;
    private MotorController backRight;

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

    public RobotMotors(MotorController frontLeft, MotorController frontRight,
            MotorController backLeft, MotorController backRight) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;

    }

    public void stopAllMotors() {
        frontLeft.set(0);
        frontRight.set(0);
        backLeft.set(0);
        backRight.set(0);
    }
}
