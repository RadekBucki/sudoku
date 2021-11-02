public class SudokuBoard {
    private SudokuField[][] board = new SudokuField[9][9];
    private SudokuRow[] rows = new SudokuRow[9];
    private SudokuColumn[] columns = new SudokuColumn[9];
    private SudokuBox[][] boxes = new SudokuBox[3][3];
    private SudokuSolver sudokuSolver;

    public SudokuBoard(SudokuSolver solver) {
        for (int i = 0; i < 9; i++) {
            rows[i] = new SudokuRow();
            columns[i] = new SudokuColumn();
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boxes[i][j] = new SudokuBox();
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int boxRow = (i - i % 3) / 3;
                int boxCol = (j - j % 3) / 3;
                board[i][j] = new SudokuField(rows[i],columns[j],boxes[boxRow][boxCol]);
            }
        }
        sudokuSolver = solver;
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
