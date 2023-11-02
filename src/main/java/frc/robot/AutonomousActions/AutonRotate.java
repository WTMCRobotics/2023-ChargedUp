package frc.robot.AutonomousActions;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import frc.robot.AutonomousAction;
import frc.robot.Robot;
import frc.robot.RobotMotors;

public class AutonRotate extends AutonomousAction {
    private double speed;
    private boolean isFirstTimeRunning;
    private RobotMotors motors;
    private double targetTurnDegrees;
    private static Gyro gyroscope = null;

    /**
     * Rotate a specified amount of degrees at a specified speed.
     * 
     * @param degrees The amount of degrees to rotate.
     * @param speed   The speed to turn at in degrees per second. Positive turns
     *                right, negative turns
     *                left.
     */
    public AutonRotate(double degrees, double speed, RobotMotors motors) {
        this.targetTurnDegrees = degrees;
        this.speed = speed;
        this.motors = motors;
        this.isFirstTimeRunning = true;
        if (gyroscope == null) {
            // gyroscope = new AHRS(SPI.Port.kMXP);
            gyroscope = Robot.robotGyroscope;
        }

    }

    @Override
    public boolean executeAndIsDone() {
        if (isFirstTimeRunning) {
            System.out.println("CALIUBRATING GYORSPOPER: b4: +" + gyroscope.getAngle());
            gyroscope.reset();
            System.out.println("AFTER GYRO: " + gyroscope.getAngle());
            System.out.println("target turn degrees is: " + targetTurnDegrees);
            if (targetTurnDegrees > 0) {
                motors.getFrontLeftMotor().set(speed);
                motors.getBackLeftMotor().set(speed);
                motors.getFrontRightMotor().set(speed);
                motors.getBackRightMotor().set(speed);
            } else {
                motors.getFrontLeftMotor().set(-speed);
                motors.getBackLeftMotor().set(-speed);
                motors.getFrontRightMotor().set(-speed);
                motors.getBackRightMotor().set(-speed);
            }

            // Timer.delay(angle / Math.abs(speed));

            // Convert negative angle to positive angle
            // if (targetTurnDegrees < 0) {
            // targetTurnDegrees = 360 - Math.abs(targetTurnDegrees);
            // }

            isFirstTimeRunning = false;
            return false;
        }
        System.out.println(
                "compraing " + gyroscope.getAngle() + " greater than " + targetTurnDegrees);
        if (Math.abs(gyroscope.getAngle()) >= Math.abs(targetTurnDegrees)) {
            motors.stopDriveMotors();
            return true;
        }
        return false;

    }

}
