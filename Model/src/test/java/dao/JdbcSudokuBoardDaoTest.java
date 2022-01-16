package dao;

import exceptions.ModelDaoReadException;
import exceptions.ModelDaoWriteException;
import exceptions.ModelioException;
import org.junit.jupiter.api.*;
import sudoku.SudokuBoard;
import sudoku.solver.BacktrackingSudokuSolver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcSudokuBoardDaoTest {
    SudokuBoard board;
    SudokuBoard boardCurrent;
    @BeforeEach
    void setUp() throws CloneNotSupportedException {
        board = new SudokuBoard(new BacktrackingSudokuSolver());
        board.solveGame();
        board.set(0,0,0);
        board.set(1,1,0);
        board.set(2,2,0);
        board.set(3,3,0);
        boardCurrent = board.clone();
        boardCurrent.set(3,3,1);
        boardCurrent.set(2,2,1);
        boardCurrent.set(1,1,1);
    }

    @Test
    @DisplayName("Jdbc write/read test")
    void jdbcWriteReadTest() throws IOException {
        try(JdbcSudokuBoardDao dao = (JdbcSudokuBoardDao)SudokuBoardDaoFactory.getJdbcDao("testDatabase")){
            dao.write(boardCurrent,board,"testTable");
            List<SudokuBoard> boards = dao.read("testTable");
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    assertEquals(board.get(i,j),boards.get(1).get(i,j));
                    assertEquals(boardCurrent.get(i,j),boards.get(0).get(i,j));
                }
            }
            assertNotSame(boardCurrent,boards.get(0));
            assertNotSame(board,boards.get(1));
            assertTrue(dao.isConnectionClosed());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Jdbc check if not binary test")
    void jdbcCheckIfNotBinaryTest() throws SQLException {
        String urlConnection = "jdbc:derby:./target/testDatabase";
        try(Connection con = DriverManager.getConnection(urlConnection)){
            Statement statement = con.createStatement();
            try(ResultSet rs = statement.executeQuery("SELECT * FROM boards "
                    + "WHERE boardName='testTable'")){
                if (rs.next()) {
                    assertEquals("1",rs.getString("id"));
                    assertEquals("testTable",rs.getString("boardName"));
                }
            }
            try(ResultSet rs2 = statement.executeQuery("SELECT * FROM fields")){
                if (rs2.next()) {
                    assertEquals("1",rs2.getString("id"));
                    assertEquals("1",rs2.getString("boardId"));
                    assertEquals("0",rs2.getString("x"));
                    assertEquals("0",rs2.getString("y"));
                    assertEquals(String.valueOf(board.get(0,0)),rs2.getString("fvalue"));
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Write negative test")
    void writeNegativeTest() {
        try(JdbcSudokuBoardDao dao = (JdbcSudokuBoardDao)SudokuBoardDaoFactory.getJdbcDao("testDatabase")) {
            assertThrows(ModelDaoWriteException.class, () -> dao.write(boardCurrent, board, "testTable"));
            assertTrue(dao.isConnectionClosed());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Read negative test")
    void readNegativeTest() {
        try(JdbcSudokuBoardDao dao = (JdbcSudokuBoardDao)SudokuBoardDaoFactory.getJdbcDao("testDatabase")) {
            assertThrows(ModelDaoReadException.class,() -> dao.read("notExist"));
            assertTrue(dao.isConnectionClosed());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("createDatabaseException")
    void createDatabaseException() {
        assertThrows(ModelioException.class,() -> SudokuBoardDaoFactory.getJdbcDao("//?//"));
    }

    @Test
    @DisplayName("Connection blocked")
    void connectionNotEstablished() {
        try(JdbcSudokuBoardDao dao = (JdbcSudokuBoardDao)SudokuBoardDaoFactory.getJdbcDao("testDatabase")) {
            assertThrows(ModelioException.class, () -> dao.write(boardCurrent));
            assertThrows(ModelioException.class, dao::read);
            assertTrue(dao.isConnectionClosed());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("List all sudokuboard names test")
    void listAllSudokuboardNamesTest() {
        try(JdbcSudokuBoardDao dao = (JdbcSudokuBoardDao)SudokuBoardDaoFactory.getJdbcDao("testDatabase")) {
            assertEquals(1,dao.allSudokuBoardsSaved().size());
            dao.write(boardCurrent,board,"testTable2");
            assertEquals(2,dao.allSudokuBoardsSaved().size());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeAll
    static void clean() throws IOException {
        File file = new File("target/testDatabase");
        if(file.exists()){
            Path path = Paths.get("target/testDatabase");
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }
}