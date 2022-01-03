package sudokuview;

import dao.Dao;
import dao.SudokuBoardDaoFactory;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sudoku.SudokuBoard;
import sudoku.solver.BacktrackingSudokuSolver;

public class MainFormController implements Initializable {
    private FileChoose fileChoose = new FileChoose();
    private FXMLLoad fxmlLoad = new FXMLLoad();
    private ResourceBundle bundle = ResourceBundle.getBundle("Language", new Locale("pl"));

    @FXML
    private ComboBox<Levels.Level> levelChoose;
    public Text levelChooseText;
    public Menu file;
    public MenuItem save;
    public MenuItem load;
    public MenuItem authors;
    public Menu language;
    public Menu about;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        levelChoose.getItems().addAll(
                new ArrayList<>(EnumSet.allOf(Levels.Level.class))
        );
    }

    /**
     * Action after click the button.
     *
     * @param actionEvent action which executed event
     * @throws RuntimeException Runtime exception
     */
    public void levelGenerate(ActionEvent actionEvent) {
        ComboBox comboBox = (ComboBox) actionEvent.getSource();
        Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
        FXMLLoader board = fxmlLoad.load(stage,"/Board.fxml",bundle);
        SudokuBoard modelSudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        modelSudokuBoard.solveGame();
        Levels.Level.valueOf(
                comboBox.getSelectionModel()
                        .getSelectedItem()
                        .toString()
        ).prepare(modelSudokuBoard);
        stage.setTitle("TurboSudoku");
        ((BoardController) board.getController()).startGame(modelSudokuBoard);
    }

    public void loadFromFile(ActionEvent actionEvent) {
        String path = fileChoose.openChooser("Start current game file", actionEvent);
        if (path.isEmpty()) {
            return;
        }
        try (Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao(path)) {
            SudokuBoard modelSudokuBoard = dao.read();
            String pathInit = fileChoose.openChooser("Start initial game file", actionEvent);
            if (pathInit.isEmpty()) {
                return;
            }
            try (Dao<SudokuBoard> daoInit = SudokuBoardDaoFactory.getFileDao(pathInit)) {
                SudokuBoard initSudokuBoard = daoInit.read();
                MenuItem m = (MenuItem) actionEvent.getSource();
                while (m.getParentPopup() == null) {
                    m = m.getParentMenu();
                }
                Stage stage = (Stage) m.getParentPopup().getOwnerWindow();
                FXMLLoader board = fxmlLoad.load(stage,"/Board.fxml",bundle);
                stage.setTitle("TurboSudoku");
                ((BoardController) board.getController()).startGame(
                    modelSudokuBoard,
                    initSudokuBoard
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLanguage(ActionEvent actionEvent) {
        bundle = ResourceBundle.getBundle(
            "Language",
            new Locale(
                ((MenuItem) actionEvent.getSource()).getId()
            )
        );
        Stage stage = (Stage) ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
        fxmlLoad.load(stage,"/MainForm.fxml",bundle);
    }

    public void getAuthors(ActionEvent actionEvent) {
        ResourceBundle listBundle = ResourceBundle.getBundle("authors.Authors_" + bundle.getLocale().getLanguage());
        StringBuilder authors = new StringBuilder();
        Iterator<String> keyIterator = listBundle.getKeys().asIterator();
        while(keyIterator.hasNext()) {
            authors.append(listBundle.getObject(keyIterator.next()))
                   .append("\n");
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString("authors"));
        alert.setHeaderText(bundle.getString("authors"));
        alert.setContentText(authors.toString());
        alert.showAndWait();
    }
}
