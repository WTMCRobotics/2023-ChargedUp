package frc.robot.AutonomousActions;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import frc.robot.AutonomousAction;
import frc.robot.RobotMotors;

/**
 * testestsetets
 */
public class AutonRotate extends AutonomousAction {
    private double speed;
    private boolean isFirstTimeRunning;
    private RobotMotors motors;
    private double targetTurnDegrees;
    private static Gyro gyroscope = null;

    public AutonRotate(double degrees, double speed) {
        this.targetTurnDegrees = degrees;
        this.speed = speed;
        this.isFirstTimeRunning = true;
        if (gyroscope == null) {
            // Despite this warning, I think that this will help performance, as I think
            // declaring a new gyroscope everytime may nto be the the brst
            gyroscope = new AHRS(SPI.Port.kMXP);
        }

    }

    @Override
    public void passMotors(RobotMotors motors) {
        this.motors = motors;
    }

    @Override
    public boolean executeAndIsDone() {
        if (isFirstTimeRunning) {
            ChassisSpeeds chassisSpeeds = new ChassisSpeeds(0, 0, speed);
            MecanumDriveWheelSpeeds wheelSpeeds = kinematics.toWheelSpeeds(chassisSpeeds);
            spinMotors(wheelSpeeds, motors);

            // Timer.delay(angle / Math.abs(speed));

            gyroscope.calibrate();

            // Convert negative angle to positive angle
            if (targetTurnDegrees < 0) {
                targetTurnDegrees = 360 - Math.abs(targetTurnDegrees);
            }

            isFirstTimeRunning = false;
            return false;
        }

        if (Math.toDegrees(gyroscope.getAngle()) >= targetTurnDegrees) {
            motors.stopAllMotors();
            return true;
        }
        return false;

    }

}