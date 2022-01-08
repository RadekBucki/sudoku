module ViewProject {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires ModelProject;
    requires slf4j.api;
    requires log4j.api;
    opens sudokuview;
}