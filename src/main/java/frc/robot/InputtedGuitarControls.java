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
        if (Timer.getMatchTime() < 0.75 && Timer.getMatchTime() > 0) {
            gribberController.set(-1);
            return;
        }
        if (Timer.getFPGATimestamp() > timeSinceGribberStateChange + 0.75) {
            if (gribberState == GribberState.OPENING) {
                gribberController.set(.30);
                // System.out.println("Set to 0");
            } else {
                gribberController.set(0);
                // System.out.println("Set to -30");
            }
            return;
        }
        if (gribberState == GribberState.CLOSING) {
            gribberController.set(-1);

        }

        if (gribberState == GribberState.OPENING) {
            gribberController.set(.60);
        }
    }

    public void moveArmController() {
        if (guitar.getPOV() == 0) {
            if (armController.getEncoderPosition() < degreesToEncoderPosition(
                    Constants.MAX_ARM_UP_DEGREES)) {
                armController.set(Constants.ARM_MOVE_UP_SPEED);
            }
            if (armController.getEncoderPosition() > degreesToEncoderPosition(
                    Constants.MAX_ARM_UP_DEGREES)) {
                armController.set(0);
            }
            position = ArmPosition.MANUAL;
            return;
        } else if (guitar.getPOV() == 180) {
            armController.set(-Constants.ARM_MOVE_DOWN_SPEED);
            if (armController.getEncoderPosition() < 0) {
                armController.set(0);
            }
            position = ArmPosition.MANUAL;
            return;
        }
        armController.set(0);
        // Manual adjust arm

        double encoderBuffer = degreesToEncoderPosition(Constants.ARM_POSITION_BUFFER_DEGREES);
        if (position == ArmPosition.MANUAL) {
            return;
        } else if (position == ArmPosition.PLACING_TOP) {
            if (armController.getEncoderPosition() < degreesToEncoderPosition(
                    Constants.ARM_PLACE_TOP_POSITION) - encoderBuffer) {
                armController.set(Constants.ARM_MOVE_UP_SPEED);
            }
            if (armController.getEncoderPosition() > degreesToEncoderPosition(
                    Constants.ARM_PLACE_TOP_POSITION) + encoderBuffer) {
                armController.set(-Constants.ARM_MOVE_DOWN_SPEED);
            }
            return;
        } else if (position == ArmPosition.PLACING_MIDDLE) {
            if (armController.getEncoderPosition() < degreesToEncoderPosition(
                    Constants.ARM_PLACE_MIDDLE_POSITION) - encoderBuffer) {
                armController.set(Constants.ARM_MOVE_UP_SPEED);
            }
            if (armController.getEncoderPosition() > degreesToEncoderPosition(
                    Constants.ARM_PLACE_MIDDLE_POSITION) + encoderBuffer) {

                armController.set(-Constants.ARM_MOVE_DOWN_SPEED);
            }
            return;
        } else if (position == ArmPosition.INTAKE) {
            if (armController
                    .getEncoderPosition() < degreesToEncoderPosition(Constants.ARM_INTAKE_POSITION)
                            - encoderBuffer) {
                armController.set(Constants.ARM_MOVE_UP_SPEED);
            }
            if (armController
                    .getEncoderPosition() > degreesToEncoderPosition(Constants.ARM_INTAKE_POSITION)
                            + encoderBuffer) {

                armController.set(-Constants.ARM_MOVE_DOWN_SPEED);
            }
            return;

        } else if (position == ArmPosition.FLIP_CONE) {
            if (armController.getEncoderPosition() < degreesToEncoderPosition(
                    Constants.ARM_FLIP_CONE_POSITION) - encoderBuffer) {
                armController.set(Constants.ARM_MOVE_UP_SPEED);
            }
            if (armController.getEncoderPosition() > degreesToEncoderPosition(
                    Constants.ARM_FLIP_CONE_POSITION) + encoderBuffer) {

                armController.set(-Constants.ARM_MOVE_DOWN_SPEED);
            }
        } else if (position == ArmPosition.PICKING_UP) {
            if (armController
                    .getEncoderPosition() < degreesToEncoderPosition(Constants.ARM_PICK_UP_POSITION)
                            - encoderBuffer) {
                armController.set(Constants.ARM_MOVE_UP_SPEED);
            }
            if (armController
                    .getEncoderPosition() > degreesToEncoderPosition(Constants.ARM_PICK_UP_POSITION)
                            + encoderBuffer) {

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
        if (guitar.getAButtonPressed()) {
            position = ArmPosition.PLACING_TOP;
            pressedCount++;
        }

        if (guitar.getBButtonPressed()) {
            position = ArmPosition.PLACING_MIDDLE;
            pressedCount++;
        }
        if (guitar.getYButtonPressed()) {
            position = ArmPosition.INTAKE;
        }
        if (guitar.getXButton()) {
            position = ArmPosition.FLIP_CONE;
            pressedCount++;
        }
        if (guitar.getLeftBumperPressed()) {
            position = ArmPosition.PICKING_UP;
        }

        if (pressedCount > 1) {
            position = storedArmPosition;
        }
        if (position != ArmPosition.MANUAL) {
            System.out.println("THe position is " + position);
        }
        if (guitar.getLeftTriggerAxis() < 0.49) {
            lightColor = LightColor.CUBE;
        } else if (guitar.getLeftTriggerAxis() > 0.51) {
            lightColor = LightColor.CONE;
        } else {
            lightColor = LightColor.NONE;
        }
        if (guitar.getStartButtonPressed()) {
            gribberState = GribberState.CLOSING;
            timeSinceGribberStateChange = Timer.getFPGATimestamp();
        }
        if (guitar.getBackButtonPressed()) {
            gribberState = GribberState.OPENING;
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

    public double degreesToEncoderPosition(double inputDegrees) {
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
        PICKING_UP, FLIP_CONE, PLACING_TOP, PLACING_MIDDLE, INTAKE, MANUAL
    }

    public static enum LightColor {
        CONE, CUBE, NONE
    }

}
