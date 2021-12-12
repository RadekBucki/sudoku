package sudoku;

public class Repository {
    SudokuBoard sudokuBoard;

    public Repository(SudokuBoard sudokuBoard) {
        this.sudokuBoard = sudokuBoard;
    }

    public SudokuBoard createInstance() throws CloneNotSupportedException {
        return sudokuBoard.clone();
    }
}
