import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import frc.robot.Constants;
import frc.robot.InputtedControls;
import frc.robot.Robot;


public class DriveTrainTest {

    Robot robot = new Robot();

    @Nested
    class ForwardTest {
        double joystickY;
        double joystickX;
        double turnAmount;
        double scaleDownFactor;
        boolean slowMode;
        InputtedControls controls;

        @BeforeEach
        void setup() {
            joystickY = 1;
            joystickX = 0;
            turnAmount = 0;
            scaleDownFactor = 1;
            slowMode = false;
            controls = new InputtedControls(joystickX, joystickY, turnAmount);
        }

        @Test
        void getFrontLeftMotorPowerTest() {
            double frontLeftMotorPower = robot.getMotorPower(controls, scaleDownFactor, slowMode,
                    Constants.FRONT_RIGHT_MOTOR_ID);

            assertEquals(1, frontLeftMotorPower);
        }

        @Test
        void getFrontRightMotorPowerTest() {
            double frontRightMotorPower = robot.getMotorPower(controls, scaleDownFactor, slowMode,
                    Constants.FRONT_RIGHT_MOTOR_ID);

            assertEquals(1, frontRightMotorPower);
        }

        @Test
        void getBackLeftMotorPowerTest() {
            double backLeftMotorPower = robot.getMotorPower(controls, scaleDownFactor, slowMode,
                    Constants.BACK_LEFT_MOTOR_ID);

            assertEquals(1, backLeftMotorPower);
        }

        @Test
        void getBackRightMotorPowerTest() {
            double backRightMotorPower = robot.getMotorPower(controls, scaleDownFactor, slowMode,
                    Constants.BACK_RIGHT_MOTOR_ID);

            assertEquals(1, backRightMotorPower);
        }
    }

    @Nested
    class BackwardTest {
        double joystickY;
        double joystickX;
        double turnAmount;
        double scaleDownFactor;
        boolean slowMode;
        InputtedControls controls;

        @BeforeEach
        void setup() {
            joystickY = -1;
            joystickX = 0;
            turnAmount = 0;
            scaleDownFactor = 1;
            slowMode = false;
            controls = new InputtedControls(joystickX, joystickY, turnAmount);
        }

        @Test
        void getFrontLeftMotorPowerTest() {
            double frontLeftMotorPower = robot.getMotorPower(controls, scaleDownFactor, slowMode,
                    Constants.FRONT_LEFT_MOTOR_ID);

            assertEquals(-1, frontLeftMotorPower);
        }

        @Test
        void getFrontRightMotorPowerTest() {
            double frontRightMotorPower = robot.getMotorPower(controls, scaleDownFactor, slowMode,
                    Constants.FRONT_RIGHT_MOTOR_ID);

            assertEquals(-1, frontRightMotorPower);
        }

        @Test
        void getBackLeftMotorPowerTest() {
            double backLeftMotorPower = robot.getMotorPower(controls, scaleDownFactor, slowMode,
                    Constants.BACK_LEFT_MOTOR_ID);

            assertEquals(-1, backLeftMotorPower);
        }

        @Test
        void getBackRightMotorPowerTest() {
            double backRightMotorPower = robot.getMotorPower(controls, scaleDownFactor, slowMode,
                    Constants.BACK_RIGHT_MOTOR_ID);

            assertEquals(-1, backRightMotorPower);
        }
    }

    @Nested
    class LeftTest {
        double joystickY;
        double joystickX;
        double turnAmount;
        double scaleDownFactor;
        boolean slowMode;
        InputtedControls controls;

        @BeforeEach
        void setup() {
            joystickY = 0;
            joystickX = -1;
            turnAmount = 0;
            scaleDownFactor = 1;
            slowMode = false;
            controls = new InputtedControls(joystickX, joystickY, turnAmount);
        }

