package sudoku.group;

import sudoku.SudokuField;

public class SudokuRow extends SudokuGroup {
    public SudokuRow(SudokuField[] values) {
        super(values);
    }

    public SudokuRow(SudokuRow sudokuRow) {
        super(sudokuRow);
    }
}
