import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GreeterTest {

    @Test
    @DisplayName("Test Greet")
    public void testGreet() {
        Greeter instance = new Greeter();
        String result = instance.greet("world!");
        assertEquals("Hello world!", result);
    }

}
