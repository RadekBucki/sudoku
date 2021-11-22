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
        File file = new File(fileName + ".txt");
        FileWriter save = new FileWriter(fileName + ".txt");
        save.write(Integer.toString(obj.get(0,0)));
        for (int i = 0; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                save.append(" "+obj.get(i,j));
            }
        }
        save.close();
    }
}
