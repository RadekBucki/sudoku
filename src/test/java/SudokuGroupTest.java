import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SudokuGroupTest {

    @Test
    void verifyPositiveTest() {
        SudokuField[] testValues = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            testValues[i]=new SudokuField(i+1);
        }
        SudokuRow sudokuGroup = new SudokuRow(testValues);
        assertTrue(sudokuGroup.verify());
    }

    @Test
    void verifyNegativeTest() {
        SudokuField[] testValues = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            testValues[i]=new SudokuField(1);
        }
        SudokuRow sudokuGroup = new SudokuRow(testValues);
        assertFalse(sudokuGroup.verify());
    }
}