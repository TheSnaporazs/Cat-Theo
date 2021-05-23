package app.GUI;

import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class ToolBarArr {
    public static void updateToolBArr(ArrGUI arr, TextField NameField, TextField SourceField, TextField TargetField, RadioButton Mor, RadioButton Epi, RadioButton Iso, RadioButton Mono) {
        NameField.setEditable(true);
        NameField.setText(arr.getArrow().getName());
        SourceField.setText(arr.getSrc().getObject().getName());
        TargetField.setText(arr.getTrg().getObject().getName());
        

        if (arr.getArrow().isIsomorphism()) {
            Iso.fire();
        }
        if (arr.getArrow().isEpic() & !arr.getArrow().isMonic()) {
            Epi.fire();
        }
        if (arr.getArrow().isMonic() & !arr.getArrow().isEpic()) {
            Epi.fire();
        }
        else {
            Mor.fire();
        }


    }
}
