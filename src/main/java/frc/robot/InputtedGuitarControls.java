package frc.robot;

import edu.wpi.first.wpilibj.XboxController;

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

    /**
     * Updates the Enum values to the current Guitar values
     */
    public void updateLatestPostionPressed() {
        ArmPosition storedArmPosition = position;
        pressedCount = 0;
        // probably not the best way to be doing things, but who knows
        if (guitar.getAButtonPressed()) {
            position = ArmPosition.STARTING_CONFIGURATION;
            pressedCount++;
        }
        if (guitar.getBButtonPressed()) {
            position = ArmPosition.MIDDLE;
            pressedCount++;
        }
        if (guitar.getYButtonPressed()) {
            position = ArmPosition.HIGH;
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
    public InputtedGuitarControls(XboxController controller) {
        this.guitar = controller;
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
        HIGH, MIDDLE, STARTING_CONFIGURATION
    }


    public static enum LightColor {
        CONE, CUBE, NONE
    }

}


