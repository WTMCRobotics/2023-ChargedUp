package frc.robot;

/**
 * AutonAction
 */

public class AutonAction {
    public MovmentType movmentType;
    public double amount;
    public double speed;

    public AutonAction(MovmentType movmentType, double amount, double speed) {
        this.movmentType = movmentType;
        this.amount = amount;
        this.speed = speed;
    }

    public static enum MovmentType {
        FORWARD, BACKWARD, LEFT, RIGHT, TURN_LEFT, TURN_RIGHT

    }
    // i dropped a heavy slab on my cat mittens
}
