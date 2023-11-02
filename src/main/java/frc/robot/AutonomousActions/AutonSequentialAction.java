package frc.robot.AutonomousActions;


import java.util.ArrayDeque;
import java.util.List;
import frc.robot.AutonomousAction;


public class AutonSequentialAction extends AutonomousAction {
    private ArrayDeque<AutonomousAction> actionList;

    /**
     * Makes multiple actions run in sequence, and will only be counted as completed after all
     * queued actions are completed.
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
