package frc.robot;

import edu.wpi.first.wpilibj.XboxController;

public class InputtedGuitarControls {

    /**
     * The guitar hero controller.
     */
    private XboxController guitar;

    public ArmPosition position;

    int pressedCount;

    public void updateLatestPostionPressed() {
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
    }

    public InputtedGuitarControls(XboxController controller) {
        this.guitar = controller;
    }

    public ArmPosition[] armEnum() {
        return ArmPosition.values();
    }
}


enum ArmPosition {
    HIGH, MIDDLE, STARTING_CONFIGURATION
}
