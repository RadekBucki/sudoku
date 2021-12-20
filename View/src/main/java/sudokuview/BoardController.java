package sudokuview;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sudoku.SudokuBoard;

public class BoardController {
    @FXML
    private VBox board;

    public void startGame(SudokuBoard modelSudokuBoard) {
        for (int i = 0; i < board.getChildren().size(); i++) {
            HBox row = (HBox) board.getChildren().get(i);
            for (int j = 0; j < row.getChildren().size(); j++) {
                TextField textField = (TextField) row.getChildren().get(j);
                int value = modelSudokuBoard.get(i, j);
                if (value != 0) {
                    textField.setText(String.valueOf(value));
                    textField.setDisable(true);
                }
                textField.textProperty().addListener(this::fieldListener);
            }
        }
    }

    private void fieldListener(ObservableValue<? extends String> observable,
                               String oldValue, String newValue) {
        System.out.println(oldValue + " -> " + newValue);
    }

}
