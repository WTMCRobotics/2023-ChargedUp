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
     * Holds if the gribber should be opening or closing
     */
    public GribberState gribberState;

    public boolean redLEDOn = false;
    public boolean greenLEDOn = false;
    public boolean blueLEDOn = false;


    int pressedCount;
    private MotorController armController;
    private MotorController gribberController;

    double timeSinceGribberStateChange = 0.0;

    private void moveGribberController() {
        if (Timer.getFPGATimestamp() > timeSinceGribberStateChange + 0.75) {
            if (gribberState == GribberState.OPENING) {
                gribberController.set(0);
                // System.out.println("Set to 0");
            } else {
                gribberController.set(-0);
                // System.out.println("Set to -30");
            }
            return;
        }
        if (gribberState == GribberState.CLOSING) {
            gribberController.set(-0.5);

        }

        if (gribberState == GribberState.OPENING) {
            gribberController.set(.50);
        }
    }

    public void moveArmController() {
        double armMoveSpeed = -guitar.getRightY() / Constants.ARM_SPEED_DIVISOR;
        if (Math.abs(armMoveSpeed) > 0.08) {
            armController.set(armMoveSpeed);

            if (armController.getEncoderPosition() > degreesToEncoderPosition(
                    Constants.MAX_ARM_UP_DEGREES)) {
                armController.set(0);
            }
            if (armController.getEncoderPosition() < 0) {
                armController.set(0);
            }

            return;
        }
        armController.set(0);

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

        if (guitar.getPOV() == 270) {
            redLEDOn = true;
            System.out.println("RED");
        } else if (guitar.getPOV() == 0) {
            greenLEDOn = true;
            System.out.println("GREEN");
        } else if (guitar.getPOV() == 90) {
            System.out.println("BLUE");
            blueLEDOn = true;
        } else if (guitar.getPOV() == 180) {
            redLEDOn = false;
            greenLEDOn = false;
            blueLEDOn = false;
        }
        if (guitar.getBButtonPressed()) {
            gribberState = GribberState.CLOSING;
            timeSinceGribberStateChange = Timer.getFPGATimestamp();
        }
        if (guitar.getAButtonPressed()) {
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


}
