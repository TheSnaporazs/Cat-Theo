package app.GUI;

import app.categories.Obj;
import app.exceptions.IllegalArgumentsException;
import app.controllers.WorkController;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * GUIutil.java
 * Singleton to provide spawning methods of javafx objects
 * to avoid boilerplate implementations in each page's controllers
 *
 * @author Dario Loi
 * @since 07/05/2021
 * @see app.controllers
 */
public class GUIutil {

    /**
     * Positions the context menu abd prompts the user with generic options provided in the function's arguments
     * the Strings and the corresponding events must be paired at the same index in the two arrays.
     * @see app.categories.Obj
     * @see app.categories.Arrow
     * @param x X to position at
     * @param y Y to position at
     * @param parent 
     * @param items An array of strings to be displayed as menuitems on the contextMenu
     * @param actions An array of EventHandlers to be attached to each menuitems on the contextmenu
     * @throws IllegalArgumentsException
     */
    public static void pingCreationMenu(Double x, Double y, Node parent, String[] items, EventHandler[] actions) throws IllegalArgumentsException {
        if(items.length != actions.length)
        {
            throw new IllegalArgumentsException("The Menu cannot have an unequal amount of items and actions!");
        }

        ArrayList<MenuItem> mItems = new ArrayList<MenuItem>();
        for(int c = 0; c < items.length; c++)
        {
            mItems.add(new MenuItem(items[c]));
            mItems.get(c).setOnAction(actions[c]);
        }
        WorkController.CtxMenu.getItems().clear();
        WorkController.CtxMenu.getItems().addAll(mItems);
        WorkController.CtxMenu.setAutoHide(true); //Dunno what this does
        WorkController.CtxMenu.show(parent, x, y);
    }


    /**
     * Wrapper method for spawnCreationMenu, provides an automatic implementation
     * of the ugly button trick utilized to pop a ContextMenu on a scrollPane
     * so that I do not have to look at it directly when implementing it
     * (sorry for the boilerplate, still friends? <3)
     *
     * WARNING: the button trick is not necessary anymore, one can instantiate
     * the contextMenu and then call the show method on a parent object
     * to cleanly display it without need for such voodoo tricks, therefore
     * this method is deprecated.
     * @see ContextMenu
     * @see Pane
     * @param X Double, the X coordinate at which to spawn the menu at
     * @param Y Double, the Y coordinate at which to spawn the menu at
     * @param items     Array of Strings, contains all the different text options
     *                  to be displayed on the menu
     * @param actions   Array of Eventhandlers, contains all the different actions
     *                  to be performed by the option
     * @param parent    Any child of class Pane, provides a parent upon which to attach
     *                  the button to do the ugly trick
     */
    @Deprecated
    public static void ButtonMenu(double X, double Y, String[] items, EventHandler[] actions, Pane parent)
    {
        Button temp = new Button();
        temp.relocate(X, Y);

        try {
            GUIutil.pingCreationMenu(X, Y, parent, items, actions);
        } catch (IllegalArgumentsException e) {
            e.printStackTrace();
        }

        temp.setContextMenu(WorkController.CtxMenu);
        temp.fire();
        WorkController.CtxMenu.setOnHidden((event1) -> {
            parent.getChildren().remove(temp);
        });

        parent.getChildren().add(temp);
    }


    // Isn't this method deprecated??? -Davide
    /**
     * Spawns the graphical representation of an Object, as a node of a graph
     *
     * @see app.categories.Obj
     * @see app.categories.Category
     * @param X     Double, the X coordinate of the Object
     * @param Y     Double, the Y coordinate of the Object
     * @param obj   Obj, the Object
     */
    public static StackPane spawnObject(double X, double Y, Obj obj) {
        final double[] xCord = new double[1];
        final double[] yCord = new double[1];

        Circle circle = new Circle(60,60,30, Color.WHITE);
        circle.setStroke(Color.BLACK);

        Text testo = new Text(obj.getName());
        testo.setFont(new Font(30));

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(circle, testo);
        stackPane.relocate(X, Y);

        obj.setRepr(stackPane); //After all we have to let the object know of this...

        stackPane.setCursor(Cursor.HAND);
        stackPane.setOnMousePressed((t) -> {
            xCord[0] = t.getSceneX();
            yCord[0] = t.getSceneY();
        });
        stackPane.setOnMouseDragged((t) -> {

            double offsetX = t.getSceneX() - xCord[0];
            double offsetY = t.getSceneY() - yCord[0];

            stackPane.setLayoutX(stackPane.getLayoutX() + offsetX);
            stackPane.setLayoutY(stackPane.getLayoutY() + offsetY);

            xCord[0] = t.getSceneX();
            yCord[0] = t.getSceneY();
        });

        return stackPane;
    }


    /**
     * Utility method to spawn a prompt (TextInputDialog), the prompts acts as an interrupt, waiting for the input
     * to be provided before resuming the program's execution
     *
     * @see TextInputDialog
     * @param msg   a String to be displayed as a message to the user
     * @param Title a String to be used as the prompt window's title
     * @return      the String inserted by the user
     */
    public static String spawnPrompt(String msg, String Title) {

        TextInputDialog prova = new TextInputDialog();
        prova.setTitle(Title);
        prova.setGraphic(null);
        prova.setHeaderText("");
        prova.setContentText(msg);
        prova.showAndWait();

        return prova.getEditor().getText();
    }
}
