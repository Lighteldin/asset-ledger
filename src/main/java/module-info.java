module me.jehn {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens me.jehn to javafx.fxml;
    exports me.jehn;
}
