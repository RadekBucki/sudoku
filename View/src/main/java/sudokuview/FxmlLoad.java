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
        FXMLLoader board = new FXMLLoader(
                FxmlLoad.class.getResource(path),
                bundle
        );
        stage.setScene(new Scene(board.load()));
        return board;
    }
}
