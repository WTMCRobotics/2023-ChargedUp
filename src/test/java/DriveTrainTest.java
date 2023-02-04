import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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

        @BeforeEach
        void setup() {
            joystickY = 1;
            joystickX = 0;
            turnAmount = 0;
            scaleDownFactor = 1;
        }

        @Test
        void getFrontLeftMotorPowerTest() {
            double frontLeftMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor);

            assertEquals(1, frontLeftMotorPower);
        }
        
        @Test
        void getFrontRightMotorPowerTest(){
            double frontRightMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor);

            assertEquals(1, frontRightMotorPower);
        }
        
        @Test
        void getBackLeftMotorPowerTest(){
            double backLeftMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor);
            
            assertEquals(1, backLeftMotorPower);
        }

        @Test
        void getBackRightMotorPowerTest(){
            double backRightMotorPower = robot.getMotorPower(joystickX, joystickY, turnAmount, scaleDownFactor);
            
            assertEquals(1, backRightMotorPower);
        }
    }
}
