package sudokuview;

import java.io.IOException;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader main = new FXMLLoader(
                    getClass().getResource("/MainForm.fxml")
            );
            primaryStage.setTitle("Sudoku Game");
            primaryStage.setScene(new Scene(main.load()));
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}