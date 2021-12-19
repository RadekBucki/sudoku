package sudokuview;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import sudoku.SudokuBoard;

import java.util.Random;

public class Levels {
    public enum Level {
        easy(15),
        medium(25),
        hard(35);

        int fieldsToRemove;

        /**
         * Constructor.
         *
         * @param i number of fields to remove
         */
        Level(int i) {
            this.fieldsToRemove = i;
        }

        /**
         * Methods prepare board to game by removing some fields values.
         *
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
}
