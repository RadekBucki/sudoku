import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sudoku.SudokuField;
import sudoku.group.SudokuRow;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuFieldTest {
    @Test
    @DisplayName("toStringTest")
    void toStringTest() {
        SudokuField field = new SudokuField(2);
        assertEquals("SudokuField{value=" + field.getFieldValue() + "}",field.toString());
    }

    @SuppressWarnings({"SimplifiableAssertion", "EqualsWithItself"})
    @Test
    @DisplayName("Equals and Hashcode test")
    void equalsAndHashcodeTest() {
        SudokuField field1 = new SudokuField(2);
        SudokuField field2 = new SudokuField(2);
        assertTrue(field1.equals(field2));
        assertEquals(field1.hashCode(),field2.hashCode());
        assertTrue(field1.equals(field1));
        assertEquals(field1.hashCode(),field1.hashCode());
    }

    @SuppressWarnings({"SimplifiableAssertion", "ConstantConditions", "EqualsBetweenInconvertibleTypes"})
    @Test
    @DisplayName("Equals and Hashcode test negative")
    void equalsAndHashcodeTestNegative() {
        SudokuField field1 = new SudokuField(2);
        SudokuField field2 = new SudokuField(3);
        assertFalse(field1.equals(field2));
        assertNotEquals(field1.hashCode(),field2.hashCode());
        assertFalse(field1.equals(null));
        SudokuRow row = new SudokuRow(new SudokuField[9]);
        assertFalse(field1.equals(row));
        assertNotEquals(field1.hashCode(),row.hashCode());

    }
}
