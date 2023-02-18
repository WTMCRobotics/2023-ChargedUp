import static org.junit.jupiter.api.Assertions.assertFalse;
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

        boolean AButtonPressed = false;
        boolean BButtonPressed = false;
        boolean YButtonPressed = false;

        @BeforeEach
        public void setup() {
            guitar = mock(XboxController.class);
            when(guitar.getAButtonPressed()).thenReturn(AButtonPressed);
            when(guitar.getBButtonPressed()).thenReturn(BButtonPressed);
            when(guitar.getYButtonPressed()).thenReturn(YButtonPressed);

            guitarControls = new InputtedGuitarControls(guitar);
        }

        @Test
        public void mockSetupCorrectlyTest() {
            assertFalse(guitar.getAButtonPressed());
        }
    }
}
