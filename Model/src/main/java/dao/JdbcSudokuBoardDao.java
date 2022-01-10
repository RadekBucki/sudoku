package dao;

import exceptions.ModelDaoReadException;
import exceptions.ModelDaoWriteException;
import exceptions.ModelDatabaseCreateException;
import exceptions.ModelioException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import sudoku.SudokuBoard;
import sudoku.solver.BacktrackingSudokuSolver;

public class JdbcSudokuBoardDao implements Dao<SudokuBoard> {
    private Statement statement;
    private String boardName;
    private SudokuBoard initialBoard;
    private Connection con;

    public JdbcSudokuBoardDao(String dbname) {
        createDatabase(dbname);
    }

    private void createDatabase(String dbname) {
        try {
            String urlConnection = "jdbc:derby:./target/" + dbname + ";create=true";
            con = DriverManager.getConnection(urlConnection);
            statement = con.createStatement();
            con.setAutoCommit(false);
        } catch (SQLException e) {
            throw new ModelDatabaseCreateException("databasecreate.exception", new Throwable());
        }
        try {
            execute("CREATE TABLE boards "
                    + "(id INT PRIMARY KEY GENERATED BY DEFAULT "
                    + "AS IDENTITY (START WITH 1, INCREMENT BY 1)"
                    + ",boardName VARCHAR(255))");
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        try {
            execute("CREATE TABLE fields "
                    + "(id INT PRIMARY KEY GENERATED BY DEFAULT "
                    + "AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "boardId INT REFERENCES boards(id),x INT,y INT,fvalue INT,disabled BOOLEAN)");
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public List<SudokuBoard> read(String boardName) {
        this.initialBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        this.boardName = boardName;
        ArrayList<SudokuBoard> list = new ArrayList<>();
        list.add(read());
        list.add(this.initialBoard);
        return list;
    }

    /**
     * Read T object from file.
     *
     * @return T
     */
    @Override
    public SudokuBoard read() {
        try {
            SudokuBoard databaseBoard = new SudokuBoard(new BacktrackingSudokuSolver());
            ResultSet rs = executeQuery("SELECT * FROM boards "
                    + "WHERE boardName='" + this.boardName + "'");
            String id = "";
            while (rs.next()) {
                if (rs.getString("boardName").equals(this.boardName)) {
                    id = rs.getString("id");
                }
            }
            if (id.equals("")) {
                throw new ModelDaoReadException("databasenoinfo.exception", new Throwable());
            }
            ResultSet rs2 = executeQuery("SELECT * FROM fields WHERE boardId=" + id);
            while (rs2.next()) {
                databaseBoard.set(Integer.parseInt(rs2.getString("x")),
                        Integer.parseInt(rs2.getString("y")),
                        Integer.parseInt(rs2.getString("fvalue")));
                if (rs2.getString("disabled").equals("true")) {
                    this.initialBoard.set(Integer.parseInt(rs2.getString("x")),
                            Integer.parseInt(rs2.getString("y")),
                            Integer.parseInt(rs2.getString("fvalue")));
                }
            }
            return databaseBoard;
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            throw new ModelDaoReadException("databaseread.exception", e);
        }
    }

    public void write(SudokuBoard current, SudokuBoard initial, String boardName) {
        this.boardName = boardName;
        this.initialBoard = initial;
        write(current);
    }

    /**
     * Write (save) T object to file.
     *
     * @param obj object type T which should be saved to file
     */
    @Override
    public void write(SudokuBoard obj) {
        try {
            String id = "";
            ResultSet exists = executeQuery("SELECT * FROM boards "
                    + "WHERE boardName='" + this.boardName + "'");
            if (exists.next()) {
                throw new ModelDaoWriteException("databasename.exception", new Throwable());
            }
            execute("INSERT INTO boards(boardName) VALUES ('" + this.boardName + "')");
            ResultSet rs = executeQuery("SELECT * FROM boards "
                    + "WHERE boardName='" + this.boardName + "'");
            if (rs.next()) {
                id = rs.getString("id");
            }
            int idNum = Integer.parseInt(id);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    execute("INSERT INTO fields(boardId,x,y,fvalue,disabled) VALUES "
                            + "(" + idNum + "," + i + "," + j + "," + obj.get(i, j) + ","
                            + (this.initialBoard.get(i, j) == obj.get(i, j)) + ")");
                }
            }
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            throw new ModelDaoWriteException("databasewrite.exception", e);
        }
    }

    @Override
    public void close() throws ModelioException {
    }

    private void execute(String query) throws SQLException {
        con.beginRequest();
        statement.execute(query);
        con.commit();
    }

    private ResultSet executeQuery(String query) throws SQLException {
        con.beginRequest();
        ResultSet rs = statement.executeQuery(query);
        con.commit();
        return rs;
    }
}
