package sudokuview;

import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sudoku.SudokuBoard;

public class BoardController {
    @FXML
    private VBox board;

    public class SudokuFieldAdapter {
        private SudokuBoard sudokuBoard;
        private int xx;
        private int yy;

        public SudokuFieldAdapter(SudokuBoard board, int x, int y) {
            this.sudokuBoard = board;
            this.xx = x;
            this.yy = y;
        }

        public String getValue() {
            return String.valueOf(this.sudokuBoard.get(xx, yy));
        }

        public void setValue(String value) {
            if (value.equals("")) {
                value = "0";
            }
            if (value.length() == 1
                    && '0' <= value.charAt(0)
                    && value.charAt(0) <= '9') {
                this.sudokuBoard.set(xx, yy, Integer.parseInt(value));
                System.out.println(this.sudokuBoard);
            }
        }
    }

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
                try {
                    SudokuFieldAdapter fieldAdapter =
                            new SudokuFieldAdapter(modelSudokuBoard, i, j);
                    StringProperty fieldProperty = JavaBeanStringPropertyBuilder.create()
                            .bean(fieldAdapter).name("value").build();
                    textField.textProperty().bindBidirectional(fieldProperty);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            }
        }
    }

    private void fieldListener(ObservableValue<? extends String> observable,
                               String oldValue, String newValue) {
        if (!newValue.isEmpty() && (newValue.length() > 1
                || '0' >= newValue.charAt(0)
                || newValue.charAt(0) > '9')) {
            StringProperty stringProperty = (StringProperty) observable;
            stringProperty.setValue(oldValue);
        }
    }

}
