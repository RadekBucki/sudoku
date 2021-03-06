open module ModelProject {
    requires java.desktop;
    requires com.google.common;
    requires slf4j.api;
    requires java.sql;
    requires derby;
    exports sudoku;
    exports sudoku.solver;
    exports dao;
    exports exceptions;
    exports sudoku.group;
}