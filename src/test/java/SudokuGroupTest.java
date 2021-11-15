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
        for (int i = 0; i < 9; i++) {
            String toString = "\n[";
            for (int j = 0; j < 9; j++) {
                toString += board.get(i,j);
                if(j != 8) {
                    toString += ", ";
                }
            }
            toString += "]";
            assertEquals(toString,board.getRow(i).toString());
        }
    }
}