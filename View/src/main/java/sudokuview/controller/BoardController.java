package sudokuview.controller;

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
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.SudokuBoard;
import sudokuview.AlertBox;
import sudokuview.BoardIterator;
import sudokuview.FileChoose;
import sudokuview.FxmlLoad;
import sudokuview.exception.BoardLoadException;
import sudokuview.exception.BoardSaveException;
import sudokuview.exception.NewGameException;
import sudokuview.exception.SaveToDatabaseException;
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
        private static Logger logger = LoggerFactory.getLogger(SudokuFieldAdapter.class);

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
            if (value.length() == 1 && '0' <= value.charAt(0) && value.charAt(0) <= '9'
                    && Integer.parseInt(value) != this.sudokuBoard.get(xx, yy)) {
                logger.info("Sudoku board changed x=" + xx + " y=" + yy + " oldValue="
                        + this.sudokuBoard.get(xx, yy) + " newValue=" + value);
                this.sudokuBoard.set(xx, yy, Integer.parseInt(value));
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
        } catch (ModelCloneNotSupportedException e) {
            new StartGameException(bundle.getString(e.getMessage()), e);
        } catch (Exception e) {
            new StartGameException(bundle.getString("start.game.exception"), e);
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
        } catch (ModelCloneNotSupportedException e) {
            new StartGameException(bundle.getString(e.getMessage()), e);
        } catch (Exception e) {
            new StartGameException(bundle.getString("start.game.exception"), e);
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
            new BoardSaveException(bundle.getString("save.exception"), e);
        }
        try (
                Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao(filePath);
                Dao<SudokuBoard> daoDecorator = new FileSudokuBoardFullDao(
                        dao, initialBoard, filePathInitial)
        ) {
            daoDecorator.write(modelBoard);
        } catch (ModelDaoWriteException e) {
            new BoardSaveException(bundle.getString(e.getMessage()), e);
        } catch (Exception e) {
            new BoardSaveException(bundle.getString("save.exception"), e);
        }
    }

    public void loadFromFile(ActionEvent actionEvent) {
        String path = null;
        String pathInit = null;
        try {
            path = getOpenChooserPath(actionEvent, "current.game.load.file");
            if (path.isEmpty()) {
                return;
            }
            pathInit = getOpenChooserPath(actionEvent, "initial.game.load.file");
            if (pathInit.isEmpty()) {
                return;
            }
        } catch (Exception e) {
            new BoardLoadException(bundle.getString("load.exception"), e);
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
            new BoardLoadException(bundle.getString(e.getMessage()), e);
        } catch (Exception e) {
            new BoardLoadException(bundle.getString("load.exception"), e);
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
     *
     * @param actionEvent ActionEvent
     */
    public void setLanguage(ActionEvent actionEvent) {
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
            new SetLanguageException(bundle.getString("set.language.exception"), e);
        }
    }

    /**
     * Opens MainForm.
     *
     * @param actionEvent ActionEvent
     */
    public void newGame(ActionEvent actionEvent) {
        Stage stage = (Stage) ((MenuItem) actionEvent.getSource())
                                                     .getParentPopup()
                                                     .getOwnerWindow();
        try {
            FxmlLoad.load(stage, "/MainForm.fxml", bundle);
        } catch (IOException e) {
            new NewGameException(bundle.getString("new.game.exception"), e);
        }
    }

    /**
     * Method saves board to database.
     * @param actionEvent ActionEvent
     */
    public void saveToDatabase(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = FxmlLoad.loadNewWindow(
                    "/SaveToDatabase.fxml",
                    bundle.getString("choose.board.name.to.save"),
                    bundle
            );
            ((SaveToDatabaseController) fxmlLoader.getController())
                    .setBoards(modelBoard, initialBoard);
        } catch (IOException e) {
            new SaveToDatabaseException(bundle.getString("save.exception"), e);
        }
    }
}
