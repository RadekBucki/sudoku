package sudoku.group;

import sudoku.SudokuField;

public abstract class SudokuGroup {
    private SudokuField[] values;

    public SudokuGroup(SudokuField[] values) {
        this.values = values;
    }

    public boolean verify() {
        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                if (values[i].getFieldValue() == values[j].getFieldValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    public SudokuField getField(int index) {
        return values[index];
    }
}
