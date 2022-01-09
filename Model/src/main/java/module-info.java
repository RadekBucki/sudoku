open module ModelProject {
    requires java.desktop;
    requires com.google.common;
    requires slf4j.api;
    exports sudoku;
    exports sudoku.solver;
    exports dao;
    exports exceptions;
}