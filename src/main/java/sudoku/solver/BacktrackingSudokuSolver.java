package sudoku.solver;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import sudoku.SudokuBoard;

public class BacktrackingSudokuSolver implements SudokuSolver {
    /**
     * Method which solves SudokuBoard.
     * @param board SodokuBoard which should be solved
     */
    public void solve(SudokuBoard board) {
        placeNumbers(board);
    }

    /**
     * Recursive method which solves SudokuBoard.
     * @param board SodokuBoard which should be solved
     * @return boolean - information about status of solving
     */
    private boolean placeNumbers(SudokuBoard board) {
        int row = -1;
        int col = -1;
        boolean found = false;

outer:  for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.get(i, j) == 0) {
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
        List<Integer> numbers = Stream.iterate(1,n -> n + 1).limit(9).collect(Collectors.toList());
        Collections.shuffle(numbers);
        for (int generated : numbers) {
            if (board.check(row, col, generated)) {
                board.set(row, col, generated);
                if (placeNumbers(board)) {
                    return true;
                } else {
                    board.set(row, col, 0);
                }
            }
        }
        return false;
    }
}
