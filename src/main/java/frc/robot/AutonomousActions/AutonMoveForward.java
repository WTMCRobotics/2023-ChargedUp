package frc.robot.AutonomousActions;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import frc.robot.AutonomousAction;
import frc.robot.RobotMotors;

public class AutonMoveForward extends AutonomousAction {
    private double speed;
    private boolean isFirstTimeRunning;
    private RobotMotors motors;
    private double targetDistance;
    protected AHRS navX;

    /**
     * Moves a specified distance at a specified speed.
     * 
     * @param distance The distance to move, measure in feet. Positive is forward,
     *                 negative is
     *                 backwards.
     * @param speed    The speed to move at, in feet per second.
     */
    public AutonMoveForward(double distance, double speed, AHRS navX) {
        this.targetDistance = distance;
        this.speed = speed;
        this.isFirstTimeRunning = true;
        this.navX = navX;
    }

    @Override
    public void passMotors(RobotMotors motors) {
        this.motors = motors;
    }

    @Override
    public boolean executeAndIsDone() {
        if (isFirstTimeRunning) {
            navX.resetDisplacement();

            if (targetDistance < 0) {
                targetDistance *= -1;
                speed *= -1;
            }
            // Convert from f/s to m/s
            speed /= 3.281;
            ChassisSpeeds chassisSpeeds = new ChassisSpeeds(speed, 0, 0);

            MecanumDriveWheelSpeeds wheelSpeeds = kinematics.toWheelSpeeds(chassisSpeeds);

            spinMotors(wheelSpeeds, motors, false);
            // Get the individual wheel speeds
            isFirstTimeRunning = false;
            return false;
        }

        if (Math.abs(getFeetTraveledForward()) >= Math.abs(targetDistance)) {
            System.out.println("The robot has moved enough, as " + getFeetTraveledForward()
                    + " is greater than " + targetDistance);
            motors.stopDriveMotors();
            return true;
        }
        return false;

    }

    private double getFeetTraveledForward() {
        return navX.getDisplacementY() / 3.281;
    }

}
