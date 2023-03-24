package frc.robot.AutonomousActions;

import frc.robot.AutonomousAction;
import frc.robot.RobotMotors;

public class AutonArmCalibrate extends AutonomousAction {

    private RobotMotors motors;
    boolean closeGribber;
    boolean isFirstTimeRunning;

    /**
     * Attempts to calibrate the arm by moving it to the bottom position
     */
    public AutonArmCalibrate(boolean closeGribber, RobotMotors motors) {
        this.isFirstTimeRunning = true;
        this.closeGribber = closeGribber;
        this.motors = motors;
    }

    public boolean executeAndIsDone() {
        if (isFirstTimeRunning) {
            motors.getArmMotor().set(-0.50);
            if (closeGribber) {
                motors.getGribberMotor().set(-0.65);
            }
            isFirstTimeRunning = false;
            return false;
        }
        if (motors.getArmMotor().getReverseLimit()) {
            motors.getArmMotor().set(0.0);
            motors.getGribberMotor().set(0);
            motors.getArmMotor().setEncoderPosition(0.0);
            System.out.println("Arm calibrated!");
            return true;
        }
        return false;

    }

}

