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

    int pressedCount;

    /**
     * Updates the Enum values to the current Guitar values
     */
    public void updateLatestPostionPressed() {
        ArmPosition storedArmPosition = position;
        pressedCount = 0;
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
     * So we can assertEquals ArmPosition enum in Unit Tests. The enum itself can't be public, so
     * this is the patchy solution
     * 
     * @return An array of all the Arm Positions
     */
    public ArmPosition[] armEnum() {
        return ArmPosition.values();
    }

    /**
     * So we can assertEquals GribberState enum in Unit Tests. The enum itself can't be public, so
     * this is the patchy solution.
     * 
     * @return An array of the Gribber states
     */
    public GribberState[] gribberStates() {
        return GribberState.values();

    }
}


/**
 * The states in which the Gribber will be in
 */
enum GribberState {
    OPENING, CLOSING
}


/**
 * The three postions you can move the arm to
 */
enum ArmPosition {
    HIGH, MIDDLE, STARTING_CONFIGURATION
}
