package sudokuview;

import dao.Dao;
import dao.FileSudokuBoardFullDao;
import dao.SudokuBoardDaoFactory;

import java.io.File;

import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import sudoku.SudokuBoard;
import sudoku.solver.BacktrackingSudokuSolver;

public class BoardController {
    @FXML
    private VBox board;
    private SudokuBoard initialBoard;

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
            if (value.length() == 1
                    && '0' <= value.charAt(0)
                    && value.charAt(0) <= '9') {
                this.sudokuBoard.set(xx, yy, Integer.parseInt(value));
                System.out.println(this.sudokuBoard);
            }
        }
    }

    public void startGame(SudokuBoard modelSudokuBoard, SudokuBoard initSudokuBoard) {
        startGame(modelSudokuBoard);
        try {
            this.initialBoard = initSudokuBoard.clone();
            for (int i = 0; i < board.getChildren().size(); i++) {
                HBox row = (HBox) board.getChildren().get(i);
                for (int j = 0; j < row.getChildren().size(); j++) {
                    TextField textField = (TextField) row.getChildren().get(j);
                    if (this.initialBoard.get(i, j) == 0 && modelSudokuBoard.get(i, j) != 0) {
                        textField.setDisable(false);
                    }
                }
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void startGame(SudokuBoard modelSudokuBoard) {
        for (int i = 0; i < board.getChildren().size(); i++) {
            HBox row = (HBox) board.getChildren().get(i);
            for (int j = 0; j < row.getChildren().size(); j++) {
                TextField textField = (TextField) row.getChildren().get(j);
                int value = modelSudokuBoard.get(i, j);
                if (value != 0) {
                    textField.setDisable(true);
                }
                textField.textProperty().addListener(this::fieldListener);
                try {
                    SudokuFieldAdapter fieldAdapter =
                            new SudokuFieldAdapter(modelSudokuBoard, i, j);
                    StringProperty fieldProperty = JavaBeanStringPropertyBuilder.create()
                            .bean(fieldAdapter).name("value").build();
                    textField.textProperty().bindBidirectional(fieldProperty);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            }
        }
        try {
            initialBoard = modelSudokuBoard.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void fieldListener(ObservableValue<? extends String> observable,
                               String oldValue, String newValue) {
        if (!newValue.isEmpty() && (newValue.length() > 1
                || '0' >= newValue.charAt(0)
                || newValue.charAt(0) > '9')) {
            StringProperty stringProperty = (StringProperty) observable;
            stringProperty.setValue(oldValue);
        }
    }

    public void saveToFile(ActionEvent actionEvent) {
        String filePath = saveChooser("Save board to file", actionEvent);
        if (filePath.equals("")) {
            return;
        }
        try (Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao(filePath)) {
            SudokuBoard boardToSave = new SudokuBoard(new BacktrackingSudokuSolver());
            for (int i = 0; i < board.getChildren().size(); i++) {
                HBox row = (HBox) board.getChildren().get(i);
                for (int j = 0; j < row.getChildren().size(); j++) {
                    TextField textField = (TextField) row.getChildren().get(j);
                    if (textField.getText().equals("")) {
                        int val = 0;
                        boardToSave.set(i, j, val);
                    } else {
                        int val = Integer.parseInt(textField.getText());
                        boardToSave.set(i, j, val);
                    }
                }
            }
            String filePathInitial = saveChooser("Save initial board to file", actionEvent);
            if (filePathInitial.equals("")) {
                return;
            }
            try (Dao<SudokuBoard> daoDecorator = new FileSudokuBoardFullDao(dao,
                    initialBoard, filePathInitial)) {
                save(daoDecorator, boardToSave);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(ActionEvent actionEvent) {
        String path = openChooser("Open current board state file", actionEvent);
        if(path.equals("")){
            return;
        }
        String pathInit = openChooser("Open initial board state file", actionEvent);
        if(pathInit.equals("")){
            return;
        }
        try (Dao<SudokuBoard> dao = SudokuBoardDaoFactory.getFileDao(path)) {
            SudokuBoard boardFromFile = dao.read();
            try (Dao<SudokuBoard> daoInit = SudokuBoardDaoFactory.getFileDao(pathInit)) {
                SudokuBoard boardFromFileInit = daoInit.read();
                initialBoard = boardFromFileInit.clone();
                for (int i = 0; i < board.getChildren().size(); i++) {
                    HBox row = (HBox) board.getChildren().get(i);
                    for (int j = 0; j < row.getChildren().size(); j++) {
                        TextField textField = (TextField) row.getChildren().get(j);
                        if (boardFromFileInit.get(i, j) == boardFromFile.get(i, j) && boardFromFile.get(i, j) != 0) {
                            textField.setText(String.valueOf(boardFromFileInit.get(i, j)));
                            textField.setDisable(true);
                        } else {
                            if (boardFromFile.get(i, j) != 0) {
                                textField.setText(String.valueOf(boardFromFile.get(i, j)));
                            } else {
                                textField.setText("");
                            }
                            textField.setDisable(false);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetBoard(ActionEvent actionEvent) {
        for (int i = 0; i < board.getChildren().size(); i++) {
            HBox row = (HBox) board.getChildren().get(i);
            for (int j = 0; j < row.getChildren().size(); j++) {
                TextField textField = (TextField) row.getChildren().get(j);
                if (initialBoard.get(i, j) == 0) {
                    textField.setText("");
                } else {
                    textField.setText(String.valueOf(initialBoard.get(i, j)));
                }
            }
        }
    }

    private String saveChooser(String windowTitle, ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(windowTitle);
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Sudoku game save (*.bin)", "*.bin")
        );
        File chosenFile = chooser.showSaveDialog(
                ((MenuItem) actionEvent.getSource()).getParentPopup()
                        .getScene()
                        .getWindow()
        );
        if (chosenFile == null) {
            return "";
        }
        return chosenFile.getAbsolutePath();
    }

    private String openChooser(String windowTitle, ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(windowTitle);
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Sudoku game save (*.bin)", "*.bin")
        );
        File chosenFile = chooser.showOpenDialog(
                ((MenuItem) actionEvent.getSource()).getParentPopup()
                        .getScene()
                        .getWindow()
        );
        if (chosenFile == null) {
            return "";
        }
        return chosenFile.getAbsolutePath();
    }

    private void save(Dao<SudokuBoard> dao, SudokuBoard board) {
        dao.write(board);
    }
}
