import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sudoku.SudokuField;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SudokuFieldTest {
    @Test
    @DisplayName("toStringTest")
    void toStringTest() {
        SudokuField field = new SudokuField(2);
        System.out.println(field.toString());
        assertEquals("SudokuField{value=" + field.getFieldValue() + "}",field.toString());
    }
}
