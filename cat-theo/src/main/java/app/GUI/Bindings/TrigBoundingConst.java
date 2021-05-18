package app.GUI.Bindings;

import app.GUI.GUIutil;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Node;

public class TrigBoundingConst extends DoubleBinding {

    double magnitude;
    double angle;
    Node A;
    Node B;


    public enum TRIG {
        Ax,
        Ay,
    }

    private final TrigBounding.TRIG computation;

    public TrigBoundingConst(double magnitude, double angle, Node a, Node b, TrigBounding.TRIG computation) {
        this.magnitude = magnitude;
        this.angle = angle;
        A = a;
        B = b;
        this.computation = computation;
        super.bind(A.layoutXProperty(), A.layoutYProperty(), B.layoutXProperty(), B.layoutYProperty());
    }

    @Override
    protected double computeValue() {
        switch (computation) {
            case Ax -> {
                return A.getLayoutX() + magnitude * (1 + Math.cos(Math.toRadians(angle)));
            }
            case Ay -> {
                return A.getLayoutY() + magnitude * (1 + Math.sin(Math.toRadians(angle)));
            }

            default -> {
                //This shouldn't happen at all! (but if it happens it will let you notice it!)
                return 0.0d;
            }

        }
    }
}
