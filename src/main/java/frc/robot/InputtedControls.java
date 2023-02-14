// package ;
// package frc.robot;
package frc.robot;

import edu.wpi.first.wpilibj.XboxController;

public class InputtedControls {


    private boolean slowMode;
    private XboxController controller;
    private final double SLOW_MODE_MULTIPLIER = 4;

    /**
     * Positive values mean move forward; negative values mean move backwards
     * 
     * @returns move amount on the x-axis within a range of -1 and 1
     */
    public double getX() {
        if (slowMode)
            return controller.getLeftY() / SLOW_MODE_MULTIPLIER;
        return controller.getLeftY();

    }

    /**
     * Positive values mean strafe to the left; negative means to the right
     * 
     * @returns strafe amount on the y-axis within a range of -1 and 1
     */
    public double getY() {
        if (slowMode)
            return controller.getLeftX() / SLOW_MODE_MULTIPLIER;
        return controller.getLeftX();
    }

    /**
     * Postive values indicate clockwise rotation; negative is counter-clockwise
     * 
     * @return the amount of rotation expected between -1 and 1
     */
    public double getTurnAmount() {
        // No slowmode for rotation, maybe change later?
        return controller.getRightX();
    }

    public void updateValues() {
        if (controller.getAButtonPressed()) {
            slowMode = !slowMode;
        }

    }

    public InputtedControls(XboxController controller) {
        this.controller = controller;
    }

}
