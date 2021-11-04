package sudoku;

public class SudokuField {
    private int value;

    public SudokuField() {
        this.value = 0;
    }

    public SudokuField(int value) {
        this.value = value;
    }

    public int getFieldValue() {
        return value;
    }

    public void setFieldValue(int value) {
        this.value = value;
    }
}
