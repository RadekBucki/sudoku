public abstract class SudokuGroup {
    public boolean verify(int[] values) {
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
