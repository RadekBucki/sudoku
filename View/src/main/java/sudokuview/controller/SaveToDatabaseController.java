package sudokuview.controller;

import exceptions.ModelioException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sudoku.SudokuBoard;
import sudokuview.AlertBox;
import sudokuview.exception.SaveToDatabaseException;
import sudokuview.exception.SudokuViewException;

public class SaveToDatabaseController extends DatabaseController implements Initializable {
    private SudokuBoard currentBoard;
    private SudokuBoard initBoard;

    @FXML
    public TextField name;

    public SaveToDatabaseController() throws SudokuViewException {
    }

    /**
     * Method initialize controller with fxml file and bundle.
     * @param url URL of fxml file
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        name.textProperty().addListener(this::nameListener);
    }

    /**
     * Validation for board name.
     * @param observable ObservableValue
     * @param oldValue String
     * @param newValue String
     */
    private void nameListener(ObservableValue<? extends String> observable,
                              String oldValue, String newValue) {
        if (newValue.length() > 255) {
            StringProperty stringProperty = (StringProperty) observable;
            AlertBox.alertShow(
                    bundle.getString("wrong.value"),
                    bundle.getString("name.must.have.less.than.255.chars"),
                    Alert.AlertType.WARNING
            );
            stringProperty.setValue(oldValue);
        }
    }

    /**
     * Method set current and init board.
     * @param currentBoard SudokuBoard
     * @param initBoard SudokuBoard
     */
    public void setBoards(SudokuBoard currentBoard, SudokuBoard initBoard) {
        this.currentBoard = currentBoard;
        this.initBoard = initBoard;
    }

    /**
     * Method saves board to database with chosen name.
     * @param actionEvent ActionEvent
     */
    public void save(ActionEvent actionEvent) {
        String boardName = name.getText();
        if (boardName.isEmpty()) {
            AlertBox.alertShow(
                    bundle.getString("wrong.value"),
                    bundle.getString("name.must.be.filled"),
                    Alert.AlertType.WARNING
            );
            return;
        }
        try {
            database.write(currentBoard, initBoard, boardName);
        } catch (ModelioException e) {
            new SaveToDatabaseException(bundle.getString(e.getMessage()), e);
        }
        Stage stage = (Stage) ((Button)actionEvent.getTarget()).getScene().getWindow();
        stage.close();
    }
}
