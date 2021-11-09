package sudoku.group;

import sudoku.SudokuField;

public class SudokuColumn extends SudokuGroup {
    public SudokuColumn(SudokuField[] values) {
        super(values);
    }

    public SudokuColumn(SudokuColumn sudokuColumn) {
        super(sudokuColumn);
    }
}
