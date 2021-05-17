package app.controllers;


import app.GUI.GUIutil;
import app.GUI.ObjectGUI;
import app.categories.Category;
import app.events.OBJECT_SPAWNED;
import app.exceptions.BadObjectNameException;
import app.exceptions.BadSpaceException;
import app.exceptions.IllegalArgumentsException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Java-FX controller for the work page
 *
 * @author Dario Loi
 * @since 30/04/2021
 */
public class WorkController extends GenericController{
    public static ContextMenu CtxMenu = new ContextMenu();
    private Category currCat = new Category("UniverseName");

    @FXML private AnchorPane scroll_wrap;
    @FXML private ToggleGroup tog1;
    @FXML private ToggleGroup tog2;

    public WorkController()
    {

    }

    @FXML
    public void initialize() {

        // Mapping right click to a context menu
        scroll_wrap.addEventHandler(MouseEvent.MOUSE_CLICKED,
            event -> {
                if (event.getButton() == MouseButton.SECONDARY) { //we catch all of them since switch is a O(1) hash table
                    String[] items = {"Create Object"};
                    EventHandler[] actions = {
                        ((event1) -> {
                            String name = GUIutil.spawnPrompt("Name: ", "Insert Object Name");
                            scroll_wrap.fireEvent(new OBJECT_SPAWNED(event.getX(),
                                    event.getY(), name));
                        })
                    };
                    try {
                        GUIutil.pingCreationMenu(event.getScreenX(), event.getScreenY(), scroll_wrap, items, actions);
                    } catch (IllegalArgumentsException e) {
                        System.out.println("Something went wrong in the contextMenu init! " +
                                "(This shouldn't really happen!)");
                        e.printStackTrace();
                    }
                    event.consume();
                }
            });
        
        // Mapping left-button-drag to panning the scroll pane
        scroll_wrap.addEventHandler(MouseEvent.MOUSE_DRAGGED,
            event -> {
                if(event.getButton() != MouseButton.PRIMARY)
                    event.consume();
            });

        scroll_wrap.addEventHandler(OBJECT_SPAWNED.OBJECT_SPAWNED_TYPE,
            event -> {
                try {
                    scroll_wrap.getChildren().add(
                            new ObjectGUI(event.getX(), event.getY(), currCat.addObject(event.getObjName()), scroll_wrap)
                    );  //Woah that's a lot!
                    printCurrCat();
                } catch (BadObjectNameException e) {
                    e.printStackTrace();
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Duplicate Object Error");
                    error.setContentText("Cannot have two objects with the same name in the same category!");
                    error.showAndWait();
                } catch (IllegalArgumentsException e) {

                    e.printStackTrace();
                    System.out.println("Something went wrong in the contextMenu init! (This shouldn't really happen!)");
                }
            });
    }


    /**
     * Debug method, prints to terminal the contents of the current category
     * being displayed
     */
    public void printCurrCat()
    {
        currCat.printObjects();
        currCat.printArrows();
    }
}
