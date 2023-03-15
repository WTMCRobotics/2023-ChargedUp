package frc.robot;

import edu.wpi.first.wpilibj.Timer;
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
    public ArmPosition position = ArmPosition.MANUAL;
    /**
     * Holds if the gribber should be opening or closing
     */
    public GribberState gribberState;

    public LightColor lightColor;

    int pressedCount;
    private MotorController armController;
    private MotorController gribberController;

    double timeSinceGribberStateChange = 0.0;

    private void moveGribberController() {
        // TODO: Overthrow Brazilian government
        if (Timer.getFPGATimestamp() > timeSinceGribberStateChange + 0.75) {
            gribberController.set(0);
            return;
        }
        if (gribberState == GribberState.OPENING) {
            gribberController.set(.90);

        }

        if (gribberState == GribberState.CLOSING) {
            gribberController.set(-.40);
        }
    }

    public void moveArmController() {
        if (guitar.getXButton()) {
            if (armController.getEncoderPosition() < degreesToEncoderPostion(Constants.MAX_ARM_UP_DEGREES)) {
                armController.set(Constants.ARM_MOVE_UP_SPEED);
            }
            if (armController.getEncoderPosition() > degreesToEncoderPostion(Constants.MAX_ARM_UP_DEGREES)) {
                armController.set(0);
            }
            position = ArmPosition.MANUAL;
            return;
        } else if (guitar.getLeftBumper()) {
            // if (Constants.bottomArmLimitSwitch.get()) {
            // return;
            // }
            position = ArmPosition.MANUAL;
            armController.set(-Constants.ARM_MOVE_DOWN_SPEED);
            return;
        }
        armController.set(0);
        // Manual adjust arm

        double encoderBuffer = degreesToEncoderPostion(Constants.ARM_POSITION_BUFFER_DEGREES);
        if (position == ArmPosition.MANUAL) {
            return;
        } else if (position == ArmPosition.PLACING_TOP) {
            if (armController
                    .getEncoderPosition() < degreesToEncoderPostion(Constants.ARM_PLACE_TOP_POSTION)
                            - encoderBuffer) {
                armController.set(Constants.ARM_MOVE_UP_SPEED);
            }
            if (armController
                    .getEncoderPosition() > degreesToEncoderPostion(Constants.ARM_PLACE_TOP_POSTION)
                            + encoderBuffer) {
                // if (Constants.bottomArmLimitSwitch.get()) {
                // return;
                // }
                armController.set(-Constants.ARM_MOVE_DOWN_SPEED);
            }
            return;
        } else if (position == ArmPosition.PLACING_MIDDLE) {
            if (armController.getEncoderPosition() < degreesToEncoderPostion(
                    Constants.ARM_PLACE_MIDDLE_POSTION) - encoderBuffer) {
                armController.set(Constants.ARM_MOVE_UP_SPEED);
            }
            if (armController.getEncoderPosition() > degreesToEncoderPostion(
                    Constants.ARM_PLACE_MIDDLE_POSTION) + encoderBuffer) {
                // if (Constants.bottomArmLimitSwitch.get()) {
                // return;
                // }
                armController.set(-Constants.ARM_MOVE_DOWN_SPEED);
            }
            return;

        } else if (position == ArmPosition.PICKING_UP) {
            if (armController
                    .getEncoderPosition() < degreesToEncoderPostion(Constants.ARM_PICK_UP_POSITION)
                            - encoderBuffer) {
                armController.set(Constants.ARM_MOVE_UP_SPEED);
            }
            if (armController
                    .getEncoderPosition() > degreesToEncoderPostion(Constants.ARM_PICK_UP_POSITION)
                            + encoderBuffer) {
                // if (Constants.bottomArmLimitSwitch.get()) {
                // return;
                // }
                armController.set(-Constants.ARM_MOVE_DOWN_SPEED);
            }
        }

    }

    public void doEveryFrame() {
        // System.out.println("Endoder position is "
        // + encoderPositionToDegrees(armController.getEncoderPosition()));
        updateLatestPostionPressed();
        moveArmController();
        moveGribberController();
    }

    /**
     * Updates the Enum values to the current Guitar values
     */
    public void updateLatestPostionPressed() {
        ArmPosition storedArmPosition = position;
        pressedCount = 0;
        // probably not the best way to be doing things, but who knows
        if (guitar.getYButtonPressed()) {
            position = ArmPosition.PICKING_UP;
            pressedCount++;
        }
        if (guitar.getBButtonPressed()) {
            position = ArmPosition.PLACING_MIDDLE;
            pressedCount++;
        }
        if (guitar.getAButtonPressed()) {
            position = ArmPosition.PLACING_TOP;
            pressedCount++;
        }
        if (pressedCount > 1) {
            position = storedArmPosition;
        }
        if (guitar.getPOV() == 0) {
            lightColor = LightColor.CUBE;
        }
        if (guitar.getPOV() == 180) {
            lightColor = LightColor.CONE;
        }
        if (guitar.getStartButtonPressed()) {
            gribberState = GribberState.OPENING;
            timeSinceGribberStateChange = Timer.getFPGATimestamp();
        }
        if (guitar.getBackButtonPressed()) {
            gribberState = GribberState.CLOSING;
            timeSinceGribberStateChange = Timer.getFPGATimestamp();
        }
    }

    /**
     * Keeps track of where motor should be moving based on the inputs on the guitar
     * 
     * @param controller The Guitar Hero controller
     */
    public InputtedGuitarControls(XboxController controller, MotorController armMotor,
            MotorController gribberMotor) {
        this.guitar = controller;
        this.armController = armMotor;
        this.gribberController = gribberMotor;
        armController.setEncoderPosition(0);
        gribberMotor.setEncoderPosition(0);
        // Endocedder
    }

    public double degreesToEncoderPostion(double inputDegrees) {
        return (inputDegrees / 360);
    }

    public double encoderPositionToDegrees(double inputEncoderPosition) {
        return (inputEncoderPosition * 360);
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
        PICKING_UP, PLACING_TOP, PLACING_MIDDLE, MANUAL
    }

    public static enum LightColor {
        CONE, CUBE, NONE
    }

}
