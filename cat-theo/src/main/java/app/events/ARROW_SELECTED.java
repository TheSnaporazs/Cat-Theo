package app.events;

import app.GUI.ArrGUI;

import javafx.event.EventType;

public class ARROW_SELECTED extends ANY_CAT{
    private ArrGUI arr;

    public static final EventType<ARROW_SELECTED> ARROW_SELECTED_TYPE = new EventType(ANY_CAT_EVENT_TYPE, "ARROW_SELECTED");

    public ARROW_SELECTED(ArrGUI arr) {
        super(ARROW_SELECTED_TYPE);
        this.arr = arr;
    }

    public ArrGUI getArr() {return arr;}
}