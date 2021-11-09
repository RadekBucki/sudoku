package sudoku.solver;

import sudoku.SudokuBoard;

public interface SudokuSolver {
    /**
     * Method which solves SudokuBoard.
     * @param board SudokuBoard which should be solved
     */
    void solve(SudokuBoard board);
}
