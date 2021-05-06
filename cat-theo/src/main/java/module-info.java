module app {
    requires javafx.controls;
    requires javafx.fxml;

    opens app to javafx.fxml, javafx.controls;
    opens app.controllers to javafx.fxml, javafx.controls;
    exports app;
}