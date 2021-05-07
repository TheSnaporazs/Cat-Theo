package app.events;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class OBJECT_SPAWNED extends ANY_CAT {

    public static final EventType<OBJECT_SPAWNED> OBJECT_SPAWNED_TYPE = new EventType(ANY_CAT_EVENT_TYPE, "OBJECT_SPAWNED");

    private final double X;
    private final double Y;

    public OBJECT_SPAWNED( double x, double y) {
        super(OBJECT_SPAWNED_TYPE);
        this.X = x;
        this.Y = y;
        System.out.println("OBJECT EVENT!");
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }
}
