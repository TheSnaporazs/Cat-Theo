package app.events;

import app.GUI.ArrGUI;

import javafx.event.EventType;

public class ARROW_OPTION extends ANY_CAT{
    private ArrGUI arr;
    private final double X;
    private final double Y;


    public static final EventType<ARROW_OPTION> ARROW_OPTION_TYPE = new EventType(ANY_CAT_EVENT_TYPE, "ARROW_OPTION");

    public ARROW_OPTION(double x, double y, ArrGUI arr) {
        super(ARROW_OPTION_TYPE);
        this.arr = arr;
        this.X = x;
        this.Y = y;
    }

    public ArrGUI getArr() {return arr;}

    /**
     * @return Returns the X coordinate of the movableLabel
     */
    public double getX() {
        return X;
    }

    /**
     * @return Returns the Y coordinate of the movableLabel
     */
    public double getY() {
        return Y;
    }
}