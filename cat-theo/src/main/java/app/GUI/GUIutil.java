package app.GUI;

import app.events.ARROW_SPAWNED;
import app.events.OBJECT_SPAWNED;
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
    public static void spawnCreationMenu(double X, double Y, Pane parent)
    {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem obj = new MenuItem("Create new object");
        MenuItem arr = new MenuItem("Create new morphism");

        obj.setOnAction((event -> {
            System.out.println("Object");
            String objName = spawnPrompt("Insert Object Name", "Create Object");
            parent.fireEvent(new OBJECT_SPAWNED(X, Y, objName));
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


    /**
     * Spawns the graphical representation of an Object, as a node of a graph
     *
     * @see app.categories.Obj
     * @see app.categories.Category
     * @param X     Double, the X coordinate of the Object
     * @param Y     Double, the Y coordinate of the Object
     * @param objName   String, the name of the Object
     * @param parent    Pane, the parent pane upon which to attach the object
     */
    public static void spawnObject(double X, double Y, String objName, Pane parent) {
        final double[] xCord = new double[1];
        final double[] yCord = new double[1];

        Circle circle = new Circle(60,60,30, Color.WHITE);
        circle.setStroke(Color.BLACK);

        Group CircleGroup = new Group();

        Text testo = new Text(objName);
        testo.setFont(new Font(30));

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(circle, testo);
        stackPane.relocate(X, Y);

        CircleGroup.getChildren().add(stackPane);
        parent.getChildren().add(CircleGroup);

        CircleGroup.setCursor(Cursor.HAND);
        CircleGroup.setOnMousePressed((t) -> {
            xCord[0] = t.getSceneX();
            yCord[0] = t.getSceneY();
        });
        CircleGroup.setOnMouseDragged((t) -> {

            double offsetX = t.getSceneX() - xCord[0];
            double offsetY = t.getSceneY() - yCord[0];

            CircleGroup.setLayoutX(CircleGroup.getLayoutX() + offsetX);
            CircleGroup.setLayoutY(CircleGroup.getLayoutY() + offsetY);

            xCord[0] = t.getSceneX();
            yCord[0] = t.getSceneY();
        });
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
