package sudoku.group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import sudoku.SudokuField;

public abstract class SudokuGroup {
    private List<SudokuField> values;

    public SudokuGroup(SudokuField[] values) {
        this.values = Arrays.asList(values);
    }

    public SudokuGroup(SudokuGroup sudokuGroup) {
        this.values = Arrays.asList(new SudokuField[9]);
        for (int i = 0; i < 9; i++) {
            values.set(i, new SudokuField(sudokuGroup.values.get(i)));
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

    public int[] getFields() {
        int[] fields = new int[9];
        for (int i = 0; i < 9; i++) {
            fields[i] = values.get(i).getFieldValue();
        }
        return fields;
    }
}
