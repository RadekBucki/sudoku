package dao;

import sudoku.SudokuBoard;
import sudoku.SudokuField;
import sudoku.solver.SudokuSolver;

import java.io.*;
import java.util.Scanner;

public class FileSudokuBoardDao implements Dao<SudokuBoard> {
    private String fileName;
    private SudokuSolver solver;

    public FileSudokuBoardDao(String fileName, SudokuSolver solver) {
        this.fileName = fileName + ".txt";
        this.solver = solver;
    }

    @Override
    public SudokuBoard read() throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        int boardIndex = 0;
        SudokuField[][] board = new SudokuField[9][9];
        Scanner reader = new Scanner(file).useDelimiter(" ");
        while (reader.hasNext()) {
            int value = Integer.parseInt(reader.next());
            board[boardIndex / 9][boardIndex % 9] = new SudokuField(value);
            boardIndex++;
        }
        reader.close();
        return new SudokuBoard(board, solver);
    }

    @Override
    public void write(SudokuBoard obj) throws IOException {
        File file = new File(fileName);
        FileWriter save = new FileWriter(fileName);
        save.write("");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                save.append(" " + obj.get(i, j));
            }
        }
        save.close();
    }
}
