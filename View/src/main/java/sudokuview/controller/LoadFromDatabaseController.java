package sudokuview.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import sudoku.SudokuBoard;
import sudoku.solver.BacktrackingSudokuSolver;
import sudokuview.FxmlLoad;

public class LoadFromDatabaseController implements Initializable {
    private ResourceBundle bundle;
    private Stage mainWindowStage;

    @FXML
    public ComboBox comboBox;

    /**
     * Method initialize controller with fxml file and bundle.
     * @param url URL of fxml file
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;

        // section for add existing Board to load
        comboBox.getItems().addAll(
                (new ArrayList<>()).add("test")
        );
        // enf of section for add existing Board to load
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

            // load section

            SudokuBoard currentBoard = new SudokuBoard(new BacktrackingSudokuSolver());
            SudokuBoard initBoard = new SudokuBoard(new BacktrackingSudokuSolver());
            ((BoardController) board.getController()).startGame(currentBoard, initBoard);

            // end of load section

            if (mainWindowStage != null) {
                mainWindowStage.close();
            }

            Stage stage = (Stage) ((ComboBox)actionEvent.getTarget()).getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
