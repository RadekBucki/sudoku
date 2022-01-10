package dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sudoku.SudokuBoard;
import sudoku.solver.BacktrackingSudokuSolver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcSudokuBoardDaoTest {
    @Test
    @DisplayName("Jdbc write/read test")
    void jdbcWriteReadTest() throws CloneNotSupportedException {
        JdbcSudokuBoardDao dao = (JdbcSudokuBoardDao)SudokuBoardDaoFactory.getJdbcDao("testDatabase");
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        board.solveGame();
        board.set(0,0,0);
        board.set(1,1,0);
        board.set(2,2,0);
        board.set(3,3,0);
        SudokuBoard boardCurrent = board.clone();
        boardCurrent.set(3,3,1);
        boardCurrent.set(2,2,1);
        boardCurrent.set(1,1,1);
        dao.write(boardCurrent,board,"testTable");
        List<SudokuBoard> boards = dao.read("testTable");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assertEquals(board.get(i,j),boards.get(1).get(i,j));
                assertEquals(boardCurrent.get(i,j),boards.get(0).get(i,j));
            }
        }
    }
}