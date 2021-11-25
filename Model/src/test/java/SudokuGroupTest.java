import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import sudoku.SudokuBoard;
import sudoku.SudokuField;
import sudoku.group.SudokuRow;
import org.junit.jupiter.api.Test;
import sudoku.solver.BacktrackingSudokuSolver;

import static org.junit.jupiter.api.Assertions.*;

class SudokuGroupTest {

    SudokuField[] testValues;
    SudokuRow sudokuGroup1, sudokuGroup2;

    @BeforeEach
    void init() {
        testValues = new SudokuField[9];
        sudokuGroup1 = new SudokuRow(testValues);
        sudokuGroup2 = new SudokuRow(testValues);
    }

    @Test
    @DisplayName("Verify positive test")
    void verifyPositiveTest() {
        for (int i = 0; i < 9; i++) {
            testValues[i] = new SudokuField(i + 1);
        }
        SudokuRow sudokuGroup = new SudokuRow(testValues);
        assertTrue(sudokuGroup.verify());
    }

    @Test
    @DisplayName("Verify negative test")
    void verifyNegativeTest() {
        for (int i = 0; i < 9; i++) {
            testValues[i] = new SudokuField(1);
        }
        SudokuRow sudokuGroup = new SudokuRow(testValues);
        assertFalse(sudokuGroup.verify());
    }

    @Test
    @DisplayName("toStringTest")
    void toStringTest() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        board.solveGame();
        StringBuilder toStringCol = new StringBuilder("SudokuColumn{values=[");
        StringBuilder toStringRow = new StringBuilder("SudokuRow{values=[");
        StringBuilder toStringBox = new StringBuilder("SudokuBox{values=[");
        for (int i = 0; i < 9; i++) {
            toStringRow.append(new SudokuField(board.get(0, i)));
            toStringCol.append(new SudokuField(board.get(i, 0)));
            toStringBox.append(new SudokuField(board.get(i / 3, i % 3)));
            if (i != 8) {
                toStringRow.append(", ");
                toStringCol.append(", ");
                toStringBox.append(", ");
            }
        }
        toStringRow.append("]}");
        toStringCol.append("]}");
        toStringBox.append("]}");
        assertEquals(toStringRow.toString(), board.getRow(0).toString());
        assertEquals(toStringCol.toString(), board.getColumn(0).toString());
        assertEquals(toStringBox.toString(), board.getBox(0, 0).toString());
    }

    @SuppressWarnings({"SimplifiableAssertion", "EqualsWithItself"})
    @Test
    @DisplayName("Equals and Hashcode test")
    void equalsAndHashcodeTest() {
        assertTrue(sudokuGroup1.equals(sudokuGroup2));
        assertTrue(sudokuGroup1.equals(sudokuGroup1));
        assertEquals(sudokuGroup1.hashCode(), sudokuGroup2.hashCode());
    }

    @SuppressWarnings({"SimplifiableAssertion", "ConstantConditions", "EqualsBetweenInconvertibleTypes"})
    @Test
    @DisplayName("Equals and Hashcode test negative")
    void equalsAndHashcodeTestNegative() {
        SudokuField[] fields = new SudokuField[9];
        fields[0] = new SudokuField(5);
        SudokuRow sudokuGroup2 = new SudokuRow(fields);
        assertFalse(sudokuGroup1.equals(sudokuGroup2));
        assertNotEquals(sudokuGroup1.hashCode(), sudokuGroup2.hashCode());
        assertFalse(sudokuGroup1.equals(null));
        SudokuField field = new SudokuField();
        assertFalse(sudokuGroup1.equals(field));
        assertNotEquals(sudokuGroup1.hashCode(), field.hashCode());
    }

    @Test
    @DisplayName("Equals and hashCode cohesion test")
    void equalsAndHashCodeCohesionTest() {
        assertEquals(sudokuGroup1.equals(sudokuGroup2), sudokuGroup1.hashCode() == sudokuGroup2.hashCode());
        testValues[0] = new SudokuField(9);
        SudokuRow sudokuGroup3 = new SudokuRow(testValues);
        assertEquals(sudokuGroup1.equals(sudokuGroup3), sudokuGroup1.hashCode() == sudokuGroup3.hashCode());
    }
}