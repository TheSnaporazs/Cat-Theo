package app.GUI;

import app.GUI.Bindings.TrigBounding;
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

    /**
     * Binds the arrow's start and end to the X and Y properties of the source and target object
     *
     * @see app.GUI.Bindings.TrigBounding
     */
    private void bindEndpoints()
    {
        //Bind start to source
        this.startXProperty().bind(new TrigBounding(src, trg, TrigBounding.TRIG.Ax));
        this.startYProperty().bind(new TrigBounding(src, trg, TrigBounding.TRIG.Ay));

        //Bind end to target
        this.endXProperty().bind(new TrigBounding(trg, src, TrigBounding.TRIG.Ax));
        this.endYProperty().bind(new TrigBounding(trg, src, TrigBounding.TRIG.Ay));

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
