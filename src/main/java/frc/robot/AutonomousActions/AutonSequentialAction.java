package frc.robot.AutonomousActions;


import java.util.ArrayDeque;
import java.util.List;
import frc.robot.AutonomousAction;


public class AutonSequentialAction extends AutonomousAction {
    private ArrayDeque<AutonomousAction> actionList;

    /**
     * Makes multiple actions run at the exact same time, and will only be counted as completed
     * after all queued actions are completed.
     * <p>
     * WARNING! Do NOT put in 2 or more of the same type of action, as it will more than likely run
     * into an infinite loop. Ex. don't say for the arm to move to the top position, and picking up
     * position in the same multiaction.
     */
    public AutonSequentialAction(AutonomousAction... allActions) {
        this.actionList = new ArrayDeque<AutonomousAction>(List.of(allActions));
    }

    @Override
    public boolean executeAndIsDone() {

        if (actionList.getFirst().executeAndIsDone()) {
            actionList.removeFirst();
        }

        if (actionList.isEmpty()) {
            return true;
        }
        return false;

    }


}
