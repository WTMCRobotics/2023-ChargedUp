package frc.robot;

import java.util.ArrayDeque;
import com.kauailabs.navx.frc.AHRS;
import frc.robot.AutonomousActions.AutonBalance;
import frc.robot.AutonomousActions.AutonMoveArm;
import frc.robot.AutonomousActions.AutonMoveGribber;
import frc.robot.AutonomousActions.AutonMoveInches;
import frc.robot.AutonomousActions.AutonMultiAction;
import frc.robot.AutonomousActions.AutonSequentialAction;
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

    // TODO correct inches value for all auton routes//go 25 in. back
    // TODO: remove the previous todo, as it is done
    public ArrayDeque<AutonomousAction> placeObjectStrafeLeftLeaveCommunity() {
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<>();
        actionQueue.add(new AutonMoveGribber(GribberState.CLOSING, this.motors));
        actionQueue.add(new AutonMultiAction(
                new AutonMoveInches(MoveInchesDirection.BACKWARD, 25, this.motors),
                new AutonSequentialAction(new AutonWait(.5),
                        new AutonMoveArm(ArmPosition.PLACING_TOP, this.motors))));
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.FORWARD, 25, this.motors));
        actionQueue.add(new AutonWait(0.5));
        actionQueue.add(new AutonMoveGribber(GribberState.OPENING, this.motors));
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.LEFT, 6, this.motors));

        actionQueue.add(new AutonMoveInches(MoveInchesDirection.BACKWARD, 156, this.motors));
        actionQueue.add(new AutonMultiAction(new AutonMoveArm(ArmPosition.PICKING_UP, this.motors),
                new AutonMoveGribber(GribberState.CLOSING, this.motors)));
        actionQueue.add(new AutonMoveGribber(GribberState.OPENING, motors));
        return actionQueue;
    }

    public ArrayDeque<AutonomousAction> placeObjectStrafeRightLeaveCommunity() {
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<>();
        actionQueue.add(new AutonMoveGribber(GribberState.CLOSING, this.motors));
        actionQueue.add(new AutonMultiAction(
                new AutonMoveInches(MoveInchesDirection.BACKWARD, 25, this.motors),
                new AutonSequentialAction(new AutonWait(.5),
                        new AutonMoveArm(ArmPosition.PLACING_TOP, this.motors))));
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.FORWARD, 25, this.motors));
        actionQueue.add(new AutonWait(0.5));
        actionQueue.add(new AutonMoveGribber(GribberState.OPENING, this.motors));
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.RIGHT, 6, this.motors));

        actionQueue.add(new AutonMoveInches(MoveInchesDirection.BACKWARD, 156, this.motors));
        actionQueue.add(new AutonMultiAction(new AutonMoveArm(ArmPosition.PICKING_UP, this.motors),
                new AutonMoveGribber(GribberState.CLOSING, this.motors)));
        actionQueue.add(new AutonMoveGribber(GribberState.OPENING, motors));
        return actionQueue;
    }

    public ArrayDeque<AutonomousAction> placeObject() {
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<>();
        actionQueue.add(new AutonMoveGribber(GribberState.CLOSING, this.motors));
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.BACKWARD, 25, this.motors));
        actionQueue.add(new AutonMoveArm(ArmPosition.PLACING_TOP, this.motors));
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.FORWARD, 25, this.motors));
        actionQueue.add(new AutonMoveGribber(GribberState.OPENING, this.motors));
        return actionQueue;
    }

    /**
     * There is most definitly not enough time for this to finish.
     * 
     * @param robotGyro The gyroscope to use for balancing
     */
    public ArrayDeque<AutonomousAction> placeLeaveCommunityThenBalance(AHRS robotGyro) {
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<AutonomousAction>();
        actionQueue.add(new AutonMoveGribber(GribberState.CLOSING, this.motors));
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.BACKWARD, 25, this.motors));

        actionQueue.add(new AutonMultiAction(new AutonMoveArm(ArmPosition.PLACING_TOP, this.motors),
                new AutonMoveInches(MoveInchesDirection.FORWARD, 25, motors)));

        actionQueue.add(new AutonMoveGribber(GribberState.OPENING, this.motors));
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.BACKWARD, 12, this.motors));
        actionQueue.add(new AutonMultiAction(new AutonMoveArm(ArmPosition.FLIP_CONE, this.motors),
                new AutonMoveInches(MoveInchesDirection.BACKWARD, 156, this.motors),
                new AutonMoveGribber(GribberState.CLOSING, this.motors)));
        actionQueue.add(new AutonBalance(MovementDirection.FORWARDS, robotGyro, this.motors));
        return actionQueue;
    }

    /**
     * There may not be enough time for this to effectively work
     * 
     * @param robotGyro The gyroscope to use for balancing
     */
    public ArrayDeque<AutonomousAction> placeThenBalance() {
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<AutonomousAction>();
        actionQueue.add(new AutonMultiAction(new AutonMoveGribber(GribberState.CLOSING, motors),
                new AutonMoveInches(MoveInchesDirection.BACKWARD, 25, this.motors),
                new AutonSequentialAction(new AutonWait(.5),
                        new AutonMoveArm(ArmPosition.PLACING_TOP, this.motors))));
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.FORWARD, 25, motors));

        actionQueue.add(new AutonMoveGribber(GribberState.OPENING, this.motors));

        // actionQueue.add(new AutonMoveInches(MoveInchesDirection.BACKWARD, 12, this.motors));


        actionQueue.add(new AutonMultiAction(new AutonMoveArm(ArmPosition.PICKING_UP, this.motors),
                new AutonMoveGribber(GribberState.CLOSING, this.motors),
                new AutonBalance(MovementDirection.BACKWARDS, navX, this.motors)));
        return actionQueue;
    }

    public ArrayDeque<AutonomousAction> leaveCommunityWhilstFacingWall() {
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<>();
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.BACKWARD, 156, this.motors));
        return actionQueue;
    }

    public ArrayDeque<AutonomousAction> leaveCommunityWhilstFacingEnemySide() {
        ArrayDeque<AutonomousAction> actionQueue = new ArrayDeque<>();
        actionQueue.add(new AutonMoveInches(MoveInchesDirection.FORWARD, 156, this.motors));
        return actionQueue;
    }
}
