import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import sudoku.SudokuBoard;
import sudoku.SudokuField;
import sudoku.group.SudokuRow;
import org.junit.jupiter.api.Test;
import sudoku.solver.BacktrackingSudokuSolver;

import java.lang.reflect.Field;

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
        assertEquals(toStringBox, board.getBox(0,0).toString());
    }
}