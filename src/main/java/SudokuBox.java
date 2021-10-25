public class SudokuBox extends SudokuGroup {
    int[] box = new int[9];

    public boolean verify() {
        return super.verify(box);
    }
}
