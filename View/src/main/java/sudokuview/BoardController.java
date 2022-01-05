package sudokuview;

import dao.Dao;
import dao.FileSudokuBoardFullDao;
import dao.SudokuBoardDaoFactory;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sudoku.SudokuBoard;
import sudoku.solver.BacktrackingSudokuSolver;

public class BoardController extends FormController implements Initializable {
    private SudokuBoard initialBoard;
    private SudokuBoard modelBoard;
    @FXML
    private VBox board;

    /**
     * Method initialize controller with fxml file and bundle.
     * @param url URL of fxml file
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = resourceBundle;
    }

    public class SudokuFieldAdapter {
        private SudokuBoard sudokuBoard;
        private int xx;
        private int yy;

        public SudokuFieldAdapter(SudokuBoard board, int x, int y) {
            this.sudokuBoard = board;
            this.xx = x;
            this.yy = y;
        }

        public String getValue() {
            return String.valueOf(this.sudokuBoard.get(xx, yy));
        }

        public void setValue(String value) {
            if (value.equals("")) {
                value = "0";
            }
            if (value.length() == 1 && '0' <= value.charAt(0) && value.charAt(0) <= '9') {
                this.sudokuBoard.set(xx, yy, Integer.parseInt(value));
                System.out.println(this.sudokuBoard);
            }
        }
    }

    public void startGame(SudokuBoard modelSudokuBoard, SudokuBoard initSudokuBoard) {
        startGame(modelSudokuBoard);
        try {
            this.initialBoard = initSudokuBoard.clone();
            for (BoardIterator bi = new BoardIterator(board); bi.hasNext(); ) {
                TextField textField = bi.next();
                if (
                    this.initialBoard.get(bi.getRow(), bi.getCol()) == 0
                    && modelSudokuBoard.get(bi.getRow(), bi.getCol()) != 0
                ) {
                    textField.setDisable(false);
                }
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void startGame(SudokuBoard modelSudokuBoard) {
        try {
            this.modelBoard = modelSudokuBoard;
            for (BoardIterator bi = new BoardIterator(board); bi.hasNext(); ) {
                TextField textField = bi.next();
                int value = modelSudokuBoard.get(bi.getRow(), bi.getCol());
                if (value != 0) {
                    textField.setDisable(true);
                }
                textField.textProperty().addListener(this::fieldListener);
                SudokuFieldAdapter fieldAdapter = new SudokuFieldAdapter(
                        modelSudokuBoard, bi.getRow(), bi.getCol());
                StringProperty fieldProperty = JavaBeanStringPropertyBuilder.create()
                        .bean(fieldAdapter).name("value").build();
                textField.textProperty().bindBidirectional(fieldProperty);
            }
            initialBoard = modelSudokuBoard.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fieldListener(ObservableValue<? extends String> observable,
                               String oldValue, String newValue) {
        if (!newValue.isEmpty() && (newValue.length() > 1
                || '0' >= newValue.charAt(0)
                || newValue.charAt(0) > '9')
        ) {
            StringProperty stringProperty = (StringProperty) observable;
            stringProperty.setValue(oldValue);
        }
    }

    public void saveToFile(ActionEvent actionEvent) {
        String filePath =
                FileChoose.saveChooser(bundle.getString("current.game.save.file"), actionEvent);
        if (filePath.isEmpty()) {
            return;
        }
        String filePathInitial =
                FileChoose.saveChooser(bundle.getString("initial.game.save.file"), actionEvent);
        if (filePathInitial.isEmpty()) {
            return;
        }
        try (
                Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao(filePath);
                Dao<SudokuBoard> daoDecorator = new FileSudokuBoardFullDao(
                        dao, initialBoard, filePathInitial)
        ) {
            SudokuBoard boardToSave = new SudokuBoard(new BacktrackingSudokuSolver());
            for (BoardIterator bi = new BoardIterator(board); bi.hasNext(); ) {
                String textFieldText = bi.next().getText();
                int val = 0;
                if (!textFieldText.isEmpty()) {
                    val = Integer.parseInt(textFieldText);
                }
                boardToSave.set(bi.getRow(), bi.getCol(), val);
            }
            save(daoDecorator, boardToSave);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(ActionEvent actionEvent) {
        String path =
                FileChoose.openChooser(bundle.getString("current.game.load.file"), actionEvent);
        if (path.isEmpty()) {
            return;
        }
        String pathInit =
                FileChoose.openChooser(bundle.getString("initial.game.load.file"), actionEvent);
        if (pathInit.isEmpty()) {
            return;
        }
        try (
                Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao(path);
                Dao<SudokuBoard> daoInit = SudokuBoardDaoFactory.getFileDao(pathInit)
        ) {
            SudokuBoard boardFromFile = dao.read();
            SudokuBoard boardFromFileInit = daoInit.read();
            initialBoard = boardFromFileInit.clone();
            for (BoardIterator bi = new BoardIterator(board); bi.hasNext(); ) {
                TextField textField = bi.next();
                textField.setDisable(false);
                if (boardFromFile.get(bi.getRow(), bi.getCol()) == 0) {
                    textField.setText("");
                } else {
                    textField.setText(String.valueOf(boardFromFile.get(bi.getRow(), bi.getCol())));
                }
                if (
                        boardFromFileInit.get(bi.getRow(), bi.getCol())
                        == boardFromFile.get(bi.getRow(), bi.getCol())
                        && boardFromFileInit.get(bi.getRow(), bi.getCol()) != 0
                ) {
                    textField.setDisable(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetBoard(ActionEvent actionEvent) {
        for (BoardIterator bi = new BoardIterator(board); bi.hasNext(); ) {
            TextField textField = bi.next();
            if (initialBoard.get(bi.getRow(), bi.getCol()) == 0) {
                textField.setText("");
            } else {
                textField.setText(String.valueOf(initialBoard.get(bi.getRow(), bi.getCol())));
            }
        }
    }

    private void save(Dao<SudokuBoard> dao, SudokuBoard board) {
        dao.write(board);
    }

    /**
     * Method changes language to choosen.
     * @param actionEvent ActionEvent
     */
    public void setLanguage(ActionEvent actionEvent) {
        bundle = ResourceBundle.getBundle(
                "Language",
                new Locale(
                        ((MenuItem) actionEvent.getSource()).getId()
                )
        );
        Stage stage = (Stage) ((MenuItem) actionEvent.getSource())
                                                     .getParentPopup()
                                                     .getOwnerWindow();
        FXMLLoader board = FxmlLoad.load(stage, "/Board.fxml", bundle);
        this.board = (VBox) stage.getScene().lookup("#board");
        ((BoardController) board.getController()).startGame(modelBoard, initialBoard);
    }
}
