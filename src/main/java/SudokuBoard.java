public class SudokuBoard {
    private SudokuField[][] board = new SudokuField[9][9];
    private SudokuSolver sudokuSolver;

    public SudokuBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = new SudokuField();
            }
        }
        sudokuSolver = new BacktrackingSudokuSolver();
        solveGame();
    }

    public int get(int x, int y) {
        if (x >= 0 && x <= 8 && y >= 0 && y <= 8) {
            return (board[x][y]).getFieldValue();
        }
        return -1;
    }

    public void set(int x, int y, int value) {
        if (x >= 0 && x <= 8 && y >= 0 && y <= 8 && value >= 0 && value <= 9) {
            (board[x][y]).setFieldValue(value);
        }
    }

    public void solveGame() {
        sudokuSolver.solve(this);
    }

    public boolean checkBoard(int row, int col, int generated) {
        //Check row
        for (int j = 0;j < 9;j++) {
            if (get(row,j) == generated) {
                return false;
            }
        }
        //Check column
        for (int j = 0;j < 9;j++) {
            if (get(j,col) == generated) {
                return false;
            }
        }
        //Check square
        int boxRowFirst = row - row % 3;
        int boxColFirst = col - col % 3;
        for (int i = boxRowFirst;i < boxRowFirst + 3;i++) {
            for (int j = boxColFirst;j < boxColFirst + 3;j++) {
                if (get(i,j) == generated) {
                    return false;
                }
            }
        }
        return true;
    }
}
