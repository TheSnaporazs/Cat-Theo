package app.controllers;


import app.GUI.GUIutil;
import app.categories.Category;
import app.categories.Obj;
import app.events.OBJECT_SPAWNED;
import app.exceptions.BadObjectNameException;
import app.exceptions.IllegalArgumentsException;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    private Category currCat = new Category();

    @FXML private AnchorPane scroll_wrap;
    @FXML private ToggleGroup tog1;
    @FXML private ToggleGroup tog2;

    public WorkController()
    {

    }

    @FXML
    public void initialize() {
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
                                Button temp = new Button();
                                temp.relocate(X, Y);
                                System.out.println("R-CLICK!");

                                String[] items = {"Create Object"};
                                EventHandler[] actions = {
                                        ((event1) -> {
                                            String name = GUIutil.spawnPrompt("Name: ", "Insert Object Name");
                                            scroll_wrap.fireEvent(new OBJECT_SPAWNED(X, Y, name));
                                        })
                                };
                                ContextMenu menu = null;
                                try {
                                    menu = GUIutil.spawnCreationMenu(X, Y, items , actions);
                                } catch (IllegalArgumentsException e) {
                                    e.printStackTrace();
                                }

                                temp.setContextMenu(menu);
                                temp.fire();
                                menu.setOnHidden((event1) -> {
                                    scroll_wrap.getChildren().remove(temp);
                                });

                                scroll_wrap.getChildren().add(temp);
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

                }
                );

        scroll_wrap.addEventHandler(OBJECT_SPAWNED.OBJECT_SPAWNED_TYPE, event -> {
            try {
                scroll_wrap.getChildren().add(
                        GUIutil.spawnObject(event.getX(), event.getY(), currCat.addObject(event.getObjName()))
                );  //Woah that's a lot!
                printCurrCat();
            } catch (BadObjectNameException e) {
                e.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("Duplicate Object Error");
                error.setContentText("Cannot have two objects with the same name in the same category!");
                error.showAndWait();
            }
        });
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
        currCat.printObjects();
        currCat.printArrows();
    }




    


}
