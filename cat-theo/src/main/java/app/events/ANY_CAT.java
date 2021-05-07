package app.events;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public abstract class ANY_CAT extends Event {

    public static final EventType<ANY_CAT> ANY_CAT_EVENT_TYPE = new EventType(ANY);

    public ANY_CAT(EventType<? extends Event> eventType) {
        super(eventType);
    }


}