package dao;

import sudoku.SudokuBoard;

import java.io.*;
import java.util.Scanner;

public class FileSudokuBoardDao implements Dao<SudokuBoard> {
    private String fileName;

    public FileSudokuBoardDao(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public SudokuBoard read() {
        return null;
    }

    @Override
    public void write(SudokuBoard obj) throws IOException {
        File file = new File(fileName+".txt");
        FileWriter save = new FileWriter(fileName+".txt");
        save.write(obj.toString());
        save.close();
    }
}
