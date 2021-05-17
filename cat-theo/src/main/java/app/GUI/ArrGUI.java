package app.GUI;

import app.categories.Arrow;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

/**
 *  ArrGUI.java
 *
 *  Child object of Line
 *  Contains an amount of properties relative to the graphical representation of the arrow itself,
 *  such as the graphical arrow connecting the two object nodes, it also contains methods for the trigonometrical
 *  computations that allow this arrow to stay on the borders of the objects nodes
 *
 * @author Dario Loi
 * @since 17/05/2021
 *
 * @see app.categories.Arrow
 * @see Line
 */
public class ArrGUI extends Line {

    private ObjectGUI src;
    private ObjectGUI trg;
    private Arrow arrow;
    private Pane parent;


    /**
     *  Initializes an ArrGUI upon this pane
     *
     * @param source    Source Obj of the Arrow
     * @param target    Source Obj of the Target
     * @param arrow     Arrow model reference of ArrowGUI
     * @param parent    Parent Pane
     */
    public ArrGUI(ObjectGUI source, ObjectGUI target, Arrow arrow, Pane parent) {

        this.src = source;
        this.trg = target;

        this.arrow = arrow;
        this.parent = parent;
        bindEndpoints();

    }


    private void removeArrGui()
    {
        this.parent.getChildren().remove(this);
    }

    private void bindEndpoints()
    {
        DoubleBinding xSrc = new DoubleBinding() {
            {
                // Specify the dependencies with super.bind()
                super.bind(src.layoutXProperty(), trg.layoutYProperty(), trg.layoutXProperty(), trg.layoutYProperty());
            }
            @Override
            protected double computeValue() {
                // Return the computed value
                return src.getLayoutX() + 30 + src.getRay() * Math.cos(GUIutil.computeAngle(src, trg));
            }
        };

        this.startXProperty().bind(xSrc);

        DoubleBinding ySrc = new DoubleBinding() {
            {
                // Specify the dependencies with super.bind()
                super.bind(src.layoutXProperty(), trg.layoutYProperty(), trg.layoutXProperty(), trg.layoutYProperty());
            }
            @Override
            protected double computeValue() {
                // Return the computed value
                return src.getLayoutY() + 30 + src.getRay() * Math.sin(GUIutil.computeAngle(src, trg));
            }
        };

        this.startYProperty().bind(ySrc);

        DoubleBinding xTrg = new DoubleBinding() {
            {
                // Specify the dependencies with super.bind()
                super.bind(src.layoutXProperty(), trg.layoutYProperty(), trg.layoutXProperty(), trg.layoutYProperty());
            }
            @Override
            protected double computeValue() {
                // Return the computed value
                return trg.getLayoutX() + 30 + trg.getRay() * Math.cos(GUIutil.computeAngle(trg, src));
            }
        };

        this.endXProperty().bind(xTrg);

        DoubleBinding yTrg = new DoubleBinding() {
            {
                // Specify the dependencies with super.bind()
                super.bind(src.layoutXProperty(), trg.layoutYProperty(), trg.layoutXProperty(), trg.layoutYProperty());
            }
            @Override
            protected double computeValue() {
                // Return the computed value
                return trg.getLayoutY() + 30 + trg.getRay() * Math.sin(GUIutil.computeAngle(trg, src));
            }
        };

        this.endYProperty().bind(yTrg);

    }

    public ObjectGUI getSrc() {
        return src;
    }

    public ObjectGUI getTrg() {
        return trg;
    }

    public Arrow getArrow() {
        return arrow;
    }
}
