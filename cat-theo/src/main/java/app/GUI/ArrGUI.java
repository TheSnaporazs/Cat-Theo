package app.GUI;

import app.categories.Arrow;
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
        arrow.guiRepr = this;
        this.parent = parent;

        bindEndpoints();
    }


    public void removeArrGui()
    {
        this.parent.getChildren().remove(this);
    }

    private void bindEndpoints()
    {
        this.startXProperty().bind(src.layoutXProperty().add(30 + src.getRay() * Math.cos(GUIutil.computeAngle(src, trg))));
        this.endXProperty().bind(trg.layoutXProperty().add(30 +trg.getRay() * Math.cos(GUIutil.computeAngle(trg, src))));
        this.startYProperty().bind(src.layoutYProperty().add(30 + src.getRay() * Math.sin(GUIutil.computeAngle(src, trg))));
        this.endYProperty().bind(trg.layoutYProperty().add(30 + trg.getRay() * Math.sin(GUIutil.computeAngle(trg, src))));
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
