package app.GUI;

import app.events.ARROW_SPAWNED;
import app.events.OBJECT_SPAWNED;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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

    public static void spawnObject(double X, double Y, Pane parent)
    {
        getInput(X, Y, parent);
    }

    private static void createCircle(double X, double Y, String stringa, Pane parent) {
        final double[] xCord = new double[1];
        final double[] yCord = new double[1];

        Circle circle = new Circle(60,60,30, Color.WHITE);
        circle.setStroke(Color.BLACK);

        Group CircleGroup = new Group();

        Text testo = new Text(stringa);
        testo.setFont(new Font(30));
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(circle, testo);
        stackPane.setLayoutX(X);
        stackPane.setLayoutY(Y);
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



    public static void getInput(double X, double Y, Pane parent) {
        TextField tf = new TextField();
        tf.relocate(X, Y);
        tf.setPrefWidth(90);
        tf.setPrefHeight(25);
        parent.getChildren().add(tf);
        Button bot = new Button("Create");
        bot.relocate(X+15, Y+25);
        bot.setPrefWidth(60);
        parent.getChildren().add(bot);
        bot.setOnAction(event -> {
            String objName = tf.getText();
            createCircle(X, Y, objName, parent);
            parent.getChildren().removeAll(tf,bot);
        });

    }
}
