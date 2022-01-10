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
    JdbcSudokuBoardDao dao;
    SudokuBoard board;
    SudokuBoard boardCurrent;
    @BeforeEach
    void setUp() throws CloneNotSupportedException {
        dao = (JdbcSudokuBoardDao)SudokuBoardDaoFactory.getJdbcDao("testDatabase");
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
    }

    @Test
    @DisplayName("Jdbc check if not binary test")
    void jdbcCheckIfNotBinaryTest() throws SQLException {
        String urlConnection = "jdbc:derby:./target/testDatabase";
        Connection con = DriverManager.getConnection(urlConnection);
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM boards "
                + "WHERE boardName='testTable'");
        if (rs.next()) {
            assertEquals("1",rs.getString("id"));
            assertEquals("testTable",rs.getString("boardName"));
        }
        rs = statement.executeQuery("SELECT * FROM fields");
        if (rs.next()) {
            assertEquals("1",rs.getString("id"));
            assertEquals("1",rs.getString("boardId"));
            assertEquals("0",rs.getString("x"));
            assertEquals("0",rs.getString("y"));
            assertEquals(String.valueOf(board.get(0,0)),rs.getString("fvalue"));
            assertEquals(String.valueOf(true),rs.getString("disabled"));
        }
    }

    @Test
    @DisplayName("Write negative test")
    void writeNegativeTest() {
        assertThrows(ModelDaoWriteException.class,() -> dao.write(boardCurrent,board,"testTable"));
    }

    @Test
    @DisplayName("Read negative test")
    void readNegativeTest() {
        assertThrows(ModelDaoReadException.class,() -> dao.read("notExist"));
    }

    @Test
    @DisplayName("createDatabaseException")
    void createDatabaseException() {
        assertThrows(ModelioException.class,() -> SudokuBoardDaoFactory.getJdbcDao("//?//"));
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