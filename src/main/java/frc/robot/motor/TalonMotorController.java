package frc.robot.motor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Constants;
import frc.robot.Robot;

public class TalonMotorController implements MotorController {
    Robot robot;
    public TalonSRX controller;

    TalonMotorController(int canID, Robot robot) {
        this.robot = robot;
        controller = new TalonSRX(canID);
    }



    @Override
    public void reset() {
        controller.configFactoryDefault();
    }

    @Override
    public void setPercentOutput(double speed) {
        controller.set(ControlMode.PercentOutput, speed);
    }

    @Override
    public void setVelocity(double speed) {
        // convert from RPM to ticks/100 ms
        // 600 * (100 ms) = 1 minute
        controller.set(ControlMode.Velocity, speed * Constants.encoderRotation / 600);
    }

    @Override
    public void setDistance(double inches) {
        controller.set(ControlMode.MotionMagic,
                inches * Constants.encoderRotation / robot.circumference);
    }

    @Override
    public void setBrakeMode(boolean brake) {
        controller.setNeutralMode(brake ? NeutralMode.Brake : NeutralMode.Coast);
    }

    @Override
    public void setInverted(boolean inverted) {
        controller.setInverted(inverted);
    }

    @Override
    public void follow(MotorController leader) {
        if (!(leader instanceof TalonMotorController))
            throw new IllegalArgumentException(
                    "Leader must be the same type of motor controller as the follower");
        controller.follow(((TalonMotorController) leader).controller);
    }

    @Override
    public void setSensorSource() {
        controller.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
                Constants.PID_LOOP_IDX, Constants.TIMEOUT_MS);
    }

    @Override
    public void setNeutralDeadband(double percent) {
        controller.configNeutralDeadband(percent, Constants.TIMEOUT_MS);
    }

    @Override
    public void setEncoderInverted(boolean inverted) {
        controller.setSensorPhase(inverted);
    }

    @Override
    public void setStatusFramePeriod(int period) {
        controller.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, period,
                Constants.TIMEOUT_MS);
        controller.setStatusFramePeriod(StatusFrameEnhanced.Status_10_Targets, period,
                Constants.TIMEOUT_MS);
    }

    @Override
    public void setOutputLimits(double nominalForward, double nominalReverse, double peakForward,
            double peakReverse) {
        controller.configNominalOutputForward(nominalForward, Constants.TIMEOUT_MS);
        controller.configNominalOutputReverse(nominalReverse, Constants.TIMEOUT_MS);
        controller.configPeakOutputForward(peakForward, Constants.TIMEOUT_MS);
        controller.configPeakOutputReverse(peakReverse, Constants.TIMEOUT_MS);
    }

    @Override
    public void setPID(double P, double I, double D, double F) {
        controller.selectProfileSlot(Constants.SLOT_IDX, Constants.PID_LOOP_IDX);
        controller.config_kP(Constants.SLOT_IDX, P, Constants.TIMEOUT_MS);
        controller.config_kI(Constants.SLOT_IDX, I, Constants.TIMEOUT_MS);
        controller.config_kD(Constants.SLOT_IDX, D, Constants.TIMEOUT_MS);
        controller.config_kF(Constants.SLOT_IDX, F, Constants.TIMEOUT_MS);
        controller.configSetParameter(null, F, 0, 0);
    }

    @Override
    public double getActiveTrajectoryVelocity() {
        return controller.getActiveTrajectoryVelocity() / Constants.encoderRotation;
    }

    @Override
    public double getSensorVelocity() {
        return controller.getSelectedSensorVelocity() / Constants.encoderRotation;
    }

    @Override
    public void setMotionSpeed(double cruiseVelocity, double acceleration) {
        controller.configMotionCruiseVelocity(cruiseVelocity, Constants.TIMEOUT_MS);
        controller.configMotionAcceleration(acceleration, Constants.TIMEOUT_MS);
    }

    @Override
    public double getEncoderPosition() {
        return controller.getSelectedSensorPosition() / Constants.encoderRotation;
    }

    @Override
    public void setEncoderPosition(double position) {
        controller.setSelectedSensorPosition(position * Constants.encoderRotation);
    }

    @Override
    public boolean getForwardLimit() {
        return controller.isFwdLimitSwitchClosed() != 0;
    }

    @Override
    public boolean getReverseLimit() {
        return controller.isRevLimitSwitchClosed() != 0;
    }

    @Override
    public void set(double speed) {
        setPercentOutput(speed);
    }



    @Override
    public double get() {
        return controller.getMotorOutputPercent();
    }



    @Override
    public boolean getInverted() {
        return controller.getInverted();
    }



    @Override
    public void disable() {
        // TODO: add motor disable for Talon
    }



    @Override
    public void stopMotor() {
        setPercentOutput(0);
    }
}
