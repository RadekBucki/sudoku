import java.util.Random;

public class SudokuBoard {
    private int[][] board = new int[9][9];

    public SudokuBoard() {
        solveGame();
    }

    public int get(int x, int y) {
        if (x >= 0 && x <= 8 && y >= 0 && y <= 8) {
            return board[x][y];
        }
        return -1;
    }

    public void set(int x, int y, int value) {
        if (x >= 0 && x <= 8 && y >= 0 && y <= 8 && value >= 0 && value <= 9) {
            board[x][y] = value;
        }
    }

    public void solveGame() {
        (new BacktrackingSudokuSolver()).solve(this);
    }

    public boolean check(SudokuBoard board,int row,int col,int generated) {
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
