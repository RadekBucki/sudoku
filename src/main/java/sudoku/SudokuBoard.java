package sudoku;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import sudoku.group.SudokuBox;
import sudoku.group.SudokuColumn;
import sudoku.group.SudokuGroup;
import sudoku.group.SudokuRow;
import sudoku.solver.SudokuSolver;

public class SudokuBoard implements PropertyChangeListener {
    private SudokuField[][] board = new SudokuField[9][9];
    private List<SudokuRow> rows = Arrays.asList(new SudokuRow[9]);
    private List<SudokuColumn> columns = Arrays.asList(new SudokuColumn[9]);
    private List<SudokuBox> boxes = Arrays.asList(new SudokuBox[9]);
    private SudokuSolver sudokuSolver;

    /**
     * Constructor.
     * @param solver implementation of interface SudokuSolver
     */
    public SudokuBoard(SudokuSolver solver) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = new SudokuField();
                board[i][j].setListener(this);
            }
        }
        // Create columns, rows and boxes
        for (int i = 0; i < 9; i++) {
            SudokuField[] column = new SudokuField[9];
            for (int j = 0; j < 9; j++) {
                column[j] = board[j][i];
            }
            rows.set(i, new SudokuRow(board[i]));
            columns.set(i, new SudokuColumn(column));

            SudokuField[] box = new SudokuField[9];
            int j = 0;
            for (int k = (i / 3) * 3; k < (i / 3) * 3 + 3; k++) {
                for (int l = (i % 3) * 3; l < (i % 3) * 3 + 3; l++) {
                    box[j++] = board[k][l];
                }
            }
            boxes.set(i,new SudokuBox(box));
        }
        sudokuSolver = solver;
    }

    /**
     * Implementation of Listener pattern.
     * @param evt event get from listened objects
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!checkBoard()) {
            SudokuField field = (SudokuField) evt.getSource();
            field.setFieldValue((Integer) evt.getOldValue());
        }
    }

    /**
     * Getter of concrete field value in board.
     * @param x row coordinate in board 2D array
     * @param y column coordinate in board 2D array
     * @return int field value
     */
    public int get(int x, int y) {
        if (x >= 0 && x <= 8 && y >= 0 && y <= 8) {
            return board[x][y].getFieldValue();
        }
        return -1;
    }

    /**
     * Setter of concrete field value in board.
     * @param x row coordinate in board 2D array
     * @param y column coordinate in board 2D array
     * @param value value which should be set
     */
    public void set(int x, int y, int value) {
        if (x >= 0 && x <= 8 && y >= 0 && y <= 8 && value >= 0 && value <= 9) {
            (board[x][y]).setFieldValue(value);
        }
    }

    /**
     * Method which calls sudokuSolver method solve.
     */
    public void solveGame() {
        sudokuSolver.solve(this);
    }

    /**
     * Method checks unique of values in rows, boxes and columns.
     * @return boolean
     */
    private boolean checkBoard() {
        for (int i = 0; i < 9; i++) {
            if (!(rows.get(i).verify() && columns.get(i).verify() && boxes.get(i).verify())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method returns SudokuRow with concrete coordinate.
     * @param y row coordinate
     * @return SudokuRow
     */
    public SudokuRow getRow(int y) {
        return new SudokuRow(rows.get(y));
    }

    /**
     * Getter of SudokuColumn with concrete coordinate.
     * @param x column coordinate
     * @return SudokuColumn
     */
    public SudokuColumn getColumn(int x) {
        return new SudokuColumn(columns.get(x));
    }

    /**
     * Getter of SudokuBox with concrete coordinates.
     * @param x row coordinate
     * @param y column coordinate
     * @return SudokuBox
     */
    public SudokuBox getBox(int x, int y) {
        return new SudokuBox(boxes.get(x * 3 + y));
    }

    /**
     * Method checks if a value can be inserted into a given coordinates.
     * @param row row coordinate
     * @param col column coordinate
     * @param generated tested value
     * @return boolean
     */
    public boolean check(int row, int col, int generated) {
        return !getListOfIntegersFromSudokuGroup(rows.get(row)).contains(generated)
                && !getListOfIntegersFromSudokuGroup(columns.get(col)).contains(generated)
                && !getListOfIntegersFromSudokuGroup(getBox(row / 3,col / 3)).contains(generated);
    }

    /**
     * Method returns List of SudokuGroup SudokuFields values.
     * @param group SudokuGroup which values should be returned
     * @return List of Integers
     */
    private List<Integer> getListOfIntegersFromSudokuGroup(SudokuGroup group) {
        return Arrays.stream(group.getFields()).boxed().collect(Collectors.toList());
    }
}
