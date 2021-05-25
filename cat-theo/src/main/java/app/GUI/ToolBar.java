package app.GUI;

import app.categories.Space;
import app.controllers.WorkController;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ToolBar {

    public static void updateToolBar(ObjectGUI obj, TextField NameField, TextField XField, TextField YField, ComboBox<String> combogg) {
        combogg.getItems().removeAll();
        NameField.setEditable(true);
        NameField.setText(obj.getObject().getName());
        XField.setText(Double.toString(obj.getLayoutX()));
        YField.setText(Double.toString(obj.getLayoutY()));

        combogg.getItems().add(obj.getObject().getDomain().getName());

        for (Space space: obj.getObject().getSubspaces()) {
            combogg.getItems().add(space.getName());
        }

    }
}
