package frc.robot;

import java.util.ArrayDeque;
import edu.wpi.first.math.kinematics.MecanumDriveKinematics;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class AutonMovement {
    private ArrayDeque<AutonomousAction> actionList;
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

    /**
     * This method is indended to be called every frame in autonomousPeriodic. This method checks if
     * a certain timestamp has been reached, or if the gyroscope has reached the specified angle. If
     * it has, then stop the motors, and execute the next queued action
     */
    public void autonomousEveryFrame() {
        if (actionList.size() < 1) {
            return;
        }
        if (actionList.getFirst().executeAndIsDone()) {
            motors.stopAllMotors();
            actionList.removeFirst();
            System.out.println("Finished an action");
            if (actionList.size() < 1) {
                System.out.println("Auton finished!");
                return;
            }

        }

    }

    public AutonMovement(RobotMotors motors, ArrayDeque<AutonomousAction> actionList) {
        this.actionList = actionList;
        this.motors = motors;

    }



}
