// package ;
// package frc.robot;
package frc.robot;

import edu.wpi.first.wpilibj.XboxController;

public class InputtedControls {

    /**
     * strafe amount on the x-axis within a range between -1 and 1 a positive value goes forward
     */

    /**
     * strafe amount on the y-axis within a range between -1 and 1 a positive value goes left
     */


    private double turnAmount;
    private boolean slowMode;
    private XboxController controller;
    private final double SLOW_MODE_MULTIPLIER = 4;

    /**
     * @returns strafe amount on the x-axis within a range between -1 and 1 a positive value goes
     *          forward
     */
    public double getX() {
        if (slowMode)
            return controller.getLeftY() / SLOW_MODE_MULTIPLIER;
        return controller.getLeftY();

    }

    /**
     * @returns strafe amount on the y-axis within a range between -1 and 1 a positive value goes to
     *          the left
     */
    public double getY() {
        if (slowMode)
            return controller.getLeftX() / SLOW_MODE_MULTIPLIER;
        return controller.getLeftX();
    }

    public double getTurnAmount() {
        return turnAmount;
    }

    public void updateValues() {
        this.turnAmount = controller.getRightX();
        if (controller.getAButtonPressed()) {
            slowMode = !slowMode;
        }

    }

    public InputtedControls(XboxController controller) {
        this.controller = controller;
    }

}
