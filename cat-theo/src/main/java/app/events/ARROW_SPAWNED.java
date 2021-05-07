package app.events;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class ARROW_SPAWNED extends ANY_CAT {

    public static final EventType<ARROW_SPAWNED> ARROW_SPAWNED_TYPE = new EventType(ANY_CAT_EVENT_TYPE, "ARROW_SPAWNED");

    private final double X;
    private final double Y;

    public ARROW_SPAWNED(double x, double y) {
        super(ARROW_SPAWNED_TYPE);
        this.X = x;
        this.Y = y;
        System.out.println("ARROW EVENT!");

    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }
}
