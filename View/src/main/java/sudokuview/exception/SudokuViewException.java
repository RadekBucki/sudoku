package sudokuview.exception;

import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudokuview.AlertBox;

public class SudokuViewException extends Exception {
    private static Logger logger = LoggerFactory.getLogger(SudokuViewException.class);

    public SudokuViewException(String message, Throwable cause) {
        super(message, cause);
        logger.error(message);
        AlertBox.alertShow(
                "Error",
                message,
                Alert.AlertType.ERROR
        );
    }
}
