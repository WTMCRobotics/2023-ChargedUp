package frc.robot;

import java.util.ArrayDeque;
import frc.robot.AutonomousActions.AutonMoveArm;
import frc.robot.AutonomousActions.AutonMoveGribber;
import frc.robot.AutonomousActions.AutonMoveInches;
import frc.robot.AutonomousActions.AutonRotate;
import frc.robot.AutonomousActions.AutonMoveInches.MoveInchesDirection;
import frc.robot.InputtedGuitarControls.ArmPosition;
import frc.robot.InputtedGuitarControls.GribberState;

public class ScriptAuton {

    static ArrayDeque<AutonomousAction> scriptActions = new ArrayDeque<>();
    static RobotMotors motors;

    public static ArrayDeque<AutonomousAction> getScriptActions(RobotMotors motors) {
        ScriptAuton.motors = motors;
        scriptActions = new ArrayDeque<>();

        // Available Actions:
        // moveForward(inches) - Moves forward the specified inches
        // moveBackward(inches*) - Moves backward the specified inches
        // turnLeft(degrees) - Turns left an amount of degrees
        // turnRight(degrees) - Turns right an amount of degrees
        // raiseArm() - Raises the arm
        // lowerArm() - Lowers the arm
        // openGribber() - Opens the claw
        // closeGribber() - Closes the claw
        // ffffffffffffffffcccffgghjnbvccxzertgvvfjuiotktfimguu; -- mark
        {
            moveForward(12);
            turnLeft(90);
            // turnRight(180);
            // closeGribber();
            // raiseArm();
            // openGribber();
        }

        return scriptActions;
    }

    static void moveForward(int amount) {
        scriptActions.add(new AutonMoveInches(MoveInchesDirection.FORWARD, (amount - 0.5 - (3.0 / 16.0)), motors));
    }

    static void moveBackward(int amount) {
        scriptActions.add(new AutonMoveInches(MoveInchesDirection.BACKWARD, (amount - 0.5 - (3.0 / 16.0)), motors));
    }

    static void turnRight(int degrees) {
        scriptActions.add(new AutonRotate(degrees - 5, 0.6, motors));
    }

    static void turnLeft(int degrees) {
        scriptActions.add(new AutonRotate(-degrees + 5, 0.6, motors));
    }

    static void raiseArm() {
        scriptActions.add(new AutonMoveArm(ArmPosition.PLACING_MIDDLE, motors));
    }

    static void lowerArm() {
        scriptActions.add(new AutonMoveArm(ArmPosition.INTAKE, motors));
    }

    static void openGribber() {
        scriptActions.add(new AutonMoveGribber(GribberState.OPENING, motors));
    }

    static void closeGribber() {
        scriptActions.add(new AutonMoveGribber(GribberState.CLOSING, motors));
    }

}
