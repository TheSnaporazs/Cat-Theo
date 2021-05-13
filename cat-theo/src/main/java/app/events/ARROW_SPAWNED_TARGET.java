package app.events;

import app.GUI.ObjectGUI;
import app.categories.Obj;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * ARROW_SPAWNED_TARGET.java
 * Implementation of ANY_CAT abstract class to provide an event to be thrown on
 * the second step of the creation of an Arrow, providing the attributes of the two Objects selected
 *
 * @see app.events.ANY_CAT
 * @see java.util.EventObject
 * @see app.categories.Arrow
 *
 * @author Dario Loi
 * @since 07/05/2021
 */
public class ARROW_SPAWNED_TARGET extends ANY_CAT {

    public static final EventType<ARROW_SPAWNED_TARGET> ARROW_SPAWNED_TYPE = new EventType(ANY_CAT_EVENT_TYPE, "ARROW_SPAWNED_TARGET");

    private ObjectGUI src;
    private ObjectGUI trg;

    private String name;

    /**
     * Initialized an ARROW_SPAWNED_TARGET Object
     * @param src   The source object of the arrow
     * @param trg   The target object of the arrow
     */
    public ARROW_SPAWNED_TARGET(ObjectGUI src, ObjectGUI trg, String name) {
        super(ARROW_SPAWNED_TYPE);
        this.src = src;
        this.trg = trg;
        this.name = name;
    }

    /**
     *
     * @return  Returns the source object of the arrow
     */
    public ObjectGUI getSrc() {
        return src;
    }

    /**
     *
     * @return  Returns the target object of the arrow
     */
    public ObjectGUI getTrg() {
        return trg;
    }

    public String getName() {
        return name;
    }


}
