package sudokuview;

import dao.Dao;
import dao.FileSudokuBoardFullDao;
import dao.SudokuBoardDaoFactory;
import exceptions.ModelCloneNotSupportedException;
import exceptions.ModelDaoReadException;
import exceptions.ModelDaoWriteException;
import java.io.IOException;
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
import sudokuview.exception.BoardLoadException;
import sudokuview.exception.BoardSaveException;
import sudokuview.exception.NewGameException;
import sudokuview.exception.SetLanguageException;
import sudokuview.exception.StartGameException;

public class BoardController extends FormController implements Initializable {
    private SudokuBoard initialBoard;
    private SudokuBoard modelBoard;
    @FXML
    private VBox board;

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

    public void startGame(SudokuBoard modelSudokuBoard, SudokuBoard initSudokuBoard)
            throws StartGameException {
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
        } catch (ModelCloneNotSupportedException e) {
            throw new StartGameException(bundle.getString(e.getMessage()), e);
        } catch (Exception e) {
            throw new StartGameException(bundle.getString("start.game.exception"), e);
        }
    }

    public void startGame(SudokuBoard modelSudokuBoard) throws StartGameException {
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
        } catch (ModelCloneNotSupportedException e) {
            throw new StartGameException(bundle.getString(e.getMessage()), e);
        } catch (Exception e) {
            throw new StartGameException(bundle.getString("start.game.exception"), e);
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

    public void saveToFile(ActionEvent actionEvent) throws BoardSaveException {
        String filePath = null;
        String filePathInitial = null;
        try {
            filePath = FileChoose.saveChooser(
                    bundle.getString("current.game.save.file"),
                    actionEvent
            );
            if (filePath.isEmpty()) {
                return;
            }
            filePathInitial = FileChoose.saveChooser(
                    bundle.getString("initial.game.save.file"),
                    actionEvent
            );
            if (filePathInitial.isEmpty()) {
                return;
            }
        } catch (Exception e) {
            throw new BoardSaveException(bundle.getString("save.exception"), e);
        }
        try (
                Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao(filePath);
                Dao<SudokuBoard> daoDecorator = new FileSudokuBoardFullDao(
                        dao, initialBoard, filePathInitial)
        ) {
            daoDecorator.write(modelBoard);
        } catch (ModelDaoWriteException e) {
            throw new BoardSaveException(bundle.getString(e.getMessage()), e);
        } catch (Exception e) {
            throw new BoardSaveException(bundle.getString("save.exception"), e);
        }
    }

    public void loadFromFile(ActionEvent actionEvent) throws BoardLoadException {
        String path = null;
        String pathInit = null;
        try {
            path = getOpenChooserPath(actionEvent, "current.game.load.file");
            pathInit = getOpenChooserPath(actionEvent, "initial.game.load.file");
        } catch (Exception e) {
            throw new BoardLoadException(bundle.getString("load.exception"), e);
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
        } catch (ModelDaoReadException | ModelCloneNotSupportedException e) {
            throw new BoardLoadException(bundle.getString(e.getMessage()), e);
        } catch (Exception e) {
            throw new BoardLoadException(bundle.getString("load.exception"), e);
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

    /**
     * Method changes language to choosen.
     * @param actionEvent ActionEvent
     * @throws SetLanguageException exception
     */
    public void setLanguage(ActionEvent actionEvent)
            throws SetLanguageException {
        try {
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
        } catch (Exception e) {
            throw new SetLanguageException(bundle.getString("set.language.exception"), e);
        }
    }

    /**
     * Opens MainForm.
     * @param actionEvent ActionEvent
     * @throws NewGameException exception
     */
    public void newGame(ActionEvent actionEvent) throws NewGameException {
        Stage stage = (Stage) ((MenuItem) actionEvent.getSource())
                                                     .getParentPopup()
                                                     .getOwnerWindow();
        try {
            FxmlLoad.load(stage, "/MainForm.fxml", bundle);
        } catch (IOException e) {
            throw new NewGameException(bundle.getString("new.game.exception"), e);
        }
    }
}
