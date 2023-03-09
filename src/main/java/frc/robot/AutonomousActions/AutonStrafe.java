package frc.robot.AutonomousActions;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.AutonomousAction;
import frc.robot.RobotMotors;

public class AutonStrafe extends AutonomousAction {

    boolean isFirstTimeRunning;
    private double speed;
    private double distance;
    private double targetedTimeStamp;
    private RobotMotors motors;


    /**
     * Strafes a specified distance at a specified speed.
     * <p>
     * WARNING! This just doesn't really work, it will kinda move, not at the correct speed, and not
     * very well either. Maybe just rotate and move instead!
     * 
     * @param distance The distance to strafe, measure in feet.
     * @param speed The speed to move at, in feet per second.
     */
    public AutonStrafe(double distance, double speed) {
        this.distance = distance;
        this.speed = speed;
        this.isFirstTimeRunning = true;
    }

    public boolean executeAndIsDone() {
        if (isFirstTimeRunning) {
            distance *= 2;
            if (distance < 0) {
                distance *= -1;
                speed *= -1;
            }
            // Convert from f/s to m/s
            speed /= 3.281;

            ChassisSpeeds chassisSpeeds = new ChassisSpeeds(0, speed, 0);

            MecanumDriveWheelSpeeds wheelSpeeds = kinematics.toWheelSpeeds(chassisSpeeds);
            // Get the individual wheel speeds
            spinMotors(wheelSpeeds, motors);

            // Timer.delay(distance / Math.abs(speed));
            targetedTimeStamp = distance / Math.abs(speed) + Timer.getFPGATimestamp();
            isFirstTimeRunning = false;
            return false;
        }
        if (Timer.getFPGATimestamp() > targetedTimeStamp) {
            return true;
        }
        return false;

    }

    @Override
    public void passMotors(RobotMotors motors) {
        this.motors = motors;
    }

}
