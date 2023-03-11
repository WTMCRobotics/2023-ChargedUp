package frc.robot.AutonomousActions;

import frc.robot.InputtedGuitarControls.ArmPosition;
import frc.robot.AutonomousAction;
import frc.robot.Constants;
import frc.robot.RobotMotors;

public class AutonMoveArm extends AutonomousAction {
    private boolean isFirstTimeRunning;
    private RobotMotors motors;
    private double targetArmDegree;
    private ArmPosition position;


    /**
     * Moves the arm to the specified position
     * 
     * @param position The position to move the arm to. Either PICKING_UP, PICKING_MIDDLE,
     *        PLACING_TOP
     */
    public AutonMoveArm(ArmPosition position) {
        this.isFirstTimeRunning = true;
        this.position = position;
    }

    @Override
    public void passMotors(RobotMotors motors) {
        this.motors = motors;
    }

    @Override
    public boolean executeAndIsDone() {
        if (isFirstTimeRunning) {
            switch (position) {
                case PICKING_UP:
                    AutoMoveArmPickingUp();
                    break;
                case PLACING_MIDDLE:
                    AutoMoveArmPlaceMiddle();
                    break;
                case PLACING_TOP:
                    AutoMoveArmPlaceTop();
                    break;
            }
            isFirstTimeRunning = false;
            return false;
        }

        if (isCloseEnoughToRange()) {
            motors.getArmMotor().set(0);
            return true;
        }
        return false;

    }

    public void AutoMoveArmPickingUp() {
        if (motors.getArmMotor()
                .getEncoderPosition() < degreesToEncoderPostion(Constants.ARM_PICK_UP_POSITION)) {
            motors.getArmMotor().set(0.25);
        }
        if (motors.getArmMotor()
                .getEncoderPosition() > degreesToEncoderPostion(Constants.ARM_PICK_UP_POSITION)
                        + degreesToEncoderPostion(Constants.ARM_POSITION_BUFFER_DEGREES)) {
            motors.getArmMotor().set(-0.25);
        }
        targetArmDegree = Constants.ARM_PICK_UP_POSITION;
    }

    public void AutoMoveArmPlaceTop() {
        if (motors.getArmMotor()
                .getEncoderPosition() < degreesToEncoderPostion(Constants.ARM_PLACE_TOP_POSTION)) {
            motors.getArmMotor().set(0.25);
        }
        if (motors.getArmMotor()
                .getEncoderPosition() > degreesToEncoderPostion(Constants.ARM_PLACE_TOP_POSTION)
                        + degreesToEncoderPostion(Constants.ARM_POSITION_BUFFER_DEGREES)) {
            motors.getArmMotor().set(-0.25);
        }
    }

    public void AutoMoveArmPlaceMiddle() {
        if (motors.getArmMotor().getEncoderPosition() <

                degreesToEncoderPostion(Constants.ARM_PLACE_MIDDLE_POSTION)) {
            motors.getArmMotor().set(0.25);
        }
        if (motors.getArmMotor()
                .getEncoderPosition() > degreesToEncoderPostion(Constants.ARM_PLACE_MIDDLE_POSTION)
                        + degreesToEncoderPostion(Constants.ARM_POSITION_BUFFER_DEGREES)) {
            motors.getArmMotor().set(-0.25);
        }
    }

    public double degreesToEncoderPostion(double inputDegrees) {
        return (inputDegrees / 360);
    }

    private boolean isCloseEnoughToRange() {
        return (motors.getArmMotor()
                .getEncoderPosition() >= (degreesToEncoderPostion(targetArmDegree)
                        - (Constants.ARM_POSITION_BUFFER_DEGREES / 2))
                && motors.getArmMotor()
                        .getEncoderPosition() <= (degreesToEncoderPostion(targetArmDegree)
                                + (Constants.ARM_POSITION_BUFFER_DEGREES / 2)));
    }
}
