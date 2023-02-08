// package ;
// package frc.robot;
package frc.robot;

public class InputtedControls {


    private double x;
    private double y;
    private double turnAmount;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getTurnAmount() {
        return turnAmount;
    }

    public void setTurnAmount(double turnAmount) {
        this.turnAmount = turnAmount;
    }

    public InputtedControls(double x, double y, double turnAmount) {
        this.x = x;
        this.y = y;
        this.turnAmount = turnAmount;

    }
}
