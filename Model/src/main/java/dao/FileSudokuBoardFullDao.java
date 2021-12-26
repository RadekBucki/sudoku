package dao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import sudoku.SudokuBoard;

public class FileSudokuBoardFullDao extends AbstractDaoDecorator<SudokuBoard> {

    SudokuBoard initialBoard;
    String path;

    public FileSudokuBoardFullDao(Dao<SudokuBoard> delegate, SudokuBoard initial, String path) {
        super(delegate);
        this.initialBoard = initial;
        this.path = path;
    }

    @Override
    public void write(SudokuBoard obj) {
        super.write(obj);
        try (
                ObjectOutputStream writer = new ObjectOutputStream(
                        new FileOutputStream(path)
                )
        ) {
            writer.writeObject(initialBoard);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
