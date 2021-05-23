package app.GUI;

import app.controllers.WorkController;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ToolBar {

    public static void updateToolBar(ObjectGUI obj, TextField NameField, TextField XField, TextField YField) {
        NameField.setEditable(true);
        NameField.setText(obj.getObject().getName());
        XField.setText(obj.getxCord());
        YField.setText(obj.getyCord());

    }
}
