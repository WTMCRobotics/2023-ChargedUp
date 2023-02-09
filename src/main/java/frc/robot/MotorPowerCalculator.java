package frc.robot;

public class MotorPowerCalculator {
    InputtedControls controls;
    double scaleDownFactor;

    public MotorPowerCalculator(InputtedControls controls) {
        this.controls = controls;
        scaleDownFactor = Math.max(Math.abs(controls.getY()) + Math.abs(controls.getX())
                + Math.abs(controls.getTurnAmount()), 1);

    }

    public MotorPowerCalculator() {

    }

    public void updateValues(InputtedControls controls) {
        this.controls = controls;
        this.scaleDownFactor = Math.max(Math.abs(controls.getY()) + Math.abs(controls.getX())
                + Math.abs(controls.getTurnAmount()), 1);
    }

    public double getScaleDownFactor() {
        if (controls.isSlowMode()) {
            return scaleDownFactor * 4;
        }
        return scaleDownFactor;
    }

    public double getFrontLeftMotorPower() {

        return (controls.getY() + controls.getX() + controls.getTurnAmount())
                / getScaleDownFactor();
    }

    public double getFrontRightMotorPower() {
        return (controls.getY() - controls.getX() - controls.getTurnAmount())
                / getScaleDownFactor();
    }

    public double getBackLeftMotorPower() {

        return (controls.getY() - controls.getX() + controls.getTurnAmount())
                / getScaleDownFactor();
    }

    public double getBackRightMotorPower() {

        return (controls.getY() + controls.getX() - controls.getTurnAmount())
                / getScaleDownFactor();
    }
}
