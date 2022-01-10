package sudokuview.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.stage.Stage;
import sudokuview.AlertBox;
import sudokuview.FileChoose;
import sudokuview.FxmlLoad;
import sudokuview.exception.LoadFromDatabaseException;

abstract class FormController implements Initializable {
    protected ResourceBundle bundle;
    @FXML
    private Menu language;

    /**
     * Method initialize controller with fxml file and bundle.
     * @param url URL of fxml file
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = resourceBundle;
        for (MenuItem item : language.getItems()) {
            RadioMenuItem radio = (RadioMenuItem) item;
            radio.setSelected(false);
            if (radio.getId().equals(bundle.getLocale().getLanguage())) {
                radio.setSelected(true);
            }
        }
    }

    /**
     * Method opens the dialog with Authors.
     * @param actionEvent ActionEvent
     */
    public void getAuthors(ActionEvent actionEvent) {
        ResourceBundle listBundle = ResourceBundle.getBundle(
                "authors.Authors_" + bundle.getLocale().getLanguage()
        );
        StringBuilder authors = new StringBuilder();
        Iterator<String> keyIterator = listBundle.getKeys().asIterator();
        while (keyIterator.hasNext()) {
            authors.append(listBundle.getObject(keyIterator.next()))
                    .append("\n");
        }
        AlertBox.alertShow(
            bundle.getString("authors"),
            authors.toString(),
            Alert.AlertType.INFORMATION
        );
    }

    /**
     * Method returns Path from open chooser.
     * @param actionEvent ActionEvent
     * @param titleKey String
     * @return String
     * @throws InvocationTargetException Exception
     * @throws NoSuchMethodException Exception
     * @throws IllegalAccessException Exception
     */
    public String getOpenChooserPath(ActionEvent actionEvent, String titleKey)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return FileChoose.openChooser(
                bundle.getString(titleKey),
                actionEvent
        );
    }

    /**
     * Method load board from database.
     * @param actionEvent ActionEvent
     */
    public void loadFromDatabase(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = FxmlLoad.loadNewWindow(
                    "/LoadFromDatabase.fxml",
                    bundle.getString("choose.board.to.load"),
                    bundle
            );
            ((LoadFromDatabaseController) fxmlLoader.getController()).setMainWindowStage(
                    (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow()
            );
        } catch (IOException e) {
            new LoadFromDatabaseException(bundle.getString("load.exception"), e);
        }
    }
}
