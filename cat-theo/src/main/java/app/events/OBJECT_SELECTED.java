package app.events;

import app.GUI.ObjectGUI;
import javafx.event.EventType;

public class OBJECT_SELECTED extends ANY_CAT{
    private ObjectGUI obj;

    public static final EventType<OBJECT_SELECTED> OBJECT_SELECTED_TYPE = new EventType(ANY_CAT_EVENT_TYPE, "OBJECT_SELECTED");

    public OBJECT_SELECTED(ObjectGUI obj) {
        super(OBJECT_SELECTED_TYPE);
        this.obj = obj;
    }

    public ObjectGUI getObj() {return obj;}
}
