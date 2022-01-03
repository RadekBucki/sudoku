package sudokuview;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class FXMLLoad {
    public FXMLLoader load(Stage stage, String path, ResourceBundle bundle){
        FXMLLoader board = new FXMLLoader(
                getClass().getResource(path),
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
