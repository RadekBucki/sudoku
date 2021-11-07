import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MainTest {

    @Test
    @DisplayName("Test Main")
    void testMain() {
        Main main = new Main();
        String[] args = {};
        Main.main(args);
    }
}
