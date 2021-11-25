import dao.Dao;
import dao.FileSudokuBoardDao;
import dao.SudokuBoardDaoFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sudoku.SudokuBoard;

import java.io.FileNotFoundException;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import sudoku.solver.BacktrackingSudokuSolver;

import static org.junit.jupiter.api.Assertions.*;

class FileSudokuBoardDaoTest {

    private SudokuBoardDaoFactory factory;
    private SudokuBoard board;

    @BeforeEach
    void setUp() {
        factory = new SudokuBoardDaoFactory();
        board = new SudokuBoard(new BacktrackingSudokuSolver());
    }

    @SuppressWarnings("SimplifiableAssertion")
    @Test
    @DisplayName("importExportTest")
    void importExportTest() {
        Dao<SudokuBoard> dao = factory.getFileDao("dao");
        SudokuBoard importedBoard = null;
        try {
            dao.write(board);
            importedBoard = dao.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(board.equals(importedBoard));
    }

    @Test
    @DisplayName("Write exception Test")
    void readExceptionTest() {
        Dao<SudokuBoard> dao = factory.getFileDao("notexist");
        Exception exception = assertThrows(RuntimeException.class, dao::read);
        assertNotNull(exception);
    }

    @Test
    @DisplayName("Write exception test")
    void writeExceptionTest() {
        Dao<SudokuBoard> dao = factory.getFileDao("?");
        Exception exception = assertThrows(
                RuntimeException.class,
                () -> dao.write(board)
        );
        assertNotNull(exception);
    }
}