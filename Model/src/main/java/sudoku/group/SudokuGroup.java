package sudoku.group;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import exceptions.ModelCloneNotSupportedException;
import sudoku.SudokuField;

public abstract class SudokuGroup implements Serializable, Cloneable {
    private List<SudokuField> values = Arrays.asList(new SudokuField[9]);

    /**
     * Constructor with parameter values.
     *
     * @param values array of values which should be set
     */
    public SudokuGroup(SudokuField[] values) {
        for (int i = 0; i < 9; i++) {
            this.values.set(i, values[i]);
        }
    }

    /**
     * Copy constructor.
     *
     * @param sudokuGroup object of the same instance which should be copied
     */
    public SudokuGroup(SudokuGroup sudokuGroup) {
        for (int i = 0; i < 9; i++) {
            values.set(i, new SudokuField(sudokuGroup.values.get(i)));
        }
    }

    /**
     * Method checks unique of values.
     *
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
     *
     * @param index position of SudokuField
     * @return int value of SudokuField
     */
    public int getField(int index) {
        return values.get(index).getFieldValue();
    }

    /**
     * Getter of fields values.
     *
     * @return int array of SudokuFields values
     */
    public int[] getFields() {
        int[] fields = new int[9];
        for (int i = 0; i < 9; i++) {
            fields[i] = values.get(i).getFieldValue();
        }
        return fields;
    }

    /**
     * Override of method returns string representation of this object.
     * @return string
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("values", values)
                .toString();
    }

    /**
     * Method returns information whether the objects are the same.
     * @param o tested object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SudokuGroup that = (SudokuGroup) o;
        return Objects.equal(values, that.values);
    }

    /**
     * Method returns the hash code.
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(values);
    }

    /**
     * Returns deep copy of SudokuGroup.
     * @return SudokuGroup
     */
    @Override
    public SudokuGroup clone() throws CloneNotSupportedException {
        try {
            SudokuGroup clone = (SudokuGroup) super.clone();
            clone.values = new ArrayList<>(values);
            for (int i = 0; i < 9; i++) {
                clone.values.set(i, values.get(i).clone());
            }
            return clone;
        } catch(CloneNotSupportedException e) {
            throw new ModelCloneNotSupportedException("clone.exception");
        }
    }
}
