public class SudokuField {
    private int value;
    private SudokuColumn column;
    private SudokuRow row;
    private SudokuBox box;

    public SudokuField(SudokuRow row,SudokuColumn column,SudokuBox box) {
        this.value = 0;
        this.row = row;
        this.box = box;
        this.column = column;
    }

    public int getFieldValue() {
        return value;
    }

    public void setFieldValue(int value) {
        this.value = value;
    }
}
