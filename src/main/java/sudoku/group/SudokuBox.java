package sudoku.group;

import sudoku.SudokuField;

public class SudokuBox extends SudokuGroup {
    /**
     * Constructor with parameter values.
     * @param values array of values which should be set
     */
    public SudokuBox(SudokuField[] values) {
        super(values);
    }

    /**
     * Copy constructor.
     * @param sudokuBox object of the same instance which should be copied
     */
    public SudokuBox(SudokuBox sudokuBox) {
        super(sudokuBox);
    }
}
