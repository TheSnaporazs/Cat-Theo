package app.events;

import app.categories.Obj;
import javafx.event.EventType;

/**
 * OBJECT_DELETED.java
 *  Implementation of ANY_CAT abstract class to provide an event
 *  to be thrown upon deletion of an object
 *
 * @see app.events.ANY_CAT
 * @see app.categories.Obj
 * @see java.util.EventObject
 *
 * @author Dario Loi
 * @since 17/05/2021
 */
public class OBJECT_DELETED extends ANY_CAT{

    public static final EventType<OBJECT_DELETED> OBJECT_DELETED_TYPE = new EventType(ANY_CAT_EVENT_TYPE, "OBJECT_DELETED");

    private Obj object;

    /**
     * Takes the Obj to be deleted and propagates it to the target of the event
     * @param object
     */
    public OBJECT_DELETED(Obj object) {
        /*
        Sadly I can't call a single constructor from the overloaded ones since I also have to do a super() call :(
        as long as it works!
         */
        super(OBJECT_DELETED_TYPE);
        this.object = object;
    }

    /**
     *
     * @return returns the Obj to be deleted
     */
    public Obj getObject() { return object; }

}
