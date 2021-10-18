import java.util.Random;

public class SudokuBoard {
    private int[][] board = new int[9][9];

    public SudokuBoard() {
        fillBoard();
    }

    public void fillBoard() {
        placeNumbers();
    }

    public int get(int x, int y) {
        return board[x][y];
    }

    public void set(int x, int y, int value) {
        board[x][y] = value;
    }

    public void solveGame() {

    }

    private boolean placeNumbers() {
        int row = -1;
        int col = -1;
        boolean found = false;
        outer: for (int i = 0;i < 9;i++) {
            for (int j = 0;j < 9;j++) {
                if (board[i][j] == 0) {
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
            if (check(row, col, generated)) {
                board[row][col] = generated;
                if (placeNumbers()) {
                    return true;
                } else {
                    board[row][col] = 0;
                }
            }
        }
        return false;
    }

    private boolean check(int row,int col,int generated) {
        //Check row
        for (int j = 0;j < 9;j++) {
            if (board[row][j] == generated) {
                return false;
            }
        }
        //Check column
        for (int j = 0;j < 9;j++) {
            if (board[j][col] == generated) {
                return false;
            }
        }
        //Check square
        int boxRowFirst = row - row % 3;
        int boxColFirst = col - col % 3;
        for (int i = boxRowFirst;i < boxRowFirst + 3;i++) {
            for (int j = boxColFirst;j < boxColFirst + 3;j++) {
                if (board[i][j] == generated) {
                    return false;
                }
            }
        }
        return true;
    }
}
