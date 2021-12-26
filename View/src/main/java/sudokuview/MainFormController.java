package sudokuview;

import dao.Dao;
import dao.SudokuBoardDaoFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sudoku.SudokuBoard;
import sudoku.solver.BacktrackingSudokuSolver;

public class MainFormController implements Initializable {
    public ComboBox levelChoose;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        levelChoose.getItems().addAll(
                new ArrayList<>(EnumSet.allOf(Levels.Level.class))
        );
    }

    /**
     * Action after click the button.
     *
     * @param actionEvent action which executed event
     * @throws RuntimeException Runtime exception
     */
    public void levelGenerate(ActionEvent actionEvent) {
        try {
            ComboBox comboBox = (ComboBox) actionEvent.getSource();
            FXMLLoader board = new FXMLLoader(
                    getClass().getResource("/Board.fxml")
            );
            SudokuBoard modelSudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
            modelSudokuBoard.solveGame();
            Levels.Level.valueOf(
                    comboBox.getSelectionModel()
                            .getSelectedItem()
                            .toString()
            ).prepare(modelSudokuBoard);
            Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
            stage.setScene(new Scene(board.load()));
            stage.setTitle("TurboSudoku");
            ((BoardController) board.getController()).startGame(modelSudokuBoard);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadFromFile(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Start Game File");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Sudoku game save (*.bin)", "*.bin")
        );
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*"));
        File chosenFile = chooser.showOpenDialog(
                ((MenuItem) actionEvent.getSource()).getParentPopup()
                        .getScene()
                        .getWindow()
        );
        if (chosenFile == null) {
            return;
        }
        Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao(chosenFile.getAbsolutePath());
        SudokuBoard modelSudokuBoard = dao.read();
        try {
            FXMLLoader board = new FXMLLoader(
                    getClass().getResource("/Board.fxml")
            );
            MenuItem m = (MenuItem) actionEvent.getSource();
            while (m.getParentPopup() == null) {
                m = m.getParentMenu();
            }
            Stage stage = (Stage) m.getParentPopup().getOwnerWindow();
            stage.setScene(new Scene(board.load()));
            stage.setTitle("TurboSudoku");
            ((BoardController) board.getController()).startGame(modelSudokuBoard);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
