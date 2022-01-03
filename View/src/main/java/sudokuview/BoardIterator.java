package sudokuview;

import java.util.Iterator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BoardIterator implements Iterator<TextField> {
    private VBox board;
    private int row = 0;
    private int col = -1;

    /**
     * Iterator constructor.
     * @param board Vbox to iterate
     */
    public BoardIterator(VBox board) {
        this.board = board;
    }

    /**
     * Method returns row number.
     * @return int row number
     */
    public int getRow() {
        return row;
    }

    /**
     * Method returns column number.
     * @return int column number
     */
    public int getCol() {
        return col;
    }

    /**
     * Method returns boolean status of existing next TextField in Vboard.
     * @return boolean
     */
    @Override
    public boolean hasNext() {
        return row < 9 && col + 1 < 9  || col + 1 == 9 && row < 8;
    }

    /**
     * Method returns next TextField.
     * @return TextField
     */
    @Override
    public TextField next() {
        if (hasNext()) {
            col++;
            if (col == 9 && row < 8) {
                col = 0;
                row++;
            }
            HBox rowBox = (HBox) board.getChildren().get(row);
            return (TextField) rowBox.getChildren().get(col);
        }
        return null;
    }
}
