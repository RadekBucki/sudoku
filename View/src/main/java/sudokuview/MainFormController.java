package sudokuview;

import dao.Dao;
import dao.SudokuBoardDaoFactory;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import sudoku.SudokuBoard;
import sudoku.solver.BacktrackingSudokuSolver;

public class MainFormController extends FormController implements Initializable {
    @FXML
    private ComboBox<String> levelChoose;

    /**
     * Method initialize controller with fxml file and bundle.
     * @param url URL of fxml file
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        levelChoose.getItems().addAll(
            Levels.getLevelFormName(resourceBundle)
        );
    }

    /**
     * Action after click the button.
     * @param actionEvent action which executed event
     * @throws RuntimeException Runtime exception
     */
    public void levelGenerate(ActionEvent actionEvent) {
        ComboBox comboBox = (ComboBox) actionEvent.getSource();
        Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
        final FXMLLoader board = FxmlLoad.load(stage, "/Board.fxml",bundle);
        SudokuBoard modelSudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        modelSudokuBoard.solveGame();
        Levels.getEnumFromLevelName(
            comboBox.getSelectionModel()
                    .getSelectedItem()
                    .toString(),
            bundle
        ).prepare(modelSudokuBoard);
        stage.setTitle("TurboSudoku");
        ((BoardController) board.getController()).startGame(modelSudokuBoard);
    }

    public void loadFromFile(ActionEvent actionEvent) {
        String path =
                FileChoose.openChooser(bundle.getString("current.game.load.file"), actionEvent);
        if (path.isEmpty()) {
            return;
        }
        String pathInit =
                FileChoose.openChooser(bundle.getString("initial.game.load.file"), actionEvent);
        if (pathInit.isEmpty()) {
            return;
        }
        try (Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao(path);
             Dao<SudokuBoard> daoInit = SudokuBoardDaoFactory.getFileDao(pathInit)
        ) {
            final SudokuBoard modelSudokuBoard = dao.read();
            final SudokuBoard initSudokuBoard = daoInit.read();
            MenuItem m = (MenuItem) actionEvent.getSource();
            while (m.getParentPopup() == null) {
                m = m.getParentMenu();
            }
            Stage stage = (Stage) m.getParentPopup().getOwnerWindow();
            FXMLLoader board = FxmlLoad.load(stage, "/Board.fxml",bundle);
            stage.setTitle("TurboSudoku");
            ((BoardController) board.getController()).startGame(
                    modelSudokuBoard,
                    initSudokuBoard
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method changes language to choosen.
     * @param actionEvent ActionEvent
     */
    public void setLanguage(ActionEvent actionEvent) {
        bundle = ResourceBundle.getBundle(
            "Language",
            new Locale(
                ((MenuItem) actionEvent.getSource()).getId()
            )
        );
        Stage stage = (Stage) ((MenuItem) actionEvent.getSource())
                                                     .getParentPopup()
                                                     .getOwnerWindow();
        FxmlLoad.load(stage,"/MainForm.fxml",bundle);
    }
}
