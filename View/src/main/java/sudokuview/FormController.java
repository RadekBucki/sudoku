package sudokuview;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;

abstract class FormController implements Initializable {
    protected ResourceBundle bundle;
    protected FxmlLoad fxmlLoad = new FxmlLoad();

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
        (new AlertBox()).alertShow(
            bundle.getString("authors"),
            authors.toString(),
            Alert.AlertType.INFORMATION
        );
    }
}
