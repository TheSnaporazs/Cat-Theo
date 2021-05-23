package app.GUI;

import app.GUI.Bindings.*;
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
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
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

    ObjectGUI src;
    ObjectGUI trg;
    private Arrow arrow;
    private Pane parent;

    //graphical components
    private QuadCurve line;
    public MovableLabel nameGUI;
    private Line rightPoint;
    private Line leftPoint;


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
        processLine();
    }

    private void initGraphics()
    {
        double angle = GUIutil.computeAngle(src, trg);
        double len = Math.sqrt(Math.pow(trg.getLayoutX() - src.getLayoutX(), 2) + Math.pow(trg.getLayoutY() - src.getLayoutY(), 2));
        nameGUI = new MovableLabel(this, parent);

        rightPoint = new Line();
        leftPoint = new Line();
        line = new QuadCurve();
        line.setFill(new Color(0, 0, 0, 0));
        line.setStroke(Color.BLACK);

        parent.getChildren().addAll(line, rightPoint, leftPoint);
        rightPoint.toBack();
        leftPoint.toBack();
        line.toBack();
    }


    public void removeArrGui()
    {
        this.parent.getChildren().removeAll(this, line, nameGUI, rightPoint, leftPoint);
    }

    /**
     * Binds the arrow's start and end to the X and Y properties of the source and target object
     *
     * @see app.GUI.Bindings.TrigBounding
     */
    public void processLine()
    {   
        //Sorry for what I am doing Dario, it was my only choice.
        double x1 = src.getLayoutX();
        double y1 = src.getLayoutY();
        double x2 = trg.getLayoutX();
        double y2 = trg.getLayoutY();
        double dist = Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1, 2));
        double angle = GUIutil.computeAngle(src, trg);
        nameGUI.setLayoutX(src.getLayoutX() + Math.cos(nameGUI.alpha + angle) * nameGUI.coeff * dist);
        nameGUI.setLayoutY(src.getLayoutY() + Math.sin(nameGUI.alpha + angle) * nameGUI.coeff * dist);

        processLineFromLabel();
    }

    
    public void processLineFromLabel()
    {   
        double angle = GUIutil.computeAngle(src, trg);
        double endX = trg.getLayoutX() + trg.getRay() * (1 + Math.cos(angle + Math.PI));
        double endY = trg.getLayoutY() + trg.getRay() * (1 + Math.sin(angle + Math.PI));

        //Compute start
        line.setStartX(src.getLayoutX() + 30);
        line.setStartY(src.getLayoutY() + 30);

        // Trigonometric hellspawn.
        double x1 = src.getLayoutX();
        double y1 = src.getLayoutY();
        double x2 = trg.getLayoutX();
        double y2 = trg.getLayoutY();
        double dist = Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1, 2));
        double c1 = Math.cos(nameGUI.alpha) * nameGUI.coeff * dist;
        double c2 = Math.sin(nameGUI.alpha) * nameGUI.coeff * Math.pow(dist, 1.1f);

        //Compute label
        line.setControlX(c1 * Math.cos(angle) - c2 * Math.sin(angle) + src.getLayoutX());
        line.setControlY(c1 * Math.sin(angle) + c2 * Math.cos(angle) + src.getLayoutY());

        //Compute end
        line.setEndX(trg.getLayoutX() + 30);
        line.setEndY(trg.getLayoutY() + 30);

        //Compute left tip
        leftPoint.setStartX(endX);
        leftPoint.setStartY(endY);
        leftPoint.setEndX(endX - 12 * Math.cos(angle + 0.5236f));
        leftPoint.setEndY(endY - 12 * Math.sin(angle + 0.5236f));

        //Compute right tip
        rightPoint.setStartX(endX);
        rightPoint.setStartY(endY);
        rightPoint.setEndX(endX - 12 * Math.cos(angle - 0.5236f));
        rightPoint.setEndY(endY - 12 * Math.sin(angle - 0.5236f));
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
