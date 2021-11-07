import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestTemplate;
import sudoku.SudokuField;
import sudoku.group.SudokuRow;
import org.junit.jupiter.api.Test;

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
    @DisplayName("Verify negatve test")
    void verifyNegativeTest() {
        for (int i = 0; i < 9; i++) {
            testValues[i] = new SudokuField(1);
        }
        SudokuRow sudokuGroup = new SudokuRow(testValues);
        assertFalse(sudokuGroup.verify());
    }
}