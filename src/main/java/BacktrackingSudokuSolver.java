import java.util.Random;

public class BacktrackingSudokuSolver implements SudokuSolver {
    public void solve(SudokuBoard board) {
        placeNumbers(board);
    }

    private boolean placeNumbers(SudokuBoard board) {
        int row = -1;
        int col = -1;
        boolean found = false;
        outer: for (int i = 0;i < 9;i++) {
            for (int j = 0;j < 9;j++) {
                if (board.get(i,j) == 0) {
                    row = i;
                    col = j;
                    found = true;
                    break outer;
                }
            }
        }
        if (!found) {
            return true;
        }
        int[] numbers = {1,2,3,4,5,6,7,8,9};
        //Fisher–Yates shuffle
        Random random = new Random();
        int temp;
        int index;
        for (int i = numbers.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = numbers[i];
            numbers[i] = numbers[index];
            numbers[index] = temp;
        }
        for (int generated : numbers) {
            if (check(board,row, col, generated)) {
                board.set(row,col,generated);
                if (placeNumbers(board)) {
                    return true;
                } else {
                    board.set(row,col,0);
                }
            }
        }
        return false;
    }

    private boolean check(SudokuBoard board,int row,int col,int generated) {
        //Check row
        for (int j = 0;j < 9;j++) {
            if (board.get(row,j) == generated) {
                return false;
            }
        }
        //Check column
        for (int j = 0;j < 9;j++) {
            if (board.get(j,col) == generated) {
                return false;
            }
        }
        //Check square
        int boxRowFirst = row - row % 3;
        int boxColFirst = col - col % 3;
        for (int i = boxRowFirst;i < boxRowFirst + 3;i++) {
            for (int j = boxColFirst;j < boxColFirst + 3;j++) {
                if (board.get(i,j) == generated) {
                    return false;
                }
            }
        }
        return true;
    }
}
