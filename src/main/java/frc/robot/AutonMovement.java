package frc.robot;


import java.util.ArrayList;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.MecanumDriveKinematics;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class AutonMovement {
    private ArrayList<AutonAction> actionList;
    private int actionNumber;
    double targetedTimeStamp;
    double targetDistance;
    double targetTurnDegree;
    RobotMotors motors;
    Gyro gyroscope;
    Encoder encoder;


    // wheels are 10.75 inches away from the center along the x-axis and 10.5 inches
    // away from the
    // center along the y-axis
    // x-axis is forward and backward; y-axis is left and right
    // positive x is forward, positive y is left

    MecanumDriveKinematics kinematics = new MecanumDriveKinematics(
            Constants.FRONT_LEFT_WHEEL_LOCATION, Constants.FRONT_RIGHT_WHEEL_LOCATION,
            Constants.BACK_LEFT_WHEEL_LOCATION, Constants.BACK_RIGHT_WHEEL_LOCATION);


    /**
     * This method is indended to be called every frame in autonomousPeriodic. This method checks if
     * a certain timestamp has been reached, or if the gyroscope has reached the specified angle. If
     * it has, then stop the motors, and execute the next queued action
     */
    public void autonomousEveryFrame() {
        // If targetedTimeStamp is set to -1, that means we don't care about the time, and thus we
        // won't check for it , same goes for turn degree
        if (targetDistance != -1 && Math.abs(encoder.getDistance()) >= Math.abs(targetDistance)) {
            motors.stopAllMotors();
            targetDistance = -1;

            if (actionList.size() < actionNumber) {
                return;
            }

            this.executeAction(actionList.get(actionNumber));
            actionNumber++;
        }

        if (targetedTimeStamp != -1 && Timer.getFPGATimestamp() > targetedTimeStamp) {
            motors.stopAllMotors();
            targetedTimeStamp = -1;

            if (actionList.size() < actionNumber) {
                return;
            }

            this.executeAction(actionList.get(actionNumber));
            actionNumber++;
        }

        if (targetTurnDegree != -1 && Math.toDegrees(gyroscope.getAngle()) >= targetTurnDegree) {
            motors.stopAllMotors();
            targetTurnDegree = -1;

            if (actionList.size() < actionNumber) {
                return;
            }

            this.executeAction(actionList.get(actionNumber));
            actionNumber++;
        }
    }

    public AutonMovement(RobotMotors motors, ArrayList<AutonAction> actionList) {

        this.actionList = actionList;


        this.actionNumber = 1;
        this.motors = motors;
        this.targetedTimeStamp = -1;
        this.targetTurnDegree = -1;
        this.targetDistance = -1;
        this.gyroscope = new AHRS(SPI.Port.kMXP);

        encoder = new Encoder(0, 1, false, Encoder.EncodingType.k2X);
        encoder.setDistancePerPulse((8.0 * Math.PI / 12.0) / 2048.0);
        encoder.setMinRate(0.5 / 12.0);
        encoder.setSamplesToAverage(5);



        if (actionList.size() > 0) {
            this.executeAction(actionList.get(0));

        }


    }

    protected void executeAction(AutonAction action) {
        double amount = action.amount;
        double speed = action.speed;
        switch (action.movmentType) {
            case BACKWARD:
                this.AutoForward(-amount, speed);
                break;
            case FORWARD:
                this.AutoForward(amount, speed);
                break;
            case LEFT:
                this.AutoStrafe(amount, speed);
                break;
            case RIGHT:
                this.AutoStrafe(-amount, speed);
                break;
            case TURN_LEFT:
                this.AutoTurn(-amount, speed);
                break;
            case TURN_RIGHT:
                this.AutoTurn(amount, speed);
                break;
            default:
                break;

        }
    }

    /**
     * 
     * @param distance the amount in meters to move forward. Negative values move backwards
     * 
     * @param speed The speed in which to move
     *
     */
    public void AutoForward(double distance, double speed) {
        // distance /= 2.55;
        System.out.println("Attempting to move forward");
        if (distance < 0) {
            distance *= -1;
            speed *= -1;
        }
        ChassisSpeeds chassisSpeeds = new ChassisSpeeds(speed, 0, 0);

        MecanumDriveWheelSpeeds wheelSpeeds = kinematics.toWheelSpeeds(chassisSpeeds);
        // Get the individual wheel speeds
        spinMotors(wheelSpeeds);

        // System.out.println("Delaying for " + (distance / Math.abs(speed)));
        // Timer.delay(distance / Math.abs(speed));
        // targetedTimeStamp = distance / Math.abs(speed) + Timer.getFPGATimestamp();
        encoder.reset();
        targetDistance = distance;

    }


    public void AutoStrafe(double distance, double speed) {
        distance *= 2;
        if (distance < 0) {
            distance *= -1;
            speed *= -1;
        }
        ChassisSpeeds chassisSpeeds = new ChassisSpeeds(0, speed, 0);

        MecanumDriveWheelSpeeds wheelSpeeds = kinematics.toWheelSpeeds(chassisSpeeds);
        // Get the individual wheel speeds
        spinMotors(wheelSpeeds);

        // Timer.delay(distance / Math.abs(speed));
        targetedTimeStamp = distance / Math.abs(speed) + Timer.getFPGATimestamp();

    }

    public void AutoTurn(double angle, double speed) {
        // drive.driveCartesian(0, 0, speed); probably don't need this

        // ChassisSpeeds arguments are:
        // chassis meters per second on the x-axis, positive is forward
        // chassis meters per second on the y-axis, positive is left
        // angular velocity of the robot in radians per second, positive is
        // counterclockwise


        // originallilay 1.0, 3.0, 1
        ChassisSpeeds chassisSpeeds = new ChassisSpeeds(0, 0, speed);

        // Convert to wheel speeds
        MecanumDriveWheelSpeeds wheelSpeeds = kinematics.toWheelSpeeds(chassisSpeeds);
        // Get the individual wheel speeds

        // Spin the motors until stopped
        spinMotors(wheelSpeeds);

        // Timer.delay(angle / Math.abs(speed));

        gyroscope.calibrate();

        // Convert negative angle to positive angle
        if (angle < 0) {
            angle = 360 - Math.abs(angle);
        }
        targetTurnDegree = angle;

    }

    private void spinMotors(MecanumDriveWheelSpeeds wheelSpeeds) {

        double frontLefSpeed = wheelSpeeds.frontLeftMetersPerSecond;
        double frontRightSpeed = wheelSpeeds.frontRightMetersPerSecond;
        double backLeftSpeed = wheelSpeeds.rearLeftMetersPerSecond;
        double backRightSpeed = wheelSpeeds.rearRightMetersPerSecond;

        motors.getFrontLeftMotor().set(frontLefSpeed);
        motors.getFrontRightMotor().set(frontRightSpeed);
        motors.getBackLeftMotor().set(backLeftSpeed);
        motors.getBackRightMotor().set(backRightSpeed);
    }



}
