package frc.robot.AutonomousActions;


import frc.robot.AutonomousAction;


public class AutonMultiAction extends AutonomousAction {
    private AutonomousAction[] allActions;

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
    public boolean executeAndIsDone() {

        boolean isAllDone = true;
        for (AutonomousAction action : allActions) {
            if (!action.executeAndIsDone()) {
                isAllDone = false;
            }
        }
        return isAllDone;

    }

}
