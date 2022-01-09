import dao.Dao;
import dao.FileSudokuBoardDao;
import dao.SudokuBoardDaoFactory;
import exceptions.ModelDaoReadException;
import exceptions.ModelDaoWriteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sudoku.SudokuBoard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import sudoku.solver.BacktrackingSudokuSolver;
import sudoku.solver.SudokuSolver;

import static org.junit.jupiter.api.Assertions.*;

class FileSudokuBoardDaoTest {

    private SudokuBoard board;

    @BeforeEach
    void setUp() {
        board = new SudokuBoard(new BacktrackingSudokuSolver());
    }

    @SuppressWarnings("SimplifiableAssertion")
    @Test
    @DisplayName("importExportTest")
    void importExportTest() {
        SudokuBoard importedBoard = null;
        try(Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao("dao")) {
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
        try(Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao("notexist")) {
            Exception exception = assertThrows(ModelDaoReadException.class, dao::read);
            assertNotNull(exception);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Write test not closed exception")
    void writeTestNotClosedException() {
        try(Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao("test")) {
            try (RandomAccessFile file = new RandomAccessFile("test.bin", "rw")) {
                file.getChannel().lock();
                assertThrows(ModelDaoReadException.class, () -> dao.read());
                assertThrows(ModelDaoWriteException.class, () -> dao.write(board));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Write exception test")
    void writeExceptionTest() {
        try(Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao("?")) {
            Exception exception = assertThrows(
                    ModelDaoWriteException.class,
                    () -> dao.write(board)
            );
            assertNotNull(exception);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("constructorTest")
    void constructorTest() {
        try(Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao("testWithExtension.bin")) {
            assertTrue(dao instanceof FileSudokuBoardDao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}