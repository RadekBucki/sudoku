package sudokuview;

import dao.Dao;
import dao.SudokuBoardDaoFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import sudoku.SudokuBoard;
import sudoku.solver.BacktrackingSudokuSolver;

public class MainFormController implements Initializable {
    private FileChoose fileChoose = new FileChoose();
    @FXML
    private ComboBox<Levels.Level> levelChoose;

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
        String path = fileChoose.openChooser("Start current game file", actionEvent);
        if (path.isEmpty()) {
            return;
        }
        try (Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao(path)) {
            SudokuBoard modelSudokuBoard = dao.read();
            String pathInit = fileChoose.openChooser("Start initial game file", actionEvent);
            if (pathInit.isEmpty()) {
                return;
            }
            try (Dao<SudokuBoard> daoInit = SudokuBoardDaoFactory.getFileDao(pathInit)) {
                SudokuBoard initSudokuBoard = daoInit.read();
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
                    ((BoardController) board.getController()).startGame(
                        modelSudokuBoard,
                        initSudokuBoard
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
