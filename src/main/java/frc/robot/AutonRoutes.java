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
    private static AHRS navX;
    private RobotMotors motors;

    public AutonRoutes(AHRS navx, RobotMotors inputtedMotors) {
        motors = inputtedMotors;
        navX = navx;
    }

    public ArrayDeque<AutonomousAction> placeObjectAndLeaveCommunity() {
        ArrayDeque<AutonomousAction> actions = new ArrayDeque<>();
        // actions.add(new AutonArmCalibrate(false));
        actions.add(new AutonMoveGribber(GribberState.CLOSING, this.motors));
        actions.add(new AutonMoveArm(ArmPosition.PLACING_TOP, this.motors));
        actions.add(new AutonMoveForward(2, 1.25, this.motors));
        actions.add(new AutonWait(0.5));
        actions.add(new AutonMoveGribber(GribberState.OPENING, this.motors));

        actions.add(new AutonMoveForward(-11, 2, this.motors));
        actions.add(new AutonMultiAction(new AutonMoveArm(ArmPosition.PICKING_UP, this.motors),
                new AutonMoveGribber(GribberState.CLOSING, this.motors)));
        return actions;
    }

    public ArrayDeque<AutonomousAction> placeObject() {
        ArrayDeque<AutonomousAction> actions = new ArrayDeque<>();
        // actions.add(new AutonArmCalibrate(false));
        actions.add(new AutonMoveGribber(GribberState.CLOSING, this.motors));
        actions.add(new AutonMoveArm(ArmPosition.PLACING_TOP, this.motors));
        actions.add(new AutonMoveForward(2, 1.25, this.motors));
        actions.add(new AutonWait(0.5));
        actions.add(new AutonMoveGribber(GribberState.OPENING, this.motors));
        return actions;
    }

    public ArrayDeque<AutonomousAction> placeLeaveCommunityThenBalance(AHRS robotGyro) {
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<AutonomousAction>();
        // actionQueue.add(new AutonArmCalibrate(false));
        actionQueue.add(new AutonMoveGribber(GribberState.CLOSING, this.motors));
        actionQueue.add(new AutonMoveArm(ArmPosition.PLACING_TOP, this.motors));
        actionQueue.add(new AutonMoveForward(2, 1.25, this.motors));
        actionQueue.add(new AutonWait(0.5));
        actionQueue.add(new AutonMoveGribber(GribberState.OPENING, this.motors));

        actionQueue.add(new AutonMoveForward(-1, 2, this.motors));
        actionQueue.add(new AutonMultiAction(new AutonMoveArm(ArmPosition.FLIP_CONE, this.motors),
                new AutonMoveForward(-5, 1.5, this.motors),
                new AutonMoveGribber(GribberState.CLOSING, this.motors)));
        actionQueue.add(new AutonBalance(MovementDirection.FORWARDS, robotGyro, this.motors));
        return actionQueue;
    }

    public ArrayDeque<AutonomousAction> placeThenBalance() {
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<AutonomousAction>();
        // actionQueue.add(new AutonArmCalibrate(false));
        actionQueue.add(new AutonMoveGribber(GribberState.CLOSING, this.motors));
        actionQueue.add(new AutonMoveArm(ArmPosition.PLACING_TOP, this.motors));
        actionQueue.add(new AutonMoveForward(2, 1.25, this.motors));
        actionQueue.add(new AutonWait(0.5));
        actionQueue.add(new AutonMoveGribber(GribberState.OPENING, this.motors));

        actionQueue.add(new AutonMoveForward(-1.5, 2, this.motors));

        actionQueue.add(new AutonMultiAction(new AutonMoveArm(ArmPosition.PICKING_UP, this.motors),
                new AutonMoveGribber(GribberState.CLOSING, this.motors),
                new AutonBalance(MovementDirection.BACKWARDS, navX, this.motors)));
        return actionQueue;
    }

    public ArrayDeque<AutonomousAction> leaveCommunityWhilstFacingWall() {
        ArrayDeque<AutonomousAction> actions = new ArrayDeque<>();
        actions.add(new AutonMoveForward(-6, 2, this.motors));
        return actions;
    }

    public ArrayDeque<AutonomousAction> leaveCommunityWhilstFacingEnemySide() {
        ArrayDeque<AutonomousAction> actions = new ArrayDeque<>();
        actions.add(new AutonMoveForward(6, 2, this.motors));
        return actions;
    }
}
