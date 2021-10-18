import java.util.Random;

public class SudokuBoard {
    private int[][] board = new int[9][9];

    public SudokuBoard() {
        solveGame();
    }

    public int get(int x, int y) {
        return board[x][y];
    }

    public void set(int x, int y, int value) {
        board[x][y] = value;
    }

    public void solveGame() {
        (new BacktrackingSudokuSolver()).solve(this);
    }


}
