package frc.robot;

import edu.wpi.first.wpilibj.XboxController;

public class InputtedGuitarControls {

    /**
     * The guitar hero controller.
     */
    private XboxController guitar;

    private ArmPosition postion;

    int pressedCount;

    public void updateLatestPostionPressed() {
        pressedCount = 0;
        if (guitar.getAButtonPressed()) {
            postion = ArmPosition.STARTING_CONFIGURATION;
            pressedCount++;
        }
        if (guitar.getBButtonPressed()) {
            pressedCount++;
        }
        if (guitar.getYButtonPressed()) {
            pressedCount++;
        }
    }

    public InputtedGuitarControls(XboxController controller) {
        this.guitar = controller;
    }
}


enum ArmPosition {
    HIGH, MIDDLE, STARTING_CONFIGURATION
}
