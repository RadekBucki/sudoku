package sudokuview;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class FileChoose {
    /**
     * Method opens dialog to choose location to save.
     * @param windowTitle String
     * @param actionEvent ActionEvent
     * @return String
     */
    public String saveChooser(String windowTitle, ActionEvent actionEvent) {
        String choose = null;
        try {
            choose = choose(
                    windowTitle, actionEvent,
                    FileChooser.class.getMethod("showSaveDialog", Window.class)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return choose;
    }

    /**
     * Method opens dialog to choose file to open.
     * @param windowTitle String
     * @param actionEvent ActionEvent
     * @return String
     */
    public String openChooser(String windowTitle, ActionEvent actionEvent) {
        String choose = null;
        try {
            choose = choose(
                    windowTitle, actionEvent,
                    FileChooser.class.getMethod("showOpenDialog", Window.class)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return choose;
    }

    /**
     * Method opens given type of dialog.
     * @param windowTitle String
     * @param actionEvent ActionEvent
     * @return String
     */
    private String choose(String windowTitle, ActionEvent actionEvent, Method showDialog)
            throws InvocationTargetException, IllegalAccessException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(windowTitle);
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
        return chosenFile.getAbsolutePath();
    }
}
