package frc.robot;

import java.util.ArrayDeque;
import com.kauailabs.navx.frc.AHRS;
import frc.robot.AutonomousActions.AutonBalance;
import frc.robot.AutonomousActions.AutonMoveArm;
import frc.robot.AutonomousActions.AutonMoveForward;
import frc.robot.AutonomousActions.AutonMoveGribber;
import frc.robot.AutonomousActions.AutonMultiAction;
import frc.robot.AutonomousActions.AutonWait;
import frc.robot.AutonomousActions.AutonBalance.MovementDirection;
import frc.robot.InputtedGuitarControls.ArmPosition;
import frc.robot.InputtedGuitarControls.GribberState;

public class AutonRoutes {

    public static ArrayDeque<AutonomousAction> placeObjectAndLeaveCommunity() {
        ArrayDeque<AutonomousAction> actions = new ArrayDeque<>();
        // actions.add(new AutonArmCalibrate(false));
        actions.add(new AutonMoveGribber(GribberState.CLOSING));
        actions.add(new AutonMoveArm(ArmPosition.PLACING_TOP));
        actions.add(new AutonMoveForward(2, 1.25));
        actions.add(new AutonWait(0.5));
        actions.add(new AutonMoveGribber(GribberState.OPENING));

        actions.add(new AutonMoveForward(-4, 2));
        actions.add(new AutonMultiAction(new AutonMoveForward(-4, 2), new AutonMoveArm(ArmPosition.PICKING_UP),
                new AutonMoveGribber(GribberState.CLOSING)));
        return actions;
    }

    public static ArrayDeque<AutonomousAction> placeLeaveCommunityThenBalance(AHRS robotGyro) {
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<AutonomousAction>();
        // actionQueue.add(new AutonArmCalibrate(false));
        actionQueue.add(new AutonMoveGribber(GribberState.CLOSING));
        actionQueue.add(new AutonMoveArm(ArmPosition.PLACING_TOP));
        actionQueue.add(new AutonMoveForward(2, 1.25));
        actionQueue.add(new AutonWait(0.5));
        actionQueue.add(new AutonMoveGribber(GribberState.OPENING));

        actionQueue.add(new AutonMoveForward(-1, 2));
        actionQueue.add(new AutonMultiAction(new AutonMoveArm(ArmPosition.FLIP_CONE),
                new AutonMoveForward(-5, 1.5), new AutonMoveGribber(GribberState.CLOSING)));
        actionQueue.add(new AutonBalance(MovementDirection.FORWARDS, robotGyro));
        return actionQueue;
    }

    public static ArrayDeque<AutonomousAction> placeThenBalance(AHRS robotGyro) {
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<AutonomousAction>();
        // actionQueue.add(new AutonArmCalibrate(false));
        actionQueue.add(new AutonMoveGribber(GribberState.CLOSING));
        actionQueue.add(new AutonMoveArm(ArmPosition.PLACING_TOP));
        actionQueue.add(new AutonMoveForward(2, 1.25));
        actionQueue.add(new AutonWait(0.5));
        actionQueue.add(new AutonMoveGribber(GribberState.OPENING));

        actionQueue.add(new AutonMoveForward(-1.5, 2));

        actionQueue.add(new AutonMultiAction(new AutonMoveArm(ArmPosition.PICKING_UP),
                new AutonMoveGribber(GribberState.CLOSING),
                new AutonBalance(MovementDirection.BACKWARDS, robotGyro)));
        return actionQueue;
    }

    public static ArrayDeque<AutonomousAction> leaveCommunityWhilstFacingWall() {
        ArrayDeque<AutonomousAction> actions = new ArrayDeque<>();
        actions.add(new AutonMoveForward(-6, 2));
        return actions;
    }

    public static ArrayDeque<AutonomousAction> leaveCommunityWhilstFacingEnemySide() {
        ArrayDeque<AutonomousAction> actions = new ArrayDeque<>();
        actions.add(new AutonMoveForward(6, 2));
        return actions;
    }
}
