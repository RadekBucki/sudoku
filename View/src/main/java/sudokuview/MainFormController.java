package sudokuview;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainFormController {
    /**
     * Action after click the button.
     * @param actionEvent action which executed event
     */
    public void levelGenerate(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        loadBoard(button.getId());
    }

    /**
     * Method loads next scene.
     * @param level level of the board
     */
    private void loadBoard(String level) {
        try {
            FXMLLoader board = new FXMLLoader(
                    getClass().getResource("/Board.fxml")
            );
            board.setController(new BoardController(level));
            Stage stage = new Stage();
            stage.setScene(new Scene(board.load()));
            stage.setTitle("TurboSudoku");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
