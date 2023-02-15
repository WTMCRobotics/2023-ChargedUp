import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.InputtedControls;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ControlsTest {

    @Nested
    class SlowModeTest {

        XboxController xboxController;
        InputtedControls controls;

        double leftX = .5;
        double leftY = .75;
        double rightX = .75;
        double rightTriggerAxis = .4;

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
        public void mockingTest() {
            assertEquals(.5, xboxController.getLeftX());
        }

        @Test
        public void xTest() {
            assertEquals(.5, controls.getY());
        }

        @Test
        public void xWhilstInSlowmode() {
            assertEquals(.125, controls.getY());
        }

        @Test
        public void yTest() {
            assertEquals(.75, controls.getX());
        }

        @Test
        public void yWhilstInSlowmode() {
            assertEquals(0.1875, controls.getX());
        }
    }
}
