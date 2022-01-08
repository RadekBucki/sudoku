package sudoku;

import exceptions.ModelCloneNotSupportedException;

public class Repository {
    SudokuBoard sudokuBoard;

    public Repository(SudokuBoard sudokuBoard) {
        this.sudokuBoard = sudokuBoard;
    }

    public SudokuBoard createInstance() throws CloneNotSupportedException {
        try {
            return sudokuBoard.clone();
        } catch (CloneNotSupportedException e) {
            throw new ModelCloneNotSupportedException("clone.exception");
        }
    }
}
