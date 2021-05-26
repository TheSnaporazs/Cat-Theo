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
public class ARROW_SPAWNED_IDENTITY extends ANY_CAT {

    public static final EventType<ARROW_SPAWNED_IDENTITY> ARROW_SPAWNED_IDENTITY_TYPE = new EventType(ANY_CAT_EVENT_TYPE, "ARROW_SPAWNED_IDENTITY");

    private final ObjectGUI src;

    /**
     * Initializes an ARROW_SPAWNED_SOURCE event
     *
     * @param src   The source object of the Arrow
     */
    public ARROW_SPAWNED_IDENTITY(ObjectGUI src) {
        super(ARROW_SPAWNED_IDENTITY_TYPE);
        this.src = src;
    }

    /**
     *
     * @return  Returns the source object upon which to spawn the arrow
     */
    public ObjectGUI getSrc() {
        return src;
    }
}

