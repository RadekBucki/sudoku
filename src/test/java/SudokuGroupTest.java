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

    @BeforeEach
    void init() {
        testValues = new SudokuField[9];
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
        String toStringCol = "SudokuColumn{values=[";
        String toStringRow = "SudokuRow{values=[";
        String toStringBox = "SudokuBox{values=[";
        for (int i = 0; i < 9; i++) {
            toStringRow += new SudokuField(board.get(0, i));
            toStringCol += new SudokuField(board.get(i, 0));
            toStringBox += new SudokuField(board.get(i / 3, i % 3));
            if (i != 8) {
                toStringRow += ", ";
                toStringCol += ", ";
                toStringBox += ", ";
            }
        }
        toStringRow += "]}";
        toStringCol += "]}";
        toStringBox += "]}";
        assertEquals(toStringRow, board.getRow(0).toString());
        assertEquals(toStringCol, board.getColumn(0).toString());
        assertEquals(toStringBox, board.getBox(0, 0).toString());
    }

    @Test
    @DisplayName("Equals and Hashcode test")
    void equalsAndHashcodeTest() {
        SudokuRow sudokuGroup1 = new SudokuRow(testValues);
        SudokuRow sudokuGroup2 = new SudokuRow(testValues);
        assertTrue(sudokuGroup1.equals(sudokuGroup2));
        assertEquals(sudokuGroup1.hashCode(), sudokuGroup2.hashCode());
    }

    @Test
    @DisplayName("Equals and Hashcode test negative")
    void equalsAndHashcodeTestNegative() {
        SudokuRow sudokuGroup1 = new SudokuRow(testValues);
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
        SudokuRow sudokuGroup1 = new SudokuRow(testValues);
        SudokuRow sudokuGroup2 = new SudokuRow(testValues);
        assertEquals(sudokuGroup1.equals(sudokuGroup2), sudokuGroup1.hashCode() == sudokuGroup2.hashCode());
    }
}