package sudokuview;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sudokuview.exception.ApplicationStartFailedException;

public class MainApplication extends Application {
    private String language = "pl";

    /**
     * Method starts first scene.
     * @param primaryStage first scene
     */
    @Override
    public void start(Stage primaryStage) throws ApplicationStartFailedException {
        Locale locale = new Locale(language);
        ResourceBundle bundle = ResourceBundle.getBundle("Language", locale);
        try {
            FXMLLoader main = new FXMLLoader(
                getClass().getResource("/MainForm.fxml"),
                bundle
            );
            primaryStage.setTitle(bundle.getString("title"));
            primaryStage.setScene(new Scene(main.load()));
            primaryStage.show();
        } catch (IOException e) {
            new ApplicationStartFailedException(bundle.getString("application.start.error"), e);
        }
    }

    /**
     * Main application method.
     * @param args arguments given with execution
     */
    public static void main(String[] args) {
        launch(args);
    }
}