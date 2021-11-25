package dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import sudoku.SudokuBoard;

public class FileSudokuBoardDao implements Dao<SudokuBoard> {
    private String fileName;
    ObjectOutputStream writer;
    ObjectInputStream reader;

    public FileSudokuBoardDao(String fileName) {
        this.fileName = fileName + ".bin";
    }

    @Override
    public SudokuBoard read() {
        SudokuBoard sudokuBoard;
        try {
            reader = new ObjectInputStream(
                    new FileInputStream(fileName)
            );
            sudokuBoard = (SudokuBoard) reader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return sudokuBoard;
    }

    @Override
    public void write(SudokuBoard obj) {
        try {
            writer = new ObjectOutputStream(
                    new FileOutputStream(fileName)
            );
            writer.writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        if (writer != null) {
            writer.close();
        }
        if (reader != null) {
            reader.close();
        }
    }
}
