module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;

    opens app to javafx.fxml, javafx.controls;
    opens app.controllers to javafx.fxml, javafx.controls;
    opens app.GUI to javafx.fxml, javafx.controls;
    exports app;
}