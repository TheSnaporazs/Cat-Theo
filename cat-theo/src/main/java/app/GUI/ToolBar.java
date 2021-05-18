package app.GUI;

import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ToolBar {

    public static void updateToolBar(ObjectGUI obj, TextField NameField) {
        NameField.setText(obj.getObject().getName());
    }
}
