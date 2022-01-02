package sudokuview;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class FileChoose {
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

    private String choose(String windowTitle, ActionEvent actionEvent, Method showSaveDialog)
            throws InvocationTargetException, IllegalAccessException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(windowTitle);
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Sudoku game save (*.bin)", "*.bin")
        );
        File chosenFile = (File) showSaveDialog.invoke(
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