        @Test
        void getFrontLeftMotorPowerTest() {
            double frontLeftMotorPower = robot.getMotorPower(controls, scaleDownFactor, slowMode,
                    Constants.FRONT_LEFT_MOTOR_ID);

            assertEquals(-1, frontLeftMotorPower);
        }

        @Test
        void getFrontRightMotorPowerTest() {
            double frontRightMotorPower = robot.getMotorPower(controls, scaleDownFactor, slowMode,
                    Constants.FRONT_RIGHT_MOTOR_ID);

            assertEquals(1, frontRightMotorPower);
        }

        @Test
        void getBackLeftMotorPowerTest() {
            double backLeftMotorPower = robot.getMotorPower(controls, scaleDownFactor, slowMode,
                    Constants.BACK_LEFT_MOTOR_ID);

            assertEquals(1, backLeftMotorPower);
        }

        @Test
        void getBackRightMotorPowerTest() {
            double backRightMotorPower = robot.getMotorPower(controls, scaleDownFactor, slowMode,
                    Constants.BACK_RIGHT_MOTOR_ID);

            assertEquals(-1, backRightMotorPower);
        }
    }

    @Nested
    class RightTest {
        double joystickY;
        double joystickX;
        double turnAmount;
        double scaleDownFactor;
        boolean slowMode;
        InputtedControls controls;

        @BeforeEach
        void setup() {
            joystickY = 0;
            joystickX = 1;
            turnAmount = 0;
            scaleDownFactor = 1;
            slowMode = false;
            controls = new InputtedControls(joystickX, joystickY, turnAmount);
        }

        @Test
        void getFrontLeftMotorPowerTest() {
            double frontLeftMotorPower = robot.getMotorPower(controls, scaleDownFactor, slowMode,
                    Constants.FRONT_LEFT_MOTOR_ID);

            assertEquals(1, frontLeftMotorPower);
        }

        @Test
        void getFrontRightMotorPowerTest() {
            double frontRightMotorPower = robot.getMotorPower(controls, scaleDownFactor, slowMode,
                    Constants.FRONT_RIGHT_MOTOR_ID);

            assertEquals(-1, frontRightMotorPower);
        }

        @Test
        void getBackLeftMotorPowerTest() {
            double backLeftMotorPower = robot.getMotorPower(controls, scaleDownFactor, slowMode,
                    Constants.BACK_LEFT_MOTOR_ID);

            assertEquals(-1, backLeftMotorPower);
        }

        @Test
        void getBackRightMotorPowerTest() {
            double backRightMotorPower = robot.getMotorPower(controls, scaleDownFactor, slowMode,
                    Constants.BACK_RIGHT_MOTOR_ID);

            assertEquals(1, backRightMotorPower);
        }
    }
    @Nested
    class deadZoneTests {
        double joyStickX;
        double joyStickY;
        double turnAmount;
        double JOYSTICK_DEADZONE;

        @BeforeEach
        void setup() {
            JOYSTICK_DEADZONE = .02;
            joyStickX = 0.015;
            joyStickY = -.0125;
            turnAmount = 1;
        }

        @Test
        void joyStickXTest() {
            assertEquals(robot.setToZeroIfInDeadzone(joyStickX, JOYSTICK_DEADZONE), 0);
        }

        @Test
        void joyStickYTest() {
            assertEquals(robot.setToZeroIfInDeadzone(joyStickY, JOYSTICK_DEADZONE), 0);
        }

        @Test
        void turnAmountTest() {
            assertEquals(robot.setToZeroIfInDeadzone(turnAmount, JOYSTICK_DEADZONE), turnAmount);
        }

    }

    @Nested
    class MiscDrivetrainTests {
        boolean slowMode;
        double scaleFactor;

        @BeforeEach
        void setup() {
            slowMode = true;
            scaleFactor = 1.0;
        }

        @Test
        void slowModeTest() {
            assertEquals(4.0, robot.applySlowMode(scaleFactor, slowMode));
        }
    }
}
