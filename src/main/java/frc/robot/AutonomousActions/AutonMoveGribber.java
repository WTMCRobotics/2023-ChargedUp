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

    public AutonMoveGribber(GribberState position) {
        this.position = position;
    }

    @Override
    public boolean executeAndIsDone() {
        if (isFirstTimeRunning) {
            if (position == GribberState.OPENING) {
                motors.getGribberMotor().set(.25);
                targetedTimeStamp = Timer.getFPGATimestamp() + 2;
            } else if (position == GribberState.OPENING) {
                motors.getGribberMotor().set(-.25);
                targetedTimeStamp = Timer.getFPGATimestamp() + 2;
            }
            isFirstTimeRunning = false;
            return false;
        }
        if (Timer.getFPGATimestamp() > targetedTimeStamp) {
            return true;
        }
        return false;
    }

    @Override
    public void passMotors(RobotMotors motors) {
        this.motors = motors;
    }

}
