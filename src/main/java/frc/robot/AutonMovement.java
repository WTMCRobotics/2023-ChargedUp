package frc.robot;


import java.util.ArrayList;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.MecanumDriveKinematics;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class AutonMovement {
    private ArrayList<AutonAction> actionList;
    private int actionNumber;
    double targetedTimeStamp;
    RobotMotors motors;
    Gyro gyroscope;


    // wheels are 10.75 inches away from the center along the x-axis and 10.5 inches
    // away from the
    // center along the y-axis
    // x-axis is forward and backward; y-axis is left and right
    // positive x is forward, positive y is left

    MecanumDriveKinematics kinematics = new MecanumDriveKinematics(
            Constants.FRONT_LEFT_WHEEL_LOCATION, Constants.FRONT_RIGHT_WHEEL_LOCATION,
            Constants.BACK_LEFT_WHEEL_LOCATION, Constants.BACK_RIGHT_WHEEL_LOCATION);

    public void timeStamp() {
        if (Timer.getFPGATimestamp() > targetedTimeStamp) {
            motors.stopAllMotors();

        }
    }

    public AutonMovement(RobotMotors motors, ArrayList<AutonAction> actionList) {
        this.actionList = actionList;



        this.motors = motors;
        gyroscope = new AHRS(SPI.Port.kMXP);

    }

    /**
     * 
     * @param distance the amount in meters to move forward. Negative values move backwards
     * 
     * @param speed The speed in which to move
     *
     */
    public void AutoForward(double distance, double speed) {
        distance /= 2.55;
        System.out.println("Attmepting stuff");
        if (distance < 0) {
            distance *= -1;
            speed *= -1;
        }
        ChassisSpeeds chassisSpeeds = new ChassisSpeeds(speed, 0, 0);

        MecanumDriveWheelSpeeds wheelSpeeds = kinematics.toWheelSpeeds(chassisSpeeds);
        // Get the individual wheel speeds
        spinMotors(wheelSpeeds);

        System.out.println("Delaying for " + (distance / Math.abs(speed)));
        Timer.delay(distance / Math.abs(speed));

        motors.stopAllMotors();
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

        Timer.delay(distance / Math.abs(speed));

        motors.stopAllMotors();
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
        spinMotors(wheelSpeeds);

        // Timer.delay(angle / Math.abs(speed));

        // Incase the other code doesn't work:
        gyroscope.calibrate();

        // Convey negative angle to positive angle
        if (angle < 0) {
            angle = 360 - Math.abs(angle);
        }
        while (Math.toDegrees(gyroscope.getAngle()) < angle) {
            Timer.delay(0.025);
        }

        motors.stopAllMotors();

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
