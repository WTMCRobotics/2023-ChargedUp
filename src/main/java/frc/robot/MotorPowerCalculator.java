package frc.robot;

public class MotorPowerCalculator {
    InputtedControls controls;
    double scaleDownFactor;

    public MotorPowerCalculator(InputtedControls controls) {
        this.controls = controls;
        scaleDownFactor = Math.max(Math.abs(controls.getY()) + Math.abs(controls.getX())
                + Math.abs(controls.getTurnAmount()), 1);
    }

    public double getScaleDownFactor() {
        if (controls.isSlowMode()) {
            return scaleDownFactor * 4;
        }
        return scaleDownFactor;
    }

    public double getFrontLeftMotorPower() {
        if (controls.isSlowMode()) {
            return (controls.getY() + controls.getX() + controls.getTurnAmount())
                    / (scaleDownFactor * 4);
        }
        return (controls.getY() + controls.getX() + controls.getTurnAmount()) / scaleDownFactor;
    }

    public double getFrontRightMotorPower() {
        return (controls.getY() - controls.getX() - controls.getTurnAmount())
                / getScaleDownFactor();
    }

    public double getBackLeftMotorPower() {
        if (controls.isSlowMode()) {
            return (controls.getY() + controls.getX() - controls.getTurnAmount())
                    / (scaleDownFactor * 4);
        }
        return (controls.getY() + controls.getX() - controls.getTurnAmount()) / scaleDownFactor;
    }

    public double getBackRightMotorPower() {
        if (controls.isSlowMode()) {
            return (controls.getY() - controls.getX() + controls.getTurnAmount())
                    / (scaleDownFactor * 4);
        }
        return (controls.getY() - controls.getX() + controls.getTurnAmount()) / scaleDownFactor;
    }
}
