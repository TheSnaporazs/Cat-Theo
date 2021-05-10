package app.GUI;

import app.events.ARROW_SPAWNED;
import app.events.OBJECT_SPAWNED;
import app.categories.Obj;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
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
     * Spawns a context menu prompting the user with the creation of an Object/Arrow, ensuring that it gets
     * properly removed when the user is done with it's decision
     *
     * @see app.categories.Obj
     * @see app.categories.Arrow
     * @param X A double representing the x coordinate at which to spawn the menu
     * @param Y A double representing the y coordinate at which to spawn the menu
     * @param parent    a JavaFX Pane object representing the parent object upon which to attach the context menu
     */
    public static ContextMenu spawnCreationMenu(double X, double Y, String item, EventHandler action)
    {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem opt1 = new MenuItem(item);

        opt1.setOnAction(action);


        contextMenu.getItems().add(opt1);

        return contextMenu;
    }

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
