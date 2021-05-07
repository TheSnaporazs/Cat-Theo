package app.GUI;

import app.events.ARROW_SPAWNED;
import app.events.OBJECT_SPAWNED;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

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
     * @param X A double representing the x coordinate at which to spawn the menu
     * @param Y A double representing the y coordinate at which to spawn the menu
     * @param parent    a JavaFX Pane object representing the parent object upon which to attach the context menu
     */
    public static void spawnCreationMenu(double X, double Y, Pane parent)
    {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem obj = new MenuItem("Create new object");
        MenuItem arr = new MenuItem("Create new morphism");

        obj.setOnAction((event -> {
            System.out.println("Object");
            parent.fireEvent(new OBJECT_SPAWNED(X, Y));
        }));

        arr.setOnAction((event -> {
            System.out.println("Arrow");
            parent.fireEvent(new ARROW_SPAWNED(X, Y));
        }));


        contextMenu.getItems().addAll(obj, arr);

        Button invisible = new Button();
        invisible.setContextMenu(contextMenu);

        invisible.relocate(X, Y);
        invisible.fire();
        parent.getChildren().add(invisible);

        contextMenu.setOnHidden(event ->
        {
            parent.getChildren().remove(invisible);
        });
    }
}
