package app.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * ANY_CAT.java
 * generic abstract class through which to implement a custom event type
 * related to category theory
 *
 * @see app.categories.Category
 * @see Event
 * @author Dario Loi
 * @since 07/05/2021
 */
public abstract class ANY_CAT extends Event {

    public static final EventType<ANY_CAT> ANY_CAT_EVENT_TYPE = new EventType(ANY);

    public ANY_CAT(EventType<? extends Event> eventType) {
        super(eventType);
    }

}