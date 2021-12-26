package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sudoku.SudokuBoard;
import sudoku.solver.BacktrackingSudokuSolver;

import static org.junit.jupiter.api.Assertions.*;

class DaoDecoratorTest {
    SudokuBoard board1;
    SudokuBoard board2;
    @BeforeEach
    void setUp() {
        board1 = new SudokuBoard(new BacktrackingSudokuSolver());
        board1.solveGame();
        board2 = new SudokuBoard(new BacktrackingSudokuSolver());
        board2.solveGame();
    }
    @Test
    @DisplayName("DaoDecoratorTest")
    void daoDecoratorTest() {
        try(Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao("test")) {
            try(Dao<SudokuBoard> daoDecorator = new FileSudokuBoardFullDao(dao,board1,"testInitial.bin")) {
                daoDecorator.write(board2);
                SudokuBoard imported = daoDecorator.read();
                assertTrue(board2.equals(imported));
                Dao<SudokuBoard> daoInit = SudokuBoardDaoFactory.getFileDao("testInitial");
                SudokuBoard imported2 = daoInit.read();
                assertTrue(board1.equals(imported2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("DaoDecoratorWriteException")
    void daoDecoratorWriteException() {
        try(Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao("test")) {
            try(Dao<SudokuBoard> daoDecorator = new FileSudokuBoardFullDao(dao,board1,"?")) {
                Exception exception = assertThrows(
                        RuntimeException.class,
                        () -> daoDecorator.write(board1)
                );
                assertNotNull(exception);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}