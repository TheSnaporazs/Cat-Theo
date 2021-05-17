package app.events;

import app.GUI.ObjectGUI;
import javafx.event.EventType;

/**
 * ARROW_SPAWNED.java
 * Implementation of ANY_CAT abstract class to provide an event to be thrown on
 * the first step of the creation of an arrow, containing partial information
 * about itself (The source object but not the Target one, since it is yet to be decided)
 *
 * @see app.events.ANY_CAT
 * @see java.util.EventObject
 * @see app.categories.Arrow
 *
 * @author Dario Loi
 * @since 07/05/2021
 */
public class ARROW_SPAWNED_SOURCE extends ANY_CAT {

    public static final EventType<ARROW_SPAWNED_SOURCE> ARROW_SPAWNED_SOURCE_TYPE = new EventType(ANY_CAT_EVENT_TYPE, "ARROW_SPAWNED_SOURCE");

    private final ObjectGUI src;
    private final String name;

    /**
     * Initializes an ARROW_SPAWNED_SOURCE event
     *
     * @param src   The source object of the Arrow
     */
    public ARROW_SPAWNED_SOURCE(ObjectGUI src, String name) {
        super(ARROW_SPAWNED_SOURCE_TYPE);
        this.src = src;
        this.name = name;
    }

    /**
     *
     * @return  Returns the source object upon which to spawn the arrow
     */
    public ObjectGUI getSrc() {
        return src;
    }

    public String getName() {
        return name;
    }
}
