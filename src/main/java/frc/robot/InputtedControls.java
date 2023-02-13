// package ;
// package frc.robot;
package frc.robot;

import edu.wpi.first.wpilibj.XboxController;

public class InputtedControls {

    private double x;
    private double y;
    private double turnAmount;
    private boolean slowMode;
    private XboxController controller;
    private final double SLOW_MODE_MULTIPLIER = 4;

    public double getX() {
        return x;
    }

    /**
     * @returns strafe amount on the y-axis within a range between -1 and 1
     *          a positive value goes to the left
     */
    public double getY() {
        return y;
    }

    public double getTurnAmount() {
        return turnAmount;
    }

    public void updateValues() {
        this.x = controller.getLeftY();
        this.y = controller.getLeftX();
        this.turnAmount = controller.getRightX();
        if (controller.getAButtonPressed()) {
            slowMode = !slowMode;
        }
        if (slowMode) {
            x /= SLOW_MODE_MULTIPLIER;
            y /= SLOW_MODE_MULTIPLIER;
            // Should this value be cut?++

            // turnAmount /= SLOW_MODE_MULTIPLIER;
        }
    }

    public InputtedControls(XboxController controller) {
        this.controller = controller;
    }

}
