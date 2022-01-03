package sudokuview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.ResourceBundle;
import sudoku.SudokuBoard;

public class Levels {
    public enum Level {
        easy(15),
        medium(25),
        hard(35),
        turbo(45);

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

    /**
     * Method returns list of Levels.
     * @param resourceBundle Resource bundle
     * @return ArrayList
     */
    public ArrayList<String> getLevelFormName(ResourceBundle resourceBundle) {
        Level[] list = Level.values();
        ArrayList<String> arrayList = new ArrayList<>();
        for (Level item : list) {
            arrayList.add(resourceBundle.getString(item.name()));
        }
        return arrayList;
    }

    /**
     * Method returns Level from name.
     * @param name String
     * @param resourceBundle Resource bundle
     * @return Level
     */
    public Level getEnumFromLevelName(String name, ResourceBundle resourceBundle) {
        Iterator<String> iterator = resourceBundle.getKeys().asIterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (resourceBundle.getString(key).equals(name)) {
                return Level.valueOf(key);
            }
        }
        return null;
    }
}
