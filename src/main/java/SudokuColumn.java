public class SudokuColumn extends SudokuGroup {
    int[] column = new int[9];

    public boolean verify() {
        return super.verify(column);
    }
}
