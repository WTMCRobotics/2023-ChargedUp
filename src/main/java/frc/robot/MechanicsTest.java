package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class MechanicsTest {
    private MotorController frontLeftMotor;
    private MotorController frontRightMotor;
    private MotorController backLeftMotor;
    private MotorController backRightMotor;
    private MotorController gribberMotor;
    private MotorController armMotor;

    public MechanicsTest(RobotMotors motors) {
        frontLeftMotor = motors.getFrontLeftMotor();
        frontRightMotor = motors.getFrontRightMotor();
        backLeftMotor = motors.getBackLeftMotor();
        backRightMotor = motors.getBackRightMotor();
        gribberMotor = motors.getGribberMotor();
        armMotor = motors.getArmMotor();

    }

    public void testMechanics() {
        Thread testingThread;
        // So uh, I don't actually know if this works or not.
        testingThread = new Thread(() -> {
            System.out.println(
                    "Front left wheel should spinning forwards. The top of the wheel should be going towards the front of the robot");
            spinMotorForTime(frontLeftMotor, 0.15, 5);
            System.out.println(
                    "Back left wheel should spinning forwards. The top of the wheel should be going towards the front of the robot");
            spinMotorForTime(backLeftMotor, 0.15, 5);
            System.out.println(
                    "Front right wheel should spinning forwards. The top of the wheel should be going towards the front of the robot");
            // It's negative because this motor was reversed in Robot.java
            spinMotorForTime(frontRightMotor, -0.15, 5);
            System.out.println(
                    "Back right wheel should spinning forwards. The top of the wheel should be going towards the front of the robot");
            // It's negative because this motor was reversed in Robot.java
            spinMotorForTime(backRightMotor, -0.15, 5);

            System.out.println("The Gribber should now be opening");
            spinMotorForTime(gribberMotor, 0.25, 2);
            System.out.println("The Gribber should now be closing");
            spinMotorForTime(gribberMotor, -0.25, 3);

            System.out.println("The arm should be going up");
            spinMotorForTime(armMotor, 0.25, 3);
            System.out.println("The arm should now be going down");
            spinMotorForTime(armMotor, -0.25, 3);
        });
        // Ooga booga
        testingThread.setDaemon(false);
        testingThread.start();
    }

    private void spinMotorForTime(MotorController motor, double speed, double time) {
        motor.set(speed);
        // This should be fine because it is being called on a seperate thread
        Timer.delay(time);
        motor.stopMotor();
    }
}
