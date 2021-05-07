package app.controllers;


import app.GUI.GUIutil;
import app.categories.Category;
import app.categories.Obj;
import app.events.ANY_CAT;
import app.events.ARROW_SPAWNED;
import app.events.OBJECT_SPAWNED;
import app.exceptions.BadObjectNameException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Java-FX controller for the work page
 *
 * @author Dario Loi
 * @since 30/04/2021
 */
public class WorkController extends GenericController{

    private Category currCat = new Category();

    @FXML private AnchorPane scroll_wrap;
    @FXML private ToggleGroup tog1;
    @FXML private ToggleGroup tog2;

    public WorkController()
    {

    }

    @FXML
    public void initialize() {


    /*
    Whole thing should be redone in a sensible way
    lots of checks are missing, but the panning behaves
    in a humane way now and I must go to dinner!
     */

        scroll_wrap.addEventHandler(MouseEvent.ANY,
                event -> {
                    if (event.getEventType() == MouseEvent.MOUSE_CLICKED )
                    {
                        double X = event.getX();
                        double Y = event.getY();

                        switch (event.getButton())  //we catch all of them since switch is a O(1) hash table
                        {
                            case PRIMARY:
                                //select object
                                break;
                            case SECONDARY:
                                GUIutil.spawnCreationMenu(X, Y, scroll_wrap);
                                break;
                            case NONE:
                                break;
                            case MIDDLE:

                                break;

                            case BACK:
                                break;
                            case FORWARD:
                                break;
                        }
                    }
                    if (event.getButton() != MouseButton.MIDDLE)
                    {
                        event.consume();
                    }


/*                    if(mouseState.equals("sel") && (event.getButton() != MouseButton.MIDDLE))
                    {
                        System.out.println("We in!");
                        switch(toSpawn)
                        {
                            case "obj":
                                //TODO Spawn object + Update model
                                System.out.println("OBJECT!" + X + Y);
                                break;
                            case "arr":
                                //TODO Spawn arrow + Update model
                                System.out.println("ARROW!" + X + Y);
                                break;
                        }
                        event.consume();
                    }*/
                }
                );

        scroll_wrap.addEventHandler(OBJECT_SPAWNED.OBJECT_SPAWNED_TYPE, event -> {
            GUIutil.spawnObject(event.getX(), event.getY(), scroll_wrap);
        });
    }


    /**
     * Updates the model by adding the object specifed by the user
     *
     * @param e Contains the attributes of the ActionEvent that invoked the method
     */
    @FXML
    private void addObj(ActionEvent e) throws BadObjectNameException {
        /*
        TODO get object name from View
        We do it twice just to be able to then
        test the addArr method, final implementation
        will of course only perform one addition.
         */
        currCat.addObject("A");
    }

    /**
     * Updates the model by adding the arrow specified by the user
     * @param e
     */
    private void addArr(ActionEvent e)
    {
        // TODO get arrow attributes from View

    }

    /**
     * Debug method, prints to terminal the contents of the current category
     * being displayed
     */
    public void printCurrCat()
    {
        // TODO add printAll method to category object
    }




    


}
