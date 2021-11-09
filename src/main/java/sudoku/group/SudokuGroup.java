package sudoku.group;

import java.util.Arrays;
import java.util.List;
import sudoku.SudokuField;

public abstract class SudokuGroup {
    private List<SudokuField> values;

    /**
     * Constructor with parameter values.
     * @param values array of values which should be set
     */
    public SudokuGroup(SudokuField[] values) {
        this.values = Arrays.asList(values);
    }

    /**
     * Copy constructor.
     * @param sudokuGroup object of the same instance which should be copied
     */
    public SudokuGroup(SudokuGroup sudokuGroup) {
        this.values = Arrays.asList(new SudokuField[9]);
        for (int i = 0; i < 9; i++) {
            values.set(i, new SudokuField(sudokuGroup.values.get(i)));
        }
    }

    /**
     * Method checks unique of values.
     * @return boolean - status of the unique values
     */
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

    /**
     * Getter of SudokuField value.
     * @param index position of SudokuField
     * @return int value of SudokuField
     */
    public int getField(int index) {
        return values.get(index).getFieldValue();
    }

    /**
     * Getter of fields values.
     * @return int array of SudokuFields values
     */
    public int[] getFields() {
        int[] fields = new int[9];
        for (int i = 0; i < 9; i++) {
            fields[i] = values.get(i).getFieldValue();
        }
        return fields;
    }
}
