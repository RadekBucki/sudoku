public abstract class SudokuGroup {
    private SudokuField[] values = new SudokuField[9];

    public SudokuGroup(SudokuField[] values) {
        this.values = values;
    }

    public boolean verify() {
        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1;j < values.length;j++) {
                if (values[i] == values[j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
