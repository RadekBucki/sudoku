package sudokuview;

import java.io.IOException;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxmlLoad {
    /**
     * Method returns FXMLLoader.
     * @param stage Stage
     * @param path String
     * @param bundle ResourceBundle
     * @return FXMLLoader
     * @throws IOException exception
     */
    public static FXMLLoader load(Stage stage, String path, ResourceBundle bundle)
            throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                FxmlLoad.class.getResource(path),
                bundle
        );
        stage.setScene(new Scene(fxmlLoader.load()));
        return fxmlLoader;
    }

    /**
     * Method returns FXMLLoader for new window.
     * @param path String
     * @param bundle ResourceBundle
     * @param title String
     * @return FXMLLoader
     * @throws IOException exception
     */
    public static FXMLLoader loadNewWindow(String path, String title, ResourceBundle bundle)
            throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                FxmlLoad.class.getResource(path),
                bundle
        );
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle(title);
        stage.show();
        return fxmlLoader;
    }
}
