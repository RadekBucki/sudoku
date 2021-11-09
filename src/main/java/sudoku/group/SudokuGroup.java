package sudoku.group;

import java.util.Arrays;
import java.util.List;
import sudoku.SudokuField;

public abstract class SudokuGroup {
    private List<SudokuField> values;

    public SudokuGroup(SudokuField[] values) {
        this.values = Arrays.asList(values);
    }

    public SudokuGroup(SudokuGroup sudokuGroup) {
        this.values = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            values[i] = new SudokuField(sudokuGroup.values[i]);
        }
    }

    public boolean verify() {
        for (int i = 0; i < values.size(); i++) {
            for (int j = i + 1; j < values.size(); j++) {
                if (values.get(i).getFieldValue() == values.get(j).getFieldValue()
                        && values.get(i).getFieldValue() != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getField(int index) {
        return values.get(index).getFieldValue();
    }
}
