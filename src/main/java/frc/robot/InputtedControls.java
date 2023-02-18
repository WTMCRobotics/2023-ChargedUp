// package ;
// package frc.robot;
package frc.robot;

import edu.wpi.first.wpilibj.XboxController;

public class InputtedControls {

    /**
     * The controller this class get inputs from
     */
    private XboxController controller;
    /**
     * The amount of times slower you want the robot to move while slowmode is enabled
     */
    private final double SLOW_MODE_MULTIPLIER = 4;

    /**
     * Positive values mean move forward; negative values mean move backwards
     * 
     * @returns move amount on the x-axis within a range of -1 and 1
     */
    public double getLeftJoystickY() {
        double controllerY = controller.getLeftY();
        if (isSlowMode()) {
            controllerY /= SLOW_MODE_MULTIPLIER;
        }

        // this has to be inverted
        return -controllerY;

    }

    /**
     * Positive values mean strafe to the left; negative values mean to the right
     * 
     * @returns strafe amount on the y-axis within a range of -1 and 1
     */
    public double getLeftJoystickX() {
        double controllerX = controller.getLeftX();
        if (isSlowMode()) {
            controllerX /= SLOW_MODE_MULTIPLIER;
        }
        // TODO: Exponential Scaling

        return controllerX;

    }

    /**
     * Postive values indicate clockwise rotation; negative is counter-clockwise
     * 
     * @return the amount of rotation expected between -1 and 1
     */
    public double getTurnAmount() {
        double rightJoystickX = controller.getRightX();

        if (isSlowMode()) {
            rightJoystickX /= SLOW_MODE_MULTIPLIER;
        }

        return rightJoystickX;
    }

    /**
     * Updates the value of slowmode, then return it
     */
    private boolean isSlowMode() {
        return (controller.getRightTriggerAxis() > .5);
    }

    /**
     * Holds all the values of the Xbox controller.
     * 
     * @param controller the Xbox controller you want to get inputs from
     */
    public InputtedControls(XboxController controller) {
        this.controller = controller;
    }

}
