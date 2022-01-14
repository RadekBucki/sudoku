package sudokuview.controller;

import exceptions.ModelioException;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import sudoku.SudokuBoard;
import sudokuview.FxmlLoad;
import sudokuview.exception.LoadFromDatabaseException;
import sudokuview.exception.SudokuViewException;

public class LoadFromDatabaseController extends DatabaseController implements Initializable {
    private Stage mainWindowStage;

    @FXML
    public ComboBox<String> comboBox;

    public LoadFromDatabaseController() throws SudokuViewException {
    }

    /**
     * Method initialize controller with fxml file and bundle.
     * @param url URL of fxml file
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        comboBox.getItems().addAll(
                database.allSudokuBoardsSaved()
        );
    }

    /**
     * Setter for calling window stage.
     * @param mainWindowStage Stage
     */
    public void setMainWindowStage(Stage mainWindowStage) {
        this.mainWindowStage = mainWindowStage;
    }

    /**
     * Methods load and open selected Board.
     * @param actionEvent ActionEvent
     */
    public void loadBoard(ActionEvent actionEvent) {
        try {
            FXMLLoader board = FxmlLoad.loadNewWindow(
                    "/Board.fxml",
                    bundle.getString("title"),
                    bundle
            );

            Iterator<SudokuBoard> boardsIterator = database.read(
                    comboBox.getSelectionModel().getSelectedItem()
            ).iterator();

            SudokuBoard currentBoard = boardsIterator.next();
            SudokuBoard initBoard = boardsIterator.next();
            ((BoardController) board.getController()).startGame(currentBoard, initBoard);

            if (mainWindowStage != null) {
                mainWindowStage.close();
            }

            Stage stage = (Stage) ((ComboBox<?>)actionEvent.getTarget()).getScene().getWindow();
            stage.close();
        } catch (ModelioException e) {
            new LoadFromDatabaseException(bundle.getString(e.getMessage()), e);
        } catch (IOException e) {
            new LoadFromDatabaseException(bundle.getString("databasewrite.exception"), e);
        }
    }
}
