package app.events;

import app.categories.Arrow;
import javafx.event.EventType;

public class COMPOSITION_SPAWNED extends ANY_CAT {

    public static final EventType<COMPOSITION_SPAWNED> COMPOSITION_SPAWNED_TYPE = new EventType(ANY_CAT_EVENT_TYPE, "COMPONENT_SPAWNED");

    private Arrow g;
    private Arrow f;

    public COMPOSITION_SPAWNED(Arrow g, Arrow f) {
        super(COMPOSITION_SPAWNED_TYPE);
        this.g = g;
        this.f = f;
    }

    public Arrow getG() {
        return g;
    }

    public Arrow getF() {
        return f;
    }
}
