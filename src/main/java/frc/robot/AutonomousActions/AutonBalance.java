package frc.robot.AutonomousActions;

import java.lang.constant.DirectMethodHandleDesc;
import com.kauailabs.navx.frc.AHRS;
import frc.robot.AutonomousAction;
import frc.robot.Constants;
import frc.robot.Gains;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMotors;

public class AutonBalance extends AutonomousAction {
    MovementDirection movementDirection;
    private boolean isFirstTimeRunning;
    private RobotMotors motors;
    private AHRS gyro;
    private boolean wasUnbalanced = false;
    private double currentDebounceTime = 0;
    private double currentBalanceDebounceTime = 0;
    private ProfiledPIDController balancePID;

    /**
     * Used to automatically balance the robot on the charging station, within a certain degree.
     * 
     * @param position Whether to start the robot moving forwards, or backwards
     */
    public AutonBalance(MovementDirection movementDirection, AHRS gyro, RobotMotors motors) {
        System.out.println("Balnce thingy");
        this.gyro = gyro;
        this.movementDirection = movementDirection;
        this.isFirstTimeRunning = true;
        this.wasUnbalanced = false;
        this.motors = motors;
        // Max acceleration is not set in constants. set here
        final Constraints balanceConstraints =
                new Constraints(Constants.BALANCING_GAINS.PEAK_OUTPUT, .75);
        balancePID = new ProfiledPIDController(Constants.BALANCING_GAINS.P,
                Constants.BALANCING_GAINS.I, Constants.BALANCING_GAINS.D, balanceConstraints);
        // balancePID.enableContinuousInput(currentDebounceTime, currentBalanceDebounceTime);
    }

    // Bottom slab = 11 & 12 degrees
    // Bigger panel = 15 degrees

    // FRONT SIDE OF ROBOT UPWARDS = Roll goes positive

    @Override
    public boolean executeAndIsDone() {

        if (!wasUnbalanced && !this.isProbablyBalanced()) {
            System.out.println("We unbalanced");
            wasUnbalanced = true;
        }

        if (wasUnbalanced) {
            System.out.println("was unbalance PID LOOP");
            double calculatedValue = balancePID.calculate(gyro.getRoll(), 0);
            System.out.println("PID Calculated Value! " + calculatedValue);
            if (movementDirection == MovementDirection.BACKWARDS) {
                // this.spinMotorsAtSpeed(-calculatedValue);
                setVelocity(-Constants.BALANCING_MAX_RPM * calculatedValue);
            } else {
                // this.spinMotorsAtSpeed(calculatedValue);
                setVelocity(Constants.BALANCING_MAX_RPM * calculatedValue);
            }
        } else {
            if (movementDirection == MovementDirection.BACKWARDS) {
                this.spinMotorsAtSpeed(
                        -Constants.ROBOT_SPEED_WHILE_BALANCING_BEFORE_CHARGE_STATION);
            } else {
                spinMotorsAtSpeed(Constants.ROBOT_SPEED_WHILE_BALANCING_BEFORE_CHARGE_STATION);
            }
        }

        /*
         * if (isFirstTimeRunning) {
         * 
         * if (movementDirection == MovementDirection.FORWARDS) {
         * spinMotorsAtSpeed(Constants.ROBOT_SPEED_WHILE_BALANCING_BEFORE_CHARGE_STATION); } else {
         * spinMotorsAtSpeed(-Constants.ROBOT_SPEED_WHILE_BALANCING_BEFORE_CHARGE_STATION); }
         * 
         * isFirstTimeRunning = false; return false; }
         * 
         * if (!this.isProbablyBalanced() && !wasUnbalanced) { currentDebounceTime += .02; if
         * (currentDebounceTime >= Constants.BEING_UNBALANCED_DEBOUNCE_TIME) {
         * System.out.println("It is not balanced rn!"); if (movementDirection ==
         * MovementDirection.FORWARDS) {
         * spinMotorsAtSpeed(Constants.ROBOT_SPEED_WHILE_BALANCING_ON_CHARGE_STATION); } else {
         * spinMotorsAtSpeed(-Constants.ROBOT_SPEED_WHILE_BALANCING_ON_CHARGE_STATION); }
         * wasUnbalanced = true; return false; }
         * 
         * } else { currentDebounceTime = 0; }
         * 
         * if (wasUnbalanced) { if (gyro.getRoll() >
         * Constants.BALANCING_MARGAIN_OF_ERROR_ON_STATION) {
         * spinMotorsAtSpeed(Constants.ROBOT_SPEED_WHILE_BALANCING_ON_CHARGE_STATION); } else if
         * (gyro.getRoll() < -Constants.BALANCING_MARGAIN_OF_ERROR_ON_STATION) {
         * spinMotorsAtSpeed(-Constants.ROBOT_SPEED_WHILE_BALANCING_ON_CHARGE_STATION); } else {
         * spinMotorsAtSpeed(0.0); }
         * 
         * if (isProbablyBalanced()) { currentBalanceDebounceTime += .02; if
         * (currentBalanceDebounceTime > Constants.BALANCING_DEBOUNCE_TIME) {
         * System.out.println("Yea it all balanced"); motors.stopDriveMotors(); return true; } }
         * else { currentBalanceDebounceTime = 0; } }
         */

        return false;
    }


    private void spinMotorsAtSpeed(double speed) {
        motors.getFrontLeftMotor().set(speed);
        motors.getFrontRightMotor().set(speed);
        motors.getBackLeftMotor().set(speed);
        motors.getBackRightMotor().set(speed);
        // MecanumDriveWheelSpeeds wheelSpeeds =
        // new MecanumDriveWheelSpeeds(speed, speed, speed, speed);
        // spinMotors(wheelSpeeds, motors, false);
    }

    private void setVelocity(double rpm) {
        motors.getFrontLeftMotor().setVelocity(rpm);
        motors.getFrontRightMotor().setVelocity(rpm);
        motors.getBackLeftMotor().setVelocity(rpm);
        motors.getBackRightMotor().setVelocity(rpm);
    }

    /**
     * Returns whether the robot is currently balanced or not. Is it accurate? Maybe.
     * 
     * @return Whether or not the robot is balanced or not (probably)
     */
    private boolean isProbablyBalanced() {
        // If the gyro reading is greater than negative half the buffer, and less than
        // positive half
        // the buffer, then it's maybe balanced!
        // System.out.println(
        // "The robot is balanced, because " + (-halfOfMargianOfError) + " is less than
        // or equal to " + gyroRoll);
        boolean isProbablyBalanced = gyro.getRoll() >= -Constants.BALANCING_MARGIN_OF_ERROR
                && gyro.getRoll() <= Constants.BALANCING_MARGIN_OF_ERROR;
        if (!isProbablyBalanced) {
            System.out.println("The robot is unbalance, as " + gyro.getRoll()
                    + " is mor ethan -3, and less and than 3 ");
        }
        return isProbablyBalanced;
    }

    public enum MovementDirection {
        FORWARDS, BACKWARDS
    }

}
