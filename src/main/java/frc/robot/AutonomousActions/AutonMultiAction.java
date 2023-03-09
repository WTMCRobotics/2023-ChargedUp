package frc.robot.AutonomousActions;


import frc.robot.AutonomousAction;
import frc.robot.RobotMotors;

/**
 * testestsetets
 */
public class AutonMultiAction extends AutonomousAction {
    private AutonomousAction[] allActions;
    private boolean isFirstTimeRunning = true;
    private RobotMotors motors;

    /**
     * Makes multiple actions run at the exact same time, and will only be counted as completed
     * after all queued actions are completed.
     * <p>
     * WARNING! Do NOT put in 2 or more of the same type of action, as it will more than likely run
     * into an infinite loop. Ex. don't say for the arm to move to the top position, and picking up
     * position in the same multiaction.
     */
    public AutonMultiAction(AutonomousAction... allActions) {
        this.allActions = allActions;
    }

    @Override
    public void passMotors(RobotMotors motors) {
        this.motors = motors;
    }

    @Override
    public boolean executeAndIsDone() {
        if (isFirstTimeRunning) {
            for (AutonomousAction action : allActions) {
                action.passMotors(motors);
            }
            isFirstTimeRunning = false;
            return false;
        }
        boolean isAllDone = true;
        for (AutonomousAction action : allActions) {
            if (!action.executeAndIsDone()) {
                isAllDone = false;
            }
        }
        return isAllDone;

    }

}
