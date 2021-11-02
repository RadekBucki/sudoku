public class SudokuRow extends SudokuGroup {
    private int[] row = new int[9];

    public boolean verify() {
        return super.verify(row);
    }
}
