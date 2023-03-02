package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class MechanicsTest {
    private MotorController frontLeftMotor;
    private MotorController frontRightMotor;
    private MotorController backLeftMotor;
    private MotorController backRightMotor;
    private Timer countingTimer;

    public MechanicsTest(RobotMotors motors) {
        frontLeftMotor = motors.getFrontLeftMotor();
        frontRightMotor = motors.getFrontRightMotor();
        backLeftMotor = motors.getBackLeftMotor();
        backRightMotor = motors.getBackRightMotor();
        countingTimer = new Timer();

    }

    public void testMechanics() {
        countingTimer.start();
        Thread testingThread;
        // So uh, I don't actually know if this works or not.
        testingThread = new Thread(() -> {
            System.out.println(
                    "Front left wheel should spinning forwards. The top of the wheel should be going towards the front of the robot");
            frontLeftMotor.set(15);
            // This shouuuuuuuuuuuuuuuld be fine because it's on a different thread. Yea, yea,
            // definitely definitely
            Timer.delay(5);

            System.out.println(
                    "Back left wheel should spinning forwards. The top of the wheel should be going towards the front of the robot");
            frontLeftMotor.stopMotor();
            backLeftMotor.set(15);
            Timer.delay(5);

            System.out.println(
                    "Front right wheel should spinning forwards. The top of the wheel should be going towards the front of the robot");
            backLeftMotor.stopMotor();
            frontRightMotor.set(15);
            Timer.delay(5);

            System.out.println(
                    "Back right wheel should spinning forwards. The top of the wheel should be going towards the front of the robot");
            frontRightMotor.stopMotor();
            backRightMotor.set(15);
            Timer.delay(5);
            backRightMotor.stopMotor();

        });
        testingThread.setDaemon(true);
        testingThread.start();
    }
}
