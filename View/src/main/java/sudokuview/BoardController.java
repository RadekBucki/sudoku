package sudokuview;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sudoku.SudokuBoard;
import sudoku.solver.BacktrackingSudokuSolver;

public class BoardController implements Initializable {
    private String level;

    public enum Level {
        easy(15),
        medium(25),
        hard(30);

        int fieldsToRemove;

        /**
         * Constructor.
         * @param i number of fields to remove
         */
        Level(int i) {
            this.fieldsToRemove = i;
        }

        /**
         * Methods prepare board to game by removing some fields values.
         * @param board which will be prepared
         */
        void prepare(SudokuBoard board) {
            Random generator = new Random();
            boolean changed;
            for (int i = 0; i < fieldsToRemove; i++) {
                do {
                    changed = false;
                    int x = generator.nextInt(0, 9);
                    int y = generator.nextInt(0, 9);
                    if (board.get(x, y) != 0) {
                        board.set(x, y, 0);
                        changed = true;
                    }
                } while (!changed);
            }
        }
    }

    /**
     * Controller constructor.
     * @param level of the game
     */
    public BoardController(String level) {
        this.level = level;
    }

    @FXML
    private VBox board;

    /**
     * Method executed on load.
     * @param url link to fxml file
     * @param resourceBundle package with regional settings
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SudokuBoard modelSudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        modelSudokuBoard.solveGame();
        Level.valueOf(level).prepare(modelSudokuBoard);
        for (int i = 0; i < board.getChildren().size(); i++) {
            HBox row = (HBox) board.getChildren().get(i);
            for (int j = 0; j < row.getChildren().size(); j++) {
                TextField textField = (TextField) row.getChildren().get(j);
                int value = modelSudokuBoard.get(i, j);
                if (value != 0) {
                    textField.setText(String.valueOf(value));
                    textField.setDisable(true);
                }
            }
        }
    }
}
