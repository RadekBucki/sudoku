package sudoku;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

public class SudokuField implements Serializable,Comparable<SudokuField>, Cloneable {
    private int value = 0;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Constructor with default value = 0.
     */
    public SudokuField() {
    }

    /**
     * Copy constructor.
     * @param sudokuField object of the same instance which should be copied
     */
    public SudokuField(SudokuField sudokuField) {
        this.value = sudokuField.value;
        this.pcs = new PropertyChangeSupport(sudokuField.pcs);
    }

    /**
     * Constructor with set concrete value.
     * @param value value which should be set
     */
    public SudokuField(int value) {
        this.value = value;
    }

    /**
     * Set sudoku field listener.
     * @param listener object which implements PropertyChangeListener
     */
    public void setListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener("value",listener);
    }

    /**
     * Getter of value parameter.
     * @return int value
     */
    public int getFieldValue() {
        return value;
    }

    /**
     * Setter of value parameter.
     * @param value which should be set
     */
    public void setFieldValue(int value) {
        int oldValue = this.value;
        this.value = value;
        if (oldValue != value) {
            pcs.firePropertyChange("value",oldValue,value);
        }
    }

    /**
     * Override of method returns string representation of this object.
     * @return string
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", value)
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
        SudokuField that = (SudokuField) o;
        return value == that.value;
    }

    /**
     * Method returns the hash code.
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    /**
     * Method compares values of sudokuFields
     * @param o
     * @return int
     */
    @Override
    public int compareTo(SudokuField o) {
        return this.value - o.value;
    }

    /**
     * Returns deep copy of SudokuBoard.
     * @return SudokuField
     */
    @Override
    public SudokuField clone() throws CloneNotSupportedException {
        SudokuField clone = (SudokuField) super.clone();
        clone.setFieldValue(this.getFieldValue());
        return clone;
    }
}
