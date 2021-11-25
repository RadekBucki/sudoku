package dao;

import sudoku.SudokuBoard;

public class SudokuBoardDaoFactory {
    /**
     * Method to create and return SudokuBoardDao instance.
     * @param filename name of file to read and write SudokuBoard object.
     * @return SudokuBoardDao
     */
    public Dao<SudokuBoard> getFileDao(String filename) {
        return new FileSudokuBoardDao(filename);
    }
}
