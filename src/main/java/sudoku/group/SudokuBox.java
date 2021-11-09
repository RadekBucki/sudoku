package sudoku.group;

import sudoku.SudokuField;

public class SudokuBox extends SudokuGroup {
    public SudokuBox(SudokuField[] values) {
        super(values);
    }

    public SudokuBox(SudokuBox sudokuBox) {
        super(sudokuBox);
    }
}
