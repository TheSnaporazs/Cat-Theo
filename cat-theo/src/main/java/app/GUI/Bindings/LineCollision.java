package app.GUI.Bindings;

import app.GUI.ObjectGUI;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.shape.Line;
@Deprecated
public class LineCollision extends BooleanBinding {

    Line line;
    ObjectGUI A;
    ObjectGUI B;

    public LineCollision(ObjectGUI a, ObjectGUI b) {
        A = a;
        B = b;

        super.bind(A.boundsInParentProperty(),
                B.boundsInParentProperty());
    }

    @Override
    protected boolean computeValue() { return !A.getBoundsInParent().intersects(B.getBoundsInParent()); }
}
