package dao;

import sudoku.SudokuBoard;

public class SudokuBoardDaoFactory {
    public Dao<SudokuBoard> getFileDao(String filename) {
        return new FileSudokuBoardDao(filename);
    }
}
