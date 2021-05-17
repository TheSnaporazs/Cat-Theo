package app.controllers;


import app.GUI.ArrGUI;
import app.GUI.GUIutil;
import app.GUI.ObjectGUI;
import app.categories.Category;
import app.events.ARROW_SPAWNED_SOURCE;
import app.events.ARROW_SPAWNED_TARGET;
import app.events.OBJECT_DELETED;
import app.events.OBJECT_SPAWNED;
import app.exceptions.BadObjectNameException;
import app.exceptions.IllegalArgumentsException;
import app.exceptions.ImpossibleArrowException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Java-FX controller for the work page
 *
 * @author Dario Loi
 * @since 30/04/2021
 */
public class WorkController extends GenericController{
    public static ContextMenu CtxMenu = new ContextMenu();
    private Category currCat = new Category();

    @FXML private AnchorPane scroll_wrap;
    @FXML private ToggleGroup tog1;
    @FXML private ToggleGroup tog2;
    @FXML private ScrollPane pannable;
    @FXML private AnchorPane root;
    private boolean isCreatingArrow = false;

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

        scroll_wrap.addEventHandler(OBJECT_SPAWNED.OBJECT_SPAWNED_TYPE, event -> {
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

        scroll_wrap.addEventHandler(ARROW_SPAWNED_SOURCE.ARROW_SPAWNED_SOURCE_TYPE, event -> {
            //TODO add mouse chasing line effect
            isCreatingArrow = true;

        });

        scroll_wrap.addEventHandler(ARROW_SPAWNED_TARGET.ARROW_SPAWNED_TARGET_TYPE, event -> {
            //TODO remove mouse chasing line effect
            try {
                ObjectGUI src = event.getSrc();
                ObjectGUI trg = event.getTrg();

                double[] src_coord = {src.getLayoutX(), src.getLayoutY()};
                double[] trg_coord = {trg.getLayoutX(), trg.getLayoutY()};

                scroll_wrap.getChildren().add(
                        new ArrGUI(src, trg,
                                currCat.addArrow(event.getName(),src.getObject(), trg.getObject()), scroll_wrap)
                );


            } catch (ImpossibleArrowException e) {
                e.printStackTrace();
            }
            printCurrCat();
        });

        scroll_wrap.addEventHandler(OBJECT_DELETED.OBJECT_DELETED_TYPE, event -> {

                currCat.removeObject(event.getObject());

        });
    }

    /**
     * Debug method, prints to terminal the contents of the current category
     * being displayed
     *
     */
    public void printCurrCat()
    {
        currCat.printObjects();
        currCat.printArrows();
    }

    /**
     * Action called from the view, provides a simple method to instantiate an object at a generic
     * location of the view without having to do particular GUI interactions
     */
    @FXML
    private void objectFromMenu()
    {
        //all of this GUI voodoo is to properly spawn the object on the center of the currently visible scrollpane
        Bounds bounds = pannable.getViewportBounds();
        scroll_wrap.fireEvent(new OBJECT_SPAWNED(bounds.getCenterX(),
                bounds.getCenterY(),
                GUIutil.spawnPrompt("Name: ", "Insert Object Name")));
    }

    @FXML
    private void arrowFromMenu()
    {
        String[] msgs = {"Source Object", "Target Object", "Arrow Name"};
        Dialog<ArrayList<String>> prompt = GUIutil.spawnMultiPrompt(msgs,"Spawn Arrow");

        // The fact that I am using this is giving me an aneurysm the same way that seeing it is giving it to you
        Optional<ArrayList<String>> objects = prompt.showAndWait();
        ArrayList<String> list = objects.orElseThrow(NullPointerException::new);


        scroll_wrap.fireEvent(new ARROW_SPAWNED_TARGET(
                (currCat.getObject(list.get(0))).getRepr(),   //Source object
                (currCat.getObject(list.get(1))).getRepr(),   //Target object
                list.get(2)));                              //Arrow  Name
    }

    public boolean isCreatingArrow() {
        return isCreatingArrow;
    }

}
