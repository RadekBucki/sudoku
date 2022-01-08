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
     */
    public static FXMLLoader load(Stage stage, String path, ResourceBundle bundle) {
        FXMLLoader board = new FXMLLoader(
                FxmlLoad.class.getResource(path),
                bundle
        );
        try {
            stage.setScene(new Scene(board.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return board;
    }
}
