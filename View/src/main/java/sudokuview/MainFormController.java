package sudokuview;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sudoku.SudokuBoard;
import sudoku.solver.BacktrackingSudokuSolver;

public class MainFormController {
    /**
     * Action after click the button.
     *
     * @param actionEvent action which executed event
     */
    public void levelGenerate(ActionEvent actionEvent) {
        try {
            Button button = (Button) actionEvent.getSource();
            FXMLLoader board = new FXMLLoader(
                    getClass().getResource("/Board.fxml")
            );
            SudokuBoard modelSudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
            modelSudokuBoard.solveGame();
            Levels.Level.valueOf(button.getId()).prepare(modelSudokuBoard);
            Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
            stage.setScene(new Scene(board.load()));
            stage.setTitle("TurboSudoku");
            ((BoardController)board.getController()).startGame(modelSudokuBoard);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveToFile(ActionEvent actionEvent) {
    }

    public void loadToFile(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sudoku game save (*.bin)", "*.bin"));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*"));
        chooser.showOpenDialog(((MenuItem) actionEvent.getSource()).getParentPopup().getScene().getWindow());
    }
}
