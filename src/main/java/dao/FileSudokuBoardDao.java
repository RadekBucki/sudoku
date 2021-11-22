package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import sudoku.SudokuBoard;

public class FileSudokuBoardDao implements Dao<SudokuBoard> {
    private String fileName;

    public FileSudokuBoardDao(String fileName) {
        this.fileName = fileName + ".ser";
    }

    @Override
    public SudokuBoard read() throws IOException, ClassNotFoundException {
        File f = new File(fileName);
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream reader = new ObjectInputStream(fis);
        return (SudokuBoard) reader.readObject();
    }

    @Override
    public void write(SudokuBoard obj) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream writer = new ObjectOutputStream(fos);
        writer.writeObject(obj);
        writer.close();
        fos.close();
    }
}
