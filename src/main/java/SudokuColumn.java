public class SudokuColumn extends SudokuGroup {
    private int[] column = new int[9];

    public boolean verify() {
        return super.verify(column);
    }
}
