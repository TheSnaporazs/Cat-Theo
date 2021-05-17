package app.GUI.Bindings;

import app.GUI.GUIutil;
import app.GUI.ObjectGUI;
import javafx.beans.binding.DoubleBinding;

/**
 *  TrigBounding.java
 *
 *  Bounds two circular objects A and B's coordinates by adding an offset to produce a coordinate on the perimeter of A
 *  The coordinate to be bound is dependent on the formula selected on the enumerator
 *  Ax bounds the X coordinate (using cosine)
 *  Ay bounds the Y coordinate (using sin)
 *
 * @author  Dario Loi
 * @since   17/05/2021
 *
 * @see app.GUI.ObjectGUI
 * @see app.GUI.ArrGUI
 * @see DoubleBinding
 */
public class TrigBounding extends DoubleBinding {

    ObjectGUI A;
    ObjectGUI B;

    public enum TRIG {
        Ax,
        Ay
    }

    private final TRIG computation;

    public TrigBounding(ObjectGUI a, ObjectGUI b, TRIG computation) {
        A = a;
        B = b;
        super.bind(A.layoutXProperty(), A.layoutYProperty(), B.layoutXProperty(), B.layoutYProperty());
        this.computation = computation;
    }

    @Override
    protected double computeValue() {
        switch(computation)
        {
            case Ax -> {
                return A.getLayoutX() + A.getRay() * (1 + Math.cos(GUIutil.computeAngle(A, B)));
            }
            case Ay -> {
                return A.getLayoutY() + A.getRay() * (1 + Math.sin(GUIutil.computeAngle(A, B)));
            }
            default -> {
                //This shouldn't happen at all! (but if it happens it will let you notice it!)
                return 0.0d;
            }

        }

    }
}
