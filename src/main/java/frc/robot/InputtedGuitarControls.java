package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.motor.MotorController;

public class InputtedGuitarControls {

    /**
     * The guitar hero controller.
     */
    private XboxController guitar;
    /**
     * The current position the arm should be moving to.
     */
    public ArmPosition position;
    /**
     * Holds if the gribber should be opening or closing
     */
    public GribberState gribberState;

    public LightColor lightColor;

    int pressedCount;
    private MotorController armController;
    private MotorController gribberController;

    public void moveArmController() {
        armController.set(0);
        if (position == ArmPosition.PLACING_TOP) {
            if (armController.getEncoderPosition() < degreesToEncoderPostion(Constants.ARM_PLACE_TOP_POSTION)) {
                armController.set(.25);
            }
        } else if (position == ArmPosition.PLACING_MIDDLE) {
            if (armController.getEncoderPosition() < degreesToEncoderPostion(Constants.ARM_PLACE_TOP_POSTION)) {
                armController.set(0.25);
            }
            if (armController.getEncoderPosition() > degreesToEncoderPostion(Constants.ARM_PLACE_TOP_POSTION)
                    + degreesToEncoderPostion(Constants.ARM_POSITION_BUFFER_DEGREES)) {
                armController.set(-0.25);
            }
        } else if (position == ArmPosition.PICKING_UP) {
            if (armController.getEncoderPosition() < degreesToEncoderPostion(Constants.ARM_PICK_UP_POSITION)) {
                armController.set(0.25);
            }
            if (armController.getEncoderPosition() > degreesToEncoderPostion(Constants.ARM_PICK_UP_POSITION)
                    + degreesToEncoderPostion(Constants.ARM_POSITION_BUFFER_DEGREES)) {
                armController.set(-0.25);
            }
        }

    }

    public void doEveryFrame() {
        updateLatestPostionPressed();
        moveArmController();
    }

    /**
     * Updates the Enum values to the current Guitar values
     */
    public void updateLatestPostionPressed() {
        ArmPosition storedArmPosition = position;
        pressedCount = 0;
        // probably not the best way to be doing things, but who knows
        if (guitar.getAButtonPressed()) {
            position = ArmPosition.PICKING_UP;
            pressedCount++;
        }
        if (guitar.getBButtonPressed()) {
            position = ArmPosition.PLACING_MIDDLE;
            pressedCount++;
        }
        if (guitar.getYButtonPressed()) {
            position = ArmPosition.PLACING_TOP;
            pressedCount++;
        }
        if (pressedCount > 1) {
            position = storedArmPosition;
        }
        if (guitar.getPOV() == 0) {
            gribberState = GribberState.OPENING;
        }
        if (guitar.getPOV() == 180) {
            gribberState = GribberState.CLOSING;
        }
        if (guitar.getStartButtonPressed()) {
            lightColor = LightColor.CUBE;
        }
        if (guitar.getBackButtonPressed()) {
            lightColor = LightColor.CONE;
        }
        if (guitar.getStartButtonPressed() && guitar.getBackButtonPressed()) {
            lightColor = LightColor.NONE;
        }
    }

    /**
     * Keeps track of where motor should be moving based on the inputs on the guitar
     * 
     * @param controller The Guitar Hero controller
     */
    public InputtedGuitarControls(XboxController controller, MotorController armMotor) {
        this.guitar = controller;
        this.armController = armMotor;

        armController.getEncoderPosition();
        armController.setEncoderPosition(0);
        // Endocedder
    }

    public double degreesToEncoderPostion(double inputDegrees) {
        return (inputDegrees / 360);
    }

    /**
     * The states in which the Gribber will be in
     */
    public static enum GribberState {
        OPENING, CLOSING
    }

    /**
     * The three postions you can move the arm to
     */
    public static enum ArmPosition {
        PICKING_UP, PLACING_TOP, PLACING_MIDDLE
    }

    public static enum LightColor {
        CONE, CUBE, NONE
    }

}
