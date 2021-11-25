package sudoku.group;

import sudoku.SudokuField;

public class SudokuColumn extends SudokuGroup {
    /**
     * Constructor with parameter values.
     * @param values array of values which should be set
     */
    public SudokuColumn(SudokuField[] values) {
        super(values);
    }

    /**
     * Copy constructor.
     * @param sudokuColumn object of the same instance which should be copied
     */
    public SudokuColumn(SudokuColumn sudokuColumn) {
        super(sudokuColumn);
    }
}
