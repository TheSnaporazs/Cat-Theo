package app.GUI;

import app.GUI.Bindings.LabelBinding;
import app.GUI.Bindings.LineCollision;
import app.GUI.Bindings.TrigBounding;
import app.GUI.Bindings.VEC;
import app.categories.Arrow;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;


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
public class ArrGUI extends Group {

    private ObjectGUI src;
    private ObjectGUI trg;
    private Arrow arrow;
    private Pane parent;

    //graphical components
    private Line line;
    private Label nameGUI;


    /**
     *  Initializes an ArrGUI upon this pane
     *
     * @param source    Source Obj of the Arrow
     * @param target    Source Obj of the Target
     * @param arrow     Arrow model reference of ArrowGUI
     * @param parent    Parent Pane
     */
    public ArrGUI(ObjectGUI source, ObjectGUI target, Arrow arrow, Pane parent) {

        super();
        this.src = source;
        this.trg = target;

        this.arrow = arrow;
        arrow.guiRepr = this;
        this.parent = parent;

        initGraphics();
        bindEndpoints();

    }

    private void initGraphics()
    {
        nameGUI = new Label(arrow.getName());
        nameGUI.setFont(new Font(15));
        this.line = new Line();
        this.getChildren().addAll(line, nameGUI);
    }


    public void removeArrGui()
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
        line.startXProperty().bind(new TrigBounding(src, trg, VEC.Ax));
        line.startYProperty().bind(new TrigBounding(src, trg, VEC.Ay));

        //Bind end to target
        line.endXProperty().bind(new TrigBounding(trg, src, VEC.Ax));
        line.endYProperty().bind(new TrigBounding(trg, src, VEC.Ay));
        line.visibleProperty().bind(new LineCollision(src, trg));

        nameGUI.layoutXProperty().bind(new LabelBinding(line, VEC.Ax));
        nameGUI.layoutYProperty().bind(new LabelBinding(line, VEC.Ay));
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
