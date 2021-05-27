package app.GUI;

import app.categories.Arrow;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;


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
        nameGUI = new MovableLabel(this, parent);

        rightPoint = new Line();
        leftPoint = new Line();
        line = new QuadCurve();
        line.setFill(new Color(0, 0, 0, 0));
        line.setStroke(Color.BLACK);

        this.getChildren().addAll(line, rightPoint, leftPoint);
        this.toBack();
    }

    /**
     * Removes this arrowGUI and its components.
     */
    public void removeArrGui()
    {
        this.parent.getChildren().removeAll(this, nameGUI);
    }

    /**
     * Processes the new graphical position of the line, label included.
     */
    public void processLine()
    {   
        //Sorry for what I am doing Dario, it was my only choice.
        double x1 = src.getLayoutX();
        double y1 = src.getLayoutY();
        double x2 = trg.getLayoutX();
        double y2 = trg.getLayoutY();
        if(arrow.isEndomorphism()) {
            x1 += src.getRay();
            x2 -= src.getRay();
        }
        double dist = Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1, 2));
        double angle = GUIutil.computeAngle(src, trg);
        nameGUI.setLayoutX(src.getLayoutX() + Math.cos(nameGUI.alpha + angle) * nameGUI.coeff * dist);
        nameGUI.setLayoutY(src.getLayoutY() + Math.sin(nameGUI.alpha + angle) * nameGUI.coeff * dist);

        processLineFromLabel();
    }

    /**
     * Processes the new graphical position of the line, label excluded.
     */
    public void processLineFromLabel()
    {   
        double angle = GUIutil.computeAngle(src, trg);

        // Trigonometric hellspawn.
        double x1 = src.getLayoutX();
        double y1 = src.getLayoutY();
        double x2 = trg.getLayoutX();
        double y2 = trg.getLayoutY();
        if(arrow.isEndomorphism()) {
            x1 += src.getRay();
            y1 += src.getRay();
        }
        double dist = Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1, 2));
        double c1 = Math.cos(nameGUI.alpha) * nameGUI.coeff * dist;
        double c2 = Math.sin(nameGUI.alpha) * nameGUI.coeff * Math.pow(dist, 1.1f);

        //Compute label
        double t1 = c1 * Math.cos(angle) - c2 * Math.sin(angle) + src.getLayoutX();
        double t2 = c1 * Math.sin(angle) + c2 * Math.cos(angle) + src.getLayoutY();
        line.setControlX(t1);
        line.setControlY(t2);

        //Compute start
        double angleSrcToTrg = Math.atan2(t2-y1,t1-x1);

        if(arrow.isEndomorphism()) {
            angleSrcToTrg -= 1;
            line.setStartX(x1 + src.getRay() * (Math.cos(angleSrcToTrg)));
        } else
            line.setStartX(x1 + src.getRay() * (1 + Math.cos(angleSrcToTrg)));
        line.setStartY(src.getLayoutY() + src.getRay() * (1 + Math.sin(angleSrcToTrg)));

        //Compute end
        double angleCtrlToTrg = Math.atan2(y2-t2,x2-t1);
        if(arrow.isEndomorphism())
            angleCtrlToTrg+= 1;
        
        double endX = x2 + trg.getRay() * (1 + Math.cos(angleCtrlToTrg + Math.PI));
        double endY = trg.getLayoutY() + trg.getRay() * (1 + Math.sin(angleCtrlToTrg + Math.PI));

        line.setEndX(endX);
        line.setEndY(endY);

        double angleCtrlToTip = Math.atan2(endY-t2,endX-t1);
        //Compute left tip
        leftPoint.setStartX(endX);
        leftPoint.setStartY(endY);
        leftPoint.setEndX(endX - 12 * Math.cos(angleCtrlToTip + 0.5236f));
        leftPoint.setEndY(endY - 12 * Math.sin(angleCtrlToTip + 0.5236f));

        //Compute right tip
        rightPoint.setStartX(endX);
        rightPoint.setStartY(endY);
        rightPoint.setEndX(endX - 12 * Math.cos(angleCtrlToTip - 0.5236f));
        rightPoint.setEndY(endY - 12 * Math.sin(angleCtrlToTip - 0.5236f));
    }


    /**
     * Returns the ObjectGUI representing this arrow's source.
     * @return
     */
    public ObjectGUI getSrc() {
        return src;
    }

    /**
     * Returns the ObjectGUI representing this arrow's target.
     * @return
     */
    public ObjectGUI getTrg() {
        return trg;
    }

    /**
     * Returns the Arrow represented by this GUI component.
     * @return
     */
    public Arrow getArrow() {
        return arrow;
    }

}
