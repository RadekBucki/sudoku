package sudoku.group;

import sudoku.SudokuField;

public abstract class SudokuGroup {
    private SudokuField[] values;

    public SudokuGroup(SudokuField[] values) {
        this.values = values;
    }

    public SudokuGroup(SudokuGroup sudokuGroup) {
        this.values = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            values[i] = new SudokuField(sudokuGroup.values[i]);
        }
    }

    public boolean verify() {
        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                if (values[i].getFieldValue() == values[j].getFieldValue()
                        && values[i].getFieldValue() != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getField(int index) {
        return values[index].getFieldValue();
    }
}
