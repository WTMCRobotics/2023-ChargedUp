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

    public double getY() {
        return y;
    }

    public double getTurnAmount() {
        return turnAmount;
    }

    public void updateValues() {
        this.x = controller.getLeftX();
        this.y = controller.getLeftY();
        this.turnAmount = controller.getRightX();
        if (controller.getAButtonPressed()) {
            slowMode = !slowMode;
        }
        if (slowMode) {
            x /= SLOW_MODE_MULTIPLIER;
            y /= SLOW_MODE_MULTIPLIER;
            // Should this value be cut?
            turnAmount /= SLOW_MODE_MULTIPLIER;
        }
    }

    public InputtedControls(XboxController controller) {
        this.controller = controller;
    }

}
