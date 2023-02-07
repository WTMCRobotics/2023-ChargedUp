import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import frc.robot.Constants;
import frc.robot.Robot;

import java.beans.Transient;

public class DriveTrainTest {

    Robot robot = new Robot();

    @Nested
    class ForwardTest {
        double joystickY;
        double joystickX;
        double turnAmount;
        double scaleDownFactor;
        boolean slowMode;

        @BeforeEach
        void setup() {
            joystickY = 1;
            joystickX = 0;
            turnAmount = 0;
            scaleDownFactor = 1;
            slowMode = false;
        }

        @Test
        void getFrontLeftMotorPowerTest() {
            double frontLeftMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor, slowMode, Constants.FRONT_RIGHT_MOTOR_ID);

            assertEquals(1, frontLeftMotorPower);
        }
        
        @Test
        void getFrontRightMotorPowerTest(){
            double frontRightMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor, slowMode, Constants.FRONT_RIGHT_MOTOR_ID);

            assertEquals(1, frontRightMotorPower);
        }
        
        @Test
        void getBackLeftMotorPowerTest(){
            double backLeftMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor, slowMode, Constants.BACK_LEFT_MOTOR_ID);
            
            assertEquals(1, backLeftMotorPower);
        }

        @Test
        void getBackRightMotorPowerTest(){
            double backRightMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor, slowMode, Constants.BACK_RIGHT_MOTOR_ID);
            
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

        @BeforeEach
        void setup() {
            joystickY = -1;
            joystickX = 0;
            turnAmount = 0;
            scaleDownFactor = 1;
            slowMode = false;
        }

        @Test
        void getFrontLeftMotorPowerTest() {
            double frontLeftMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor, slowMode, Constants.FRONT_LEFT_MOTOR_ID);

            assertEquals(-1, frontLeftMotorPower);
        }
        
        @Test
        void getFrontRightMotorPowerTest(){
            double frontRightMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor, slowMode, Constants.FRONT_RIGHT_MOTOR_ID);

            assertEquals(-1, frontRightMotorPower);
        }
        
        @Test
        void getBackLeftMotorPowerTest(){
            double backLeftMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor, slowMode, Constants.BACK_LEFT_MOTOR_ID);
            
            assertEquals(-1, backLeftMotorPower);
        }

        @Test
        void getBackRightMotorPowerTest(){
            double backRightMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor, slowMode, Constants.BACK_RIGHT_MOTOR_ID);
            
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

        @BeforeEach
        void setup() {
            joystickY = 0;
            joystickX = -1;
            turnAmount = 0;
            scaleDownFactor = 1;
            slowMode = false;
        }

        @Test
        void getFrontLeftMotorPowerTest() {
            double frontLeftMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor, slowMode, Constants.FRONT_LEFT_MOTOR_ID);

            assertEquals(-1, frontLeftMotorPower);
        }
        
        @Test
        void getFrontRightMotorPowerTest(){
            double frontRightMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor, slowMode, Constants.FRONT_RIGHT_MOTOR_ID);

            assertEquals(1, frontRightMotorPower);
        }
        
        @Test
        void getBackLeftMotorPowerTest(){
            double backLeftMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor, slowMode, Constants.BACK_LEFT_MOTOR_ID);
            
            assertEquals(1, backLeftMotorPower);
        }

        @Test
        void getBackRightMotorPowerTest(){
            double backRightMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor, slowMode, Constants.BACK_RIGHT_MOTOR_ID);
            
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

        @BeforeEach
        void setup() {
            joystickY = 0;
            joystickX = 1;
            turnAmount = 0;
            scaleDownFactor = 1;
            slowMode = false;
        }

        @Test
        void getFrontLeftMotorPowerTest() {
            double frontLeftMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor, slowMode, Constants.FRONT_LEFT_MOTOR_ID);

            assertEquals(1, frontLeftMotorPower);
        }
        
        @Test
        void getFrontRightMotorPowerTest(){
            double frontRightMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor, slowMode, Constants.FRONT_RIGHT_MOTOR_ID);

            assertEquals(-1, frontRightMotorPower);
        }
        
        @Test
        void getBackLeftMotorPowerTest(){
            double backLeftMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor, slowMode, Constants.BACK_LEFT_MOTOR_ID);
            
            assertEquals(-1, backLeftMotorPower);
        }

        @Test
        void getBackRightMotorPowerTest(){
            double backRightMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor, slowMode, Constants.BACK_RIGHT_MOTOR_ID);
            
            assertEquals(1, backRightMotorPower);
        }
    }
}
