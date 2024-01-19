module at.htlsteyr.minesweeper {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.htlsteyr.minesweeper to javafx.fxml;
    exports at.htlsteyr.minesweeper;
}