package dao;

import exceptions.ModelDaoReadException;
import exceptions.ModelDaoWriteException;
import exceptions.ModelException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import sudoku.SudokuBoard;

public class FileSudokuBoardDao implements Dao<SudokuBoard> {
    private String fileName;

    /**
     * Constructor.
     * @param fileName name of the file
     */
    public FileSudokuBoardDao(String fileName) {
        this.fileName = fileName;
        if (!fileName.endsWith(".bin")) {
            this.fileName += ".bin";
        }
    }

    /**
     * Method loading written SudokuBoard object.
     * @return SudokuBoard
     */
    @Override
    public SudokuBoard read() {
        SudokuBoard sudokuBoard;
        try (
                ObjectInputStream reader = new ObjectInputStream(
                        new FileInputStream(fileName)
                )
        ) {
            sudokuBoard = (SudokuBoard) reader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new ModelDaoReadException("dao.read.exception", e);
        }
        return sudokuBoard;
    }

    /**
     * Save SudokuBoard object to file.
     * @param obj object type SudokuBoard which should be saved to file
     */
    @Override
    public void write(SudokuBoard obj) {
        try (
                ObjectOutputStream writer = new ObjectOutputStream(
                        new FileOutputStream(fileName)
                )
        ) {
            writer.writeObject(obj);
        } catch (IOException e) {
            throw new ModelDaoWriteException("dao.write.exception", e);
        }
    }

    @Override
    public void close() throws ModelException {
    }
}
