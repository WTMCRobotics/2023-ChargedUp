import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.drive.MecanumDrive.WheelSpeeds;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.robot.InputtedControls;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ControlsTest {

    @Nested
    class BasicGetValuesTest {

        XboxController xboxController;
        InputtedControls controls;

        double leftX = 0.5;
        double leftY = 0.75;
        double rightX = 0.75;
        // dictates if slowmode is enabled; > .5 enables slowmode
        double rightTriggerAxis = 0.4;

        @BeforeEach
        void setup() {
            this.xboxController = mock(XboxController.class);
            when(xboxController.getLeftX()).thenReturn(leftX);
            when(xboxController.getLeftY()).thenReturn(leftY);
            when(xboxController.getRightX()).thenReturn(rightX);
            when(xboxController.getRightTriggerAxis()).thenReturn(rightTriggerAxis);
            controls = new InputtedControls(xboxController);
        }


        @Test
        public void xTest() {
            assertEquals(.5, controls.getY());
        }

        @Test
        public void yTest() {
            assertEquals(.75, controls.getX());
        }
    }
    @Nested
    class SlowModeTest {

        XboxController xboxController;
        InputtedControls controls;

        double leftX = .5;
        double leftY = .75;
        double rightX = .75;
        // dictates if slowmode is enabled; .5 enables slowmode
        double rightTriggerAxis = .6;

        @BeforeEach
        void setup() {
            this.xboxController = mock(XboxController.class);
            when(xboxController.getLeftX()).thenReturn(leftX);
            when(xboxController.getLeftY()).thenReturn(leftY);
            when(xboxController.getRightX()).thenReturn(rightX);
            when(xboxController.getRightTriggerAxis()).thenReturn(rightTriggerAxis);
            controls = new InputtedControls(xboxController);
        }


        @Test
        public void xWhilstInSlowmode() {
            assertEquals(.125, controls.getY());
        }



        @Test
        public void yWhilstInSlowmode() {
            assertEquals(0.1875, controls.getX());
        }
    }
    // Checking if wpilibs mechanumDrive class ratio from xbox controller to motor speed is a
    // constant 1:1
    @Nested
    class IsLinear {

        XboxController xboxController;
        InputtedControls controls;

        double leftX = .125;
        double leftY = 0;
        double rightX = 0;
        MotorController motors;

        @Test
        void linearTest() {

            controls = new InputtedControls(xboxController);
            this.motors = mock(MotorController.class);

            // MecanumDrive mecanumDriveTrain = new MecanumDrive(motors, motors, motors, motors);
            for (int i = 0; i < 4; i++) {
                WheelSpeeds speeds = MecanumDrive.driveCartesianIK(leftX, leftY, rightX);
                System.out.println("front left is " + speeds.frontLeft);
                System.out.println("front right is " + speeds.frontRight);
                System.out.println("back left is " + speeds.rearLeft);
                System.out.println("back right is " + speeds.rearRight);
                leftX *= 2;
            }
        }


    }
}
