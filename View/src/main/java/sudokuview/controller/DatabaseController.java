package sudokuview.controller;

import dao.JdbcSudokuBoardDao;
import exceptions.ModelioException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import sudokuview.exception.SudokuViewException;

public class DatabaseController implements Initializable {
    protected ResourceBundle bundle;
    protected JdbcSudokuBoardDao database;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
    }

    public DatabaseController() throws SudokuViewException {
        try {
            database = new JdbcSudokuBoardDao("viewDatabase");
        } catch (ModelioException e) {
            throw new SudokuViewException(bundle.getString(e.getMessage()), e);
        }
    }
}
