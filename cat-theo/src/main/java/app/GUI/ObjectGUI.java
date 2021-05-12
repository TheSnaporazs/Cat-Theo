package app.GUI;

import app.categories.Obj;
import app.exceptions.IllegalArgumentsException;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Child object of stackpane to provide a standardized implementation
 * of a graphical object, to reduce boilerplate from controllers
 * and control every ObjectGUI's behaviour from one place, avoiding the
 * addition of EventHandlers in various places of the code
 *
 * @author Dario Loi
 * @since 10/05/2021
 * @see StackPane
 * @see app.categories.Obj
 */
public class ObjectGUI extends StackPane {

    public ObjectGUI(double X, double Y, Obj object, Pane parent) throws IllegalArgumentsException {
        super();

        drawCircle(X, Y, object);                   //Graphical representation
        addHandlers(parent);     //Event Handling
        this.setCursor(Cursor.HAND);                //Cursor Icon, I think it's neat!
    }

    private void drawCircle(double X, double Y, Obj object)
    {
        Circle circle = new Circle(60,60,30, Color.WHITE);
        circle.setStroke(Color.BLACK);
        Text txt = new Text(object.getName());
        txt.setFont(new Font(30));

        this.getChildren().addAll(circle, txt);
        this.relocate(X, Y);

        object.setRepr(this); //After all we have to let the object know of this...
    };

    /**
     * Adds various behaviours to the object,
     * @param parent    the parent pane on which the object is spawned
     * @param cntxt     the context menu utilized to prompt the user from the object
     */
    private void addHandlers(Pane parent)
    {
        //To be used in lambdas from an outside scope, we must do this treachery, or so I have been told
        final double[] xCord = new double[1];
        final double[] yCord = new double[1];

        this.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> {
                    if(event.getButton().equals(MouseButton.SECONDARY)) {
                        try {
                            generateContext(event.getScreenX(), event.getScreenY(), parent);
                        } catch(IllegalArgumentsException e) {
                            System.out.println(e.getMessage() + "\n" + "error got while creating context menu for object");
                            e.printStackTrace();
                        }
                        event.consume();
                    }});
        this.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                event -> {
                        this.setCursor(Cursor.MOVE);
                        double offsetX = event.getSceneX() - xCord[0];
                        double offsetY = event.getSceneY() - yCord[0];

                        this.setLayoutX(this.getLayoutX() + offsetX);
                        this.setLayoutY(this.getLayoutY() + offsetY);

                        xCord[0] = event.getSceneX();
                        yCord[0] = event.getSceneY();
                        event.consume();
                    });
        this.addEventHandler(MouseEvent.MOUSE_PRESSED,
                event -> {
                        xCord[0] = event.getSceneX();
                        yCord[0] = event.getSceneY();
                        event.consume();
                    });
        this.addEventHandler(MouseEvent.MOUSE_RELEASED,
                event -> {
                        this.setCursor(Cursor.HAND);
                        event.consume();
                    });
    }

    private void generateContext(Double X, Double Y, Pane parent) throws IllegalArgumentsException {
        /*
        Creation of the object contextMenu, wordy.
         */
        String[] items = {"Spawn Morphism"};
        EventHandler[] actions = {
                (event -> {
                    String name = GUIutil.spawnPrompt("Name: ", "Insert Morphism Name");
                })
        };

        GUIutil.pingCreationMenu(X, Y, parent, items, actions);
    }

}
