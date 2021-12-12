import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sudoku.SudokuBoard;
import sudoku.SudokuField;
import sudoku.group.SudokuRow;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuFieldTest {

    SudokuField field1, field2;

    @BeforeEach
    void init() {
        field1 = new SudokuField(2);
        field2 = new SudokuField(2);
    }

    @Test
    @DisplayName("toStringTest")
    void toStringTest() {
        SudokuField field = new SudokuField(2);
        assertEquals("SudokuField{value=" + field.getFieldValue() + "}", field.toString());
    }

    @SuppressWarnings({"SimplifiableAssertion", "EqualsWithItself"})
    @Test
    @DisplayName("Equals and Hashcode test")
    void equalsAndHashcodeTest() {
        assertTrue(field1.equals(field2));
        assertEquals(field1.hashCode(), field2.hashCode());
        assertTrue(field1.equals(field1));
        assertEquals(field1.hashCode(), field1.hashCode());
    }

    @SuppressWarnings({"SimplifiableAssertion", "ConstantConditions", "EqualsBetweenInconvertibleTypes"})
    @Test
    @DisplayName("Equals and Hashcode test negative")
    void equalsAndHashcodeTestNegative() {
        SudokuField field2 = new SudokuField(3);
        assertFalse(field1.equals(field2));
        assertNotEquals(field1.hashCode(), field2.hashCode());
        assertFalse(field1.equals(null));
        SudokuRow row = new SudokuRow(new SudokuField[9]);
        assertFalse(field1.equals(row));
        assertNotEquals(field1.hashCode(), row.hashCode());
    }

    @Test
    @DisplayName("Equals and hashCode cohesion test")
    void equalsAndHashCodeCohesionTest() {
        assertEquals(field1.equals(field2), field1.hashCode() == field2.hashCode());
        field2.setFieldValue(3);
        assertEquals(field1.equals(field2), field1.hashCode() == field2.hashCode());
    }

    @Test
    @DisplayName("Compare To Test")
    void compareToTest() {
        assertEquals(field1.compareTo(field2),0);
        field1.setFieldValue(4);
        assertEquals(field1.compareTo(field2),2);
        assertEquals(field2.compareTo(field1),-2);
    }

    @Test
    @DisplayName("Clone test")
    void cloneTest() throws CloneNotSupportedException {
        SudokuField newField = (SudokuField) field1.clone();
        assertTrue(field1.equals(newField));
    }
}
