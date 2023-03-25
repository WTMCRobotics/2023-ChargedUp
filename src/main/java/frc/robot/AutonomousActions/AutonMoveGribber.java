package frc.robot.AutonomousActions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.AutonomousAction;
import frc.robot.InputtedGuitarControls.GribberState;
import frc.robot.RobotMotors;

public class AutonMoveGribber extends AutonomousAction {
    GribberState position;
    private boolean isFirstTimeRunning;
    private RobotMotors motors;
    private double targetedTimeStamp;

    /**
     * Used to open or close the gribber on the robot
     * 
     * @param position the state to move the gribber, either OPENING, or CLOSING
     */
    public AutonMoveGribber(GribberState position, RobotMotors motors) {
        this.position = position;
        isFirstTimeRunning = true;
        this.motors = motors;
    }

    @Override
    public boolean executeAndIsDone() {
        if (isFirstTimeRunning) {
            if (position == GribberState.OPENING) {
                System.out.println("Should be opening at " + Timer.getFPGATimestamp());
                motors.getGribberMotor().set(.45);
                targetedTimeStamp = Timer.getFPGATimestamp() + 1;
            } else if (position == GribberState.CLOSING) {
                System.out.println("Should be closing at " + Timer.getFPGATimestamp());
                motors.getGribberMotor().set(-.6);
                targetedTimeStamp = Timer.getFPGATimestamp() + .8;
            } else {
                System.out.println("ruh row " + position);
            }
            isFirstTimeRunning = false;
            return false;
        }
        if (Timer.getFPGATimestamp() > targetedTimeStamp) {
            System.out.println("Girbber finsihed because " + Timer.getFPGATimestamp()
                    + " is greater than " + targetedTimeStamp);
            motors.getGribberMotor().set(0);
            return true;
        }
        return false;
    }

}
