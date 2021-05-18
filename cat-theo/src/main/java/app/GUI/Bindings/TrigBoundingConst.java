package app.GUI.Bindings;

import app.GUI.GUIutil;
import app.GUI.ObjectGUI;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Node;
import javafx.scene.shape.Line;

public class TrigBoundingConst extends DoubleBinding {

    double magnitude;
    double angle;
    Line line;

    ObjectGUI A;
    ObjectGUI B;

    private final VEC computation;

    public TrigBoundingConst(ObjectGUI A, ObjectGUI B,
                             Line line, double magnitude, double angle, VEC computation) {
        this.A = A;
        this.B = B;
        this.magnitude = magnitude;
        this.angle = angle;
        this.line = line;
        this.computation = computation;
        super.bind(line.endXProperty(), line.endYProperty());
    }

    @Override
    protected double computeValue() {
        switch (computation) {
            case Ax -> {
                return line.getEndX() + magnitude * Math.cos(GUIutil.computeAngle(A, B)+Math.toRadians(angle));
            }
            case Ay -> {
                return line.getEndY() + magnitude * Math.sin(GUIutil.computeAngle(A, B)+Math.toRadians(angle));
            }

            default -> {
                //This shouldn't happen at all! (but if it happens it will let you notice it!)
                return 0.0d;
            }

        }
    }
}
