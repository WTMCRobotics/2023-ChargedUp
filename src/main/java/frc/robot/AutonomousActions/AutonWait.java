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
