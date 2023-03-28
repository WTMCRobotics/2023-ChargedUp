package frc.robot;

import java.util.ArrayDeque;
import com.kauailabs.navx.frc.AHRS;
import frc.robot.AutonomousActions.AutonBalance;
import frc.robot.AutonomousActions.AutonMoveArm;
import frc.robot.AutonomousActions.AutonMoveGribber;
import frc.robot.AutonomousActions.AutonMoveInches;
import frc.robot.AutonomousActions.AutonMultiAction;
import frc.robot.AutonomousActions.AutonWait;
import frc.robot.AutonomousActions.AutonBalance.MovementDirection;
import frc.robot.AutonomousActions.AutonMoveInches.MoveInchesDirection;
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
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<>();
        actionQueue.add(new AutonMoveGribber(GribberState.CLOSING, this.motors));
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.BACKWARD, 24, this.motors));
        actionQueue.add(new AutonMultiAction(new AutonMoveArm(ArmPosition.PLACING_TOP, this.motors),
                new AutonMoveInches(MoveInchesDirection.FORWARD, 16, this.motors)));
        actionQueue.add(new AutonWait(0.5));
        actionQueue.add(new AutonMoveGribber(GribberState.OPENING, this.motors));

        actionQueue.add(new AutonMoveInches(MoveInchesDirection.BACKWARD, 72, this.motors));
        actionQueue.add(new AutonMultiAction(new AutonMoveArm(ArmPosition.PICKING_UP, this.motors),
                new AutonMoveGribber(GribberState.CLOSING, this.motors)));
        return actionQueue;
    }

    public ArrayDeque<AutonomousAction> placeObject() {
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<>();
        actionQueue.add(new AutonMoveGribber(GribberState.CLOSING, this.motors));
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.BACKWARD, 24, this.motors));
        actionQueue.add(new AutonMoveArm(ArmPosition.PLACING_TOP, this.motors));
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.FORWARD, 16, this.motors));
        actionQueue.add(new AutonMoveGribber(GribberState.OPENING, this.motors));
        return actionQueue;
    }

    public ArrayDeque<AutonomousAction> placeLeaveCommunityThenBalance(AHRS robotGyro) {
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<AutonomousAction>();
        actionQueue.add(new AutonMoveGribber(GribberState.CLOSING, this.motors));
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.BACKWARD, 24, this.motors));

        actionQueue.add(new AutonMultiAction(new AutonMoveArm(ArmPosition.PLACING_TOP, this.motors),
                new AutonMoveInches(MoveInchesDirection.FORWARD, 16, motors)));

        actionQueue.add(new AutonMoveGribber(GribberState.OPENING, this.motors));
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.BACKWARD, 12, this.motors));
        actionQueue.add(new AutonMultiAction(new AutonMoveArm(ArmPosition.FLIP_CONE, this.motors),
                new AutonMoveInches(MoveInchesDirection.BACKWARD, 60, this.motors),
                new AutonMoveGribber(GribberState.CLOSING, this.motors)));
        actionQueue.add(new AutonBalance(MovementDirection.FORWARDS, robotGyro, this.motors));
        return actionQueue;
    }

    public ArrayDeque<AutonomousAction> placeThenBalance() {
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<AutonomousAction>();
        actionQueue.add(new AutonMoveGribber(GribberState.CLOSING, this.motors));
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.BACKWARD, 24, this.motors));

        actionQueue.add(new AutonMultiAction(new AutonMoveArm(ArmPosition.PLACING_TOP, this.motors),
                new AutonMoveInches(MoveInchesDirection.FORWARD, 16, motors)));
        actionQueue.add(new AutonMoveGribber(GribberState.OPENING, this.motors));

        actionQueue.add(new AutonMoveInches(MoveInchesDirection.BACKWARD, 12, this.motors));


        actionQueue.add(new AutonMultiAction(new AutonMoveArm(ArmPosition.PICKING_UP, this.motors),
                new AutonMoveGribber(GribberState.CLOSING, this.motors),
                new AutonBalance(MovementDirection.BACKWARDS, navX, this.motors)));
        return actionQueue;
    }

    public ArrayDeque<AutonomousAction> leaveCommunityWhilstFacingWall() {
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<>();
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.BACKWARD, 72, this.motors));
        return actionQueue;
    }

    public ArrayDeque<AutonomousAction> leaveCommunityWhilstFacingEnemySide() {
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<>();
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.FORWARD, 72, this.motors));
        return actionQueue;
    }
}
