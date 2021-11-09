package sudoku.group;

import sudoku.SudokuField;

public class SudokuRow extends SudokuGroup {
    /**
     * Constructor with parameter values.
     * @param values array of values which should be set
     */
    public SudokuRow(SudokuField[] values) {
        super(values);
    }

    /**
     * Copy constructor.
     * @param sudokuRow object of the same instance which should be copied
     */
    public SudokuRow(SudokuRow sudokuRow) {
        super(sudokuRow);
    }
}
