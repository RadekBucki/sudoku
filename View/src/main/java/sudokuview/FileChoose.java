package sudokuview;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class FileChoose {
    private static String lastUsedDir = "";

    /**
     * Method opens dialog to choose location to save.
     * @param windowTitle String
     * @param actionEvent ActionEvent
     * @return String
     * @throws NoSuchMethodException exception
     * @throws InvocationTargetException exception
     * @throws IllegalAccessException exception
     */
    public static String saveChooser(String windowTitle, ActionEvent actionEvent)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String choose = null;
        choose = choose(
                windowTitle, actionEvent,
                FileChooser.class.getMethod("showSaveDialog", Window.class)
        );
        return choose;
    }

    /**
     * Method opens dialog to choose file to open.
     * @param windowTitle String
     * @param actionEvent ActionEvent
     * @return String
     * @throws NoSuchMethodException exception
     * @throws InvocationTargetException exception
     * @throws IllegalAccessException exception
     */
    public static String openChooser(String windowTitle, ActionEvent actionEvent)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String choose = null;
        choose = choose(
                windowTitle, actionEvent,
                FileChooser.class.getMethod("showOpenDialog", Window.class)
        );
        return choose;
    }

    /**
     * Method opens given type of dialog.
     * @param windowTitle String
     * @param actionEvent ActionEvent
     * @return String
     * @throws InvocationTargetException exception
     * @throws IllegalAccessException exception
     */
    private static String choose(String windowTitle, ActionEvent actionEvent, Method showDialog)
            throws InvocationTargetException, IllegalAccessException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(windowTitle);
        if (!lastUsedDir.isEmpty()) {
            chooser.setInitialDirectory(new File(lastUsedDir));
        }
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Sudoku game save (*.bin)", "*.bin")
        );
        File chosenFile = (File) showDialog.invoke(
                chooser,
                ((MenuItem) actionEvent.getSource()).getParentPopup()
                        .getScene()
                        .getWindow()
        );
        if (chosenFile == null) {
            return "";
        }
        lastUsedDir = chosenFile.getAbsoluteFile().getParentFile().getAbsolutePath();
        return chosenFile.getAbsolutePath();
    }
}
