package app.events;

import javafx.event.EventType;

/**
 * ARROW_SPAWNED.java
 * Implementation of ANY_CAT abstract class to provide an event to be thrown on
 * the creation of an arrow
 *
 * @see app.events.ANY_CAT
 * @see java.util.EventObject
 * @see app.categories.Arrow
 *
 * @author Dario Loi
 * @since 07/05/2021
 */
public class ARROW_SPAWNED extends ANY_CAT {

    public static final EventType<ARROW_SPAWNED> ARROW_SPAWNED_TYPE = new EventType(ANY_CAT_EVENT_TYPE, "ARROW_SPAWNED");

    private final double X;
    private final double Y;

    public ARROW_SPAWNED(double x, double y) {
        super(ARROW_SPAWNED_TYPE);
        this.X = x;
        this.Y = y;
    }

    /**
     * @return Returns the X coordinate of the Arrow to be spawned as a Double
     */
    public double getX() {
        return X;
    }

    /**
     * @return Returns the Y coordinate of the Arrow to be spawned as a Double
     */
    public double getY() {
        return Y;
    }
}
