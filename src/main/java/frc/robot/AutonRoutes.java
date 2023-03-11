package frc.robot;

import java.util.ArrayDeque;
import frc.robot.AutonomousActions.AutonBalance;
import frc.robot.AutonomousActions.AutonMoveArm;
import frc.robot.AutonomousActions.AutonMoveForward;
import frc.robot.AutonomousActions.AutonMoveGribber;
import frc.robot.AutonomousActions.AutonMultiAction;
import frc.robot.AutonomousActions.AutonBalance.MovementDirection;
import frc.robot.InputtedGuitarControls.ArmPosition;
import frc.robot.InputtedGuitarControls.GribberState;

public class AutonRoutes {


    public static ArrayDeque<AutonomousAction> placeObjectAndLeaveCommunity() {
        ArrayDeque<AutonomousAction> actions = new ArrayDeque<>();
        actions.add(new AutonMoveGribber(GribberState.CLOSING));
        actions.add(new AutonMoveArm(ArmPosition.PLACING_TOP));
        actions.add(new AutonMoveForward(3, 1.25));
        actions.add(new AutonMoveGribber(GribberState.OPENING));
        actions.add(new AutonMoveForward(-15, 3.5));
        return actions;
    }


    public static ArrayDeque<AutonomousAction> placeLeaveCommunityThenBalance() {
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<AutonomousAction>();
        actionQueue.add(new AutonMoveGribber(GribberState.CLOSING));
        actionQueue.add(new AutonMoveArm(ArmPosition.PLACING_TOP));
        actionQueue.add(new AutonMoveForward(3, 1));
        actionQueue.add(new AutonMoveGribber(GribberState.OPENING));
        actionQueue.add(new AutonMoveForward(-5, 3));
        actionQueue.add(new AutonMultiAction(new AutonMoveArm(ArmPosition.PICKING_UP),
                new AutonMoveForward(-10, 3)));
        actionQueue.add(new AutonBalance(MovementDirection.FORWARDS));
        return actionQueue;
    }

    public static ArrayDeque<AutonomousAction> placeThenBalance() {
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<AutonomousAction>();
        actionQueue.add(new AutonMoveGribber(GribberState.CLOSING));
        actionQueue.add(new AutonMoveArm(ArmPosition.PLACING_TOP));
        actionQueue.add(new AutonMoveForward(3, 1));
        actionQueue.add(new AutonMoveGribber(GribberState.OPENING));
        actionQueue.add(new AutonMoveForward(-5, 3));
        actionQueue.add(new AutonMultiAction(new AutonMoveArm(ArmPosition.PICKING_UP),
                new AutonBalance(MovementDirection.BACKWARDS)));
        return actionQueue;
    }

    public static ArrayDeque<AutonomousAction> leaveCommunityWhilstFacingWall() {
        ArrayDeque<AutonomousAction> actions = new ArrayDeque<>();
        actions.add(new AutonMoveForward(-12, 3));
        return actions;
    }

    public static ArrayDeque<AutonomousAction> leaveCommunityWhilstFacingEnemySide() {
        ArrayDeque<AutonomousAction> actions = new ArrayDeque<>();
        actions.add(new AutonMoveForward(12, 3));
        return actions;
    }
}
