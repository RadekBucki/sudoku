package sudokuview;

import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

abstract class FormController {
    protected ResourceBundle bundle;
    protected FxmlLoad fxmlLoad = new FxmlLoad();

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
