package app.GUI.Bindings;


import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.TextField;
import java.util.ArrayList;

/**
 * Binding to allow the pressing of an ok button only if all the text
 * fields of a prompt have been compiled, thereby blocking the user from
 * sending invalid data to the controller.
 *
 * @author Dario
 * @since 12/06/2021
 * @see BooleanBinding
 * @see app.GUI.GUIutil
 */
public class FieldsBounding extends BooleanBinding {


    private final ArrayList<TextField> fields;

    public FieldsBounding(ArrayList<TextField> fields)
    {
        fields.forEach(textField -> {
            bind(textField.textProperty());
        });

        this.fields = fields;
    }

    @Override
    protected boolean computeValue() {
        System.out.println("Computing!");
        System.out.println(fields.stream().noneMatch(field -> field.getText() == ""));
        return !fields.stream().noneMatch(field -> field.getText() == "");
    }
}
