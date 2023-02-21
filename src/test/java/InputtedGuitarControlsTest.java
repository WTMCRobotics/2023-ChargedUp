import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.InputtedGuitarControls;

public class InputtedGuitarControlsTest {


    @Nested
    class positionsTest {
        public XboxController guitar;
        public InputtedGuitarControls guitarControls;

        boolean AButtonPressed = true;
        boolean BButtonPressed = true;
        boolean YButtonPressed = true;

        @BeforeEach
        public void setup() {
            guitar = mock(XboxController.class);

            // when(guitar.getBButtonPressed()).thenReturn(BButtonPressed);
            // when(guitar.getYButtonPressed()).thenReturn(YButtonPressed);

            guitarControls = new InputtedGuitarControls(guitar);
            guitarControls.updateLatestPostionPressed();
        }

        @Test
        public void mockSetupCorrectlyTest() {
            assertFalse(guitar.getAButtonPressed());
        }

        @Test
        public void JustAButtonPressed() {
            when(guitar.getAButtonPressed()).thenReturn(AButtonPressed);
            guitarControls = new InputtedGuitarControls(guitar);
            guitarControls.updateLatestPostionPressed();
            assertEquals(guitarControls.position, guitarControls.armEnum()[2]);
        }

        @Test
        public void JustBButtonPressed() {
            when(guitar.getBButtonPressed()).thenReturn(BButtonPressed);
            guitarControls = new InputtedGuitarControls(guitar);
            guitarControls.updateLatestPostionPressed();
            assertEquals(guitarControls.position, guitarControls.armEnum()[1]);
        }

        @Test
        public void JustYButtonPressed() {
            when(guitar.getYButtonPressed()).thenReturn(YButtonPressed);
            guitarControls = new InputtedGuitarControls(guitar);
            guitarControls.updateLatestPostionPressed();
            assertEquals(guitarControls.position, guitarControls.armEnum()[0]);
        }
    }
}