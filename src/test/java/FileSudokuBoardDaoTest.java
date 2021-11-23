import dao.FileSudokuBoardDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sudoku.SudokuBoard;
import java.io.FileNotFoundException;
import sudoku.solver.BacktrackingSudokuSolver;
import static org.junit.jupiter.api.Assertions.*;

class FileSudokuBoardDaoTest {
    @SuppressWarnings("SimplifiableAssertion")
    @Test
    @DisplayName("importExportTest")
    void importExportTest() {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        FileSudokuBoardDao dao = new FileSudokuBoardDao("dao");
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
        FileSudokuBoardDao dao = new FileSudokuBoardDao("notexist");
        Exception exception = assertThrows(RuntimeException.class, dao::read);
        assertNotNull(exception);
    }

    @Test
    @DisplayName("Write exception test")
    void writeExceptionTest() {
        FileSudokuBoardDao dao = new FileSudokuBoardDao("?");
        Exception exception = assertThrows(
                RuntimeException.class,
                () -> dao.write(new SudokuBoard(new BacktrackingSudokuSolver()))
        );
        assertNotNull(exception);
    }
}