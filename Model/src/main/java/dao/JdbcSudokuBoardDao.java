package dao;

import exceptions.ModelioException;
import sudoku.SudokuBoard;

public class JdbcSudokuBoardDao implements Dao<SudokuBoard> {
    private String dbName;

    public JdbcSudokuBoardDao(String dbName) {
        this.dbName = dbName;
    }

    /**
     * Read T object from file.
     *
     * @return T
     */
    @Override
    public SudokuBoard read() {
        return null;
    }

    /**
     * Write (save) T object to file.
     *
     * @param obj object type T which should be saved to file
     */
    @Override
    public void write(SudokuBoard obj) {
        dbName = "";
    }

    @Override
    public void close() throws ModelioException {

    }

    public void betterFinalize() throws Throwable {

    }
}
