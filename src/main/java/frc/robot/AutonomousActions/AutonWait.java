package frc.robot.AutonomousActions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.AutonomousAction;
import frc.robot.RobotMotors;

/**
 * testestsetets
 */
public class AutonWait extends AutonomousAction {
    private boolean isFirstTimeRunning;
    private double targetedTime;
    private double delay;

    /**
     * Used to make the robot wait for a specified number of seconds. The thread will not be harmed
     * 
     * @param secondsToDelay the seconds number of seconds to delay to make the robot wait, in
     *        seconds (timed in seconds)
     */
    public AutonWait(double secondsToDelay) {
        delay = secondsToDelay;
        this.isFirstTimeRunning = true;
    }

    @Override
    public void passMotors(RobotMotors motors) {
        // I have brain damage
    }

    @Override
    public boolean executeAndIsDone() {
        if (isFirstTimeRunning) {
            targetedTime = Timer.getFPGATimestamp() + delay;
            isFirstTimeRunning = false;
            return false;
        }

        if (Timer.getFPGATimestamp() >= targetedTime) {
            return true;
        }
        return false;

    }

}
