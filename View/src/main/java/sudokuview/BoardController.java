package sudokuview;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.adapter.JavaBeanIntegerProperty;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanLongPropertyBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import sudoku.SudokuBoard;
import sudoku.SudokuField;

public class BoardController {
    @FXML
    private VBox board;

    public class SudokuFieldAdapter {
        private SudokuBoard sudokuBoard;
        private SimpleIntegerProperty value = new SimpleIntegerProperty();
        private int xx;
        private int yy;

        public SudokuFieldAdapter(SudokuBoard board, int x, int y) {
            this.sudokuBoard = board;
            this.value.set(board.get(x, y));
            this.xx = x;
            this.yy = y;
        }

        public int getValue() {
            return this.sudokuBoard.get(xx, yy);
        }

        public SimpleIntegerProperty valueProperty() {
            return value;
        }

        public void setValue(int value) {
            this.sudokuBoard.set(xx, yy, value);
            this.value.set(value);
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
                    IntegerProperty fieldProperty = JavaBeanIntegerPropertyBuilder.create()
                            .bean(fieldAdapter).name("value").build();
                    textField.textProperty().bindBidirectional(fieldProperty,
                            (StringConverter) new IntegerStringConverter());
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            }
        }
    }

    private void fieldListener(ObservableValue<? extends String> observable,
                               String oldValue, String newValue) {
        System.out.println(oldValue + " -> " + newValue);
    }

}
