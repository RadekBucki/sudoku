public class SudokuField {
    private int value;
    private SudokuColumn column;
    private SudokuRow row;
    private SudokuBox box;

    public SudokuField() {
        this.value = 0;
    }

    public int getFieldValue() {
        return value;
    }

    public void setFieldValue(int value) {
        this.value = value;
    }
}
