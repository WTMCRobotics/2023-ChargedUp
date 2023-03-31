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
    public AutonMoveArm(ArmPosition position, RobotMotors motors) {
        this.isFirstTimeRunning = true;
        this.position = position;
        this.motors = motors;
    }

    @Override
    public boolean executeAndIsDone() {
        if (isFirstTimeRunning) {
            System.out.println("Setting motor power!");
            switch (position) {
                case PLACING_TOP:
                    AutoMoveArmPlaceTop();
                    break;
                case PLACING_MIDDLE:
                    AutoMoveArmPlaceMiddle();
                    break;
                case INTAKE:
                    AutoIntake();
                    break;
                case FLIP_CONE:
                    AutoMoveArmFlipCone();
                    break;
                case PICKING_UP:
                    AutoMoveArmPickingUp();
                    break;
                default:
                    break;
            }
            isFirstTimeRunning = false;
            return false;

        }

        if (isCloseEnoughToRange()) {
            System.out.println(
                    "The arm is done moving, as " + motors.getArmMotor().getEncoderPosition()
                            + " is close enought to" + degreesToEncoderPostion(targetArmDegree));
            motors.getArmMotor().set(0);
            return true;
        }
        return false;

    }

    public void AutoMoveArmPickingUp() {
        if (motors.getArmMotor()
                .getEncoderPosition() < degreesToEncoderPostion(Constants.ARM_PICK_UP_POSITION)) {
            motors.getArmMotor().set(1);
        }
        if (motors.getArmMotor()
                .getEncoderPosition() > degreesToEncoderPostion(Constants.ARM_PICK_UP_POSITION)
                        + degreesToEncoderPostion(Constants.ARM_POSITION_BUFFER_DEGREES)) {
            motors.getArmMotor().set(-1);
        }
        targetArmDegree = Constants.ARM_PICK_UP_POSITION;
    }

    // does something (probably)
    public void AutoIntake() {
        if (motors.getArmMotor()
                .getEncoderPosition() < degreesToEncoderPostion(Constants.ARM_INTAKE_POSITION)) {
            motors.getArmMotor().set(1);
        }
        if (motors.getArmMotor()
                .getEncoderPosition() > degreesToEncoderPostion(Constants.ARM_INTAKE_POSITION)
                        + degreesToEncoderPostion(Constants.ARM_POSITION_BUFFER_DEGREES)) {
            motors.getArmMotor().set(-1);
        }
        targetArmDegree = Constants.ARM_INTAKE_POSITION;
    }

    public void AutoMoveArmPlaceTop() {
        if (motors.getArmMotor()
                .getEncoderPosition() < degreesToEncoderPostion(Constants.ARM_PLACE_TOP_POSITION)) {
            motors.getArmMotor().set(1);
        }
        if (motors.getArmMotor()
                .getEncoderPosition() > degreesToEncoderPostion(Constants.ARM_PLACE_TOP_POSITION)
                        + degreesToEncoderPostion(Constants.ARM_POSITION_BUFFER_DEGREES)) {
            motors.getArmMotor().set(-1);
        }
        targetArmDegree = Constants.ARM_PLACE_TOP_POSITION;
    }

    public void AutoMoveArmPlaceMiddle() {
        if (motors.getArmMotor().getEncoderPosition() <

                degreesToEncoderPostion(Constants.ARM_PLACE_MIDDLE_POSITION)) {
            motors.getArmMotor().set(1);
        }
        if (motors.getArmMotor()
                .getEncoderPosition() > degreesToEncoderPostion(Constants.ARM_PLACE_MIDDLE_POSITION)
                        + degreesToEncoderPostion(Constants.ARM_POSITION_BUFFER_DEGREES)) {
            motors.getArmMotor().set(-1);
        }
        targetArmDegree = Constants.ARM_PLACE_MIDDLE_POSITION;
    }

    public void AutoMoveArmFlipCone() {
        if (motors.getArmMotor().getEncoderPosition() <

                degreesToEncoderPostion(Constants.ARM_FLIP_CONE_POSITION)) {
            motors.getArmMotor().set(0.6);
        }
        if (motors.getArmMotor()
                .getEncoderPosition() > degreesToEncoderPostion(Constants.ARM_FLIP_CONE_POSITION)
                        + degreesToEncoderPostion(Constants.ARM_POSITION_BUFFER_DEGREES)) {
            motors.getArmMotor().set(-0.6);
        }
        targetArmDegree = Constants.ARM_FLIP_CONE_POSITION;
    }

    public double degreesToEncoderPostion(double inputDegrees) {
        return (inputDegrees / 360.0);
    }

    private boolean isCloseEnoughToRange() {
        return (Math.abs(degreesToEncoderPostion(targetArmDegree)
                - Math.abs(motors.getArmMotor().getEncoderPosition())) <= degreesToEncoderPostion(
                        Constants.ARM_POSITION_BUFFER_DEGREES));
    }
}
