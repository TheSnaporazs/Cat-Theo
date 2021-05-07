package app.events;

import javafx.event.EventType;

/**
 * OBJECT_SPAWNED.java
 * Implementation of ANY_CAT abstract class to provide an event to be thrown on
 * the creation of an object
 *
 * @see app.events.ANY_CAT
 * @see java.util.EventObject
 * @see app.categories.Obj
 *
 * @author Dario Loi
 * @since 07/05/2021
 */
public class OBJECT_SPAWNED extends ANY_CAT {

    public static final EventType<OBJECT_SPAWNED> OBJECT_SPAWNED_TYPE = new EventType(ANY_CAT_EVENT_TYPE, "OBJECT_SPAWNED");

    private final double X;
    private final double Y;
    private final String objName;

    /**
     * @return Returns the name of the object to be spawned
     */
    public String getObjName() {
        return objName;
    }

    public OBJECT_SPAWNED(double x, double y, String objName) {
        super(OBJECT_SPAWNED_TYPE);
        this.X = x;
        this.Y = y;
        this.objName = objName;
    }

    /**
     * @return Returns the X coordinate of the object to be spawned as a Double
     */
    public double getX() {
        return X;
    }

    /**
     * @return Returns the Y coordinate of the object to be spawned as a Double
     */
    public double getY() {
        return Y;
    }
}
