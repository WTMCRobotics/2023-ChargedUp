package frc.robot;

import java.util.ArrayDeque;
import edu.wpi.first.math.kinematics.MecanumDriveKinematics;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class AutonMovement {
    private ArrayDeque<AutonomousAction> actionList;
    RobotMotors motors;
    Gyro gyroscope;

    // wheels are 10.75 inches away from the center along the x-axis and 10.5 inches
    // away from the
    // center along the y-axis
    // x-axis is forward and backward; y-axis is left and right
    // positive x is forward, positive y is left

    MecanumDriveKinematics kinematics = new MecanumDriveKinematics(
            Constants.FRONT_LEFT_WHEEL_LOCATION, Constants.FRONT_RIGHT_WHEEL_LOCATION,
            Constants.BACK_LEFT_WHEEL_LOCATION, Constants.BACK_RIGHT_WHEEL_LOCATION);

    /**
     * This method is indended to be called every frame in autonomousPeriodic. This method checks if
     * a certain timestamp has been reached, or if the gyroscope has reached the specified angle. If
     * it has, then stop the motors, and execute the next queued action
     */
    public void autonomousEveryFrame() {
        if (actionList.size() < 1) {
            return;
        }
        if (actionList.getFirst().executeAndIsDone()) {
            motors.stopAllMotors();
            actionList.removeFirst();
            if (actionList.size() < 1) {
                System.out.println("Auton finished!");
                return;
            }
            actionList.getFirst().passMotors(motors);


        }

    }

    public AutonMovement(RobotMotors motors, ArrayDeque<AutonomousAction> actionList) {
        this.actionList = actionList;
        this.motors = motors;
        // this.gyroscope = new AHRS(SPI.Port.kMXP);
        actionList.getFirst().passMotors(motors);

    }

    /*
     * public void AutoMoveArmPickingUp() { if (motors.getArmMotor().getEncoderPosition() <
     * degreesToEncoderPostion(Constants.ARM_PICK_UP_POSITION)) { motors.getArmMotor().set(0.25); }
     * if (motors.getArmMotor().getEncoderPosition() >
     * degreesToEncoderPostion(Constants.ARM_PICK_UP_POSITION) +
     * degreesToEncoderPostion(Constants.ARM_POSITION_BUFFER_DEGREES)) {
     * motors.getArmMotor().set(-0.25); } targetArmDegree = Constants.ARM_PICK_UP_POSITION; }
     * 
     * public void AutoMoveArmPlaceTop() { if (motors.getArmMotor().getEncoderPosition() <
     * degreesToEncoderPostion(Constants.ARM_PLACE_TOP_POSTION)) { motors.getArmMotor().set(0.25); }
     * if (motors.getArmMotor().getEncoderPosition() >
     * degreesToEncoderPostion(Constants.ARM_PLACE_TOP_POSTION) +
     * degreesToEncoderPostion(Constants.ARM_POSITION_BUFFER_DEGREES)) {
     * motors.getArmMotor().set(-0.25); } }
     * 
     * public void AutoMoveArmPlaceMiddle() { if (motors.getArmMotor().getEncoderPosition() <
     * degreesToEncoderPostion(Constants.ARM_PLACE_MIDDLE_POSTION)) {
     * motors.getArmMotor().set(0.25); } if (motors.getArmMotor().getEncoderPosition() >
     * degreesToEncoderPostion(Constants.ARM_PLACE_MIDDLE_POSTION) +
     * degreesToEncoderPostion(Constants.ARM_POSITION_BUFFER_DEGREES)) {
     * motors.getArmMotor().set(-0.25); } }
     */

}
