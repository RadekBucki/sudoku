package sudoku;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SudokuField {
    private int value;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public SudokuField() {
        this.value = 0;
    }

    public SudokuField(SudokuField sudokuField) {
        this.value = sudokuField.value;
        this.pcs = new PropertyChangeSupport(sudokuField.pcs);
    }

    public void setListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener("value",listener);
    }

    public SudokuField(int value) {
        this.value = value;
    }

    public int getFieldValue() {
        return value;
    }

    public void setFieldValue(int value) {
        int oldValue = this.value;
        this.value = value;
        if (oldValue != value) {
            pcs.firePropertyChange("value",oldValue,value);
        }
    }
}
