package sudoku.solver;

import java.io.Serializable;
import sudoku.SudokuBoard;

public interface SudokuSolver extends Serializable {
    /**
     * Method which solves SudokuBoard.
     * @param board SudokuBoard which should be solved
     */
    void solve(SudokuBoard board);
}
