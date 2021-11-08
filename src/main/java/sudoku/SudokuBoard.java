package sudoku;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import sudoku.group.SudokuBox;
import sudoku.group.SudokuColumn;
import sudoku.group.SudokuRow;
import sudoku.solver.SudokuSolver;

public class SudokuBoard implements PropertyChangeListener {
    private List<List<SudokuField>> board = Stream.generate(()
            -> List.of(getFields())).limit(9).collect(Collectors.toList());
    private List<SudokuRow> rows = Stream.generate(()
            -> new SudokuRow(getFields())).limit(9).collect(Collectors.toList());
    private List<SudokuColumn> columns = Stream.generate(()
            -> new SudokuColumn(getFields())).limit(9).collect(Collectors.toList());
    private SudokuBox[][] boxes = new SudokuBox[3][3];
    private SudokuSolver sudokuSolver;

    public SudokuBoard(SudokuSolver solver) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board.get(i).get(j).setListener(this);
            }
        }
        for (int i = 0; i < 9; i++) {
            SudokuField[] column = new SudokuField[9];
            for (int j = 0; j < 9; j++) {
                column[j] = board.get(j).get(i);
            }
            rows.set(i, new SudokuRow(board.get(i).toArray(new SudokuField[9])));
            columns.set(i, new SudokuColumn(column));
        }
        // Create boxes
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                SudokuField[] box = new SudokuField[9];
                // Get box fields
                for (int k = i * 3; k < i * 3 + 3; k++) {
                    for (int l = j * 3; l < j * 3 + 3; l++) {
                        box[k % 3 * 3 + l % 3] = board.get(k).get(l);
                    }
                }
                boxes[i][j] = new SudokuBox(box);
            }
        }
        sudokuSolver = solver;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!checkBoard()) {
            SudokuField field = (SudokuField) evt.getSource();
            field.setFieldValue((Integer) evt.getOldValue());
        }
    }

    public int get(int x, int y) {
        if (x >= 0 && x <= 8 && y >= 0 && y <= 8) {
            return board.get(x).get(y).getFieldValue();
        }
        return -1;
    }

    public void set(int x, int y, int value) {
        if (x >= 0 && x <= 8 && y >= 0 && y <= 8 && value >= 0 && value <= 9) {
            (board.get(x).get(y)).setFieldValue(value);
        }
    }

    public void solveGame() {
        sudokuSolver.solve(this);
    }

    private boolean checkBoard() {
        for (int i = 0; i < 9; i++) {
            if (!(rows.get(i).verify() && columns.get(i).verify())) {
                return false;
            }
        }

        for (SudokuBox[] boxRow : boxes) {
            for (SudokuBox box : boxRow) {
                if (!box.verify()) {
                    return false;
                }
            }
        }
        return true;
    }

    public SudokuRow getRow(int y) {
        return rows.get(y);
    }

    public SudokuColumn getColumn(int x) {
        return columns.get(x);
    }

    public SudokuBox getBox(int x, int y) {
        return boxes[x][y];
    }

    public boolean check(int row, int col, int generated) {
        //Check row
        for (int j = 0; j < 9; j++) {
            if (get(row, j) == generated) {
                return false;
            }
        }
        //Check column
        for (int j = 0; j < 9; j++) {
            if (get(j, col) == generated) {
                return false;
            }
        }
        //Check square
        int boxRowFirst = row - row % 3;
        int boxColFirst = col - col % 3;
        for (int i = boxRowFirst; i < boxRowFirst + 3; i++) {
            for (int j = boxColFirst; j < boxColFirst + 3; j++) {
                if (get(i, j) == generated) {
                    return false;
                }
            }
        }
        return true;
    }

    private SudokuField[] getFields() {
        SudokuField[] fields = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            fields[i] = new SudokuField();
        }
        return fields;
    }
}
