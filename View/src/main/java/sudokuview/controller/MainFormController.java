package sudokuview.controller;

import dao.Dao;
import dao.SudokuBoardDaoFactory;
import exceptions.ModelDaoReadException;
import java.io.IOException;
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
import sudokuview.FxmlLoad;
import sudokuview.Levels;
import sudokuview.exception.BoardLoadException;
import sudokuview.exception.LevelGenerateException;
import sudokuview.exception.SetLanguageException;

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
     * @throws LevelGenerateException exception
     */
    public void levelGenerate(ActionEvent actionEvent) {
        ComboBox comboBox = (ComboBox) actionEvent.getSource();
        Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
        FXMLLoader board = null;
        try {
            board = FxmlLoad.load(stage, "/Board.fxml",bundle);
        } catch (IOException e) {
            new LevelGenerateException(bundle.getString("level.generate.exception"), e);
        }
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
        String loadPath = null;
        String loadPathInit = null;
        try {
            loadPath = getOpenChooserPath(actionEvent, "current.game.load.file");
            if (loadPath.isEmpty()) {
                return;
            }
            loadPathInit = getOpenChooserPath(actionEvent, "initial.game.load.file");
            if (loadPathInit.isEmpty()) {
                return;
            }
        } catch (Exception e) {
            new BoardLoadException(bundle.getString("load.exception"), e);
        }
        try (Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao(loadPath);
             Dao<SudokuBoard> daoInit = SudokuBoardDaoFactory.getFileDao(loadPathInit)
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
        } catch (ModelDaoReadException e) {
            new BoardLoadException(bundle.getString(e.getMessage()), e);
        } catch (Exception e) {
            new BoardLoadException(bundle.getString("load.exception"), e);
        }
    }

    /**
     * Method changes language to choosen.
     * @param actionEvent ActionEvent
     * @throws SetLanguageException exception
     */
    public void setLanguage(ActionEvent actionEvent) {
        try {
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
        } catch (Exception e) {
            new SetLanguageException(bundle.getString("set.language.exception"), e);
        }
    }
}
