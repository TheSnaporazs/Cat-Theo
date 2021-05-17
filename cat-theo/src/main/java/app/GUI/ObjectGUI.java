package app.GUI;

import app.categories.Obj;
import app.events.OBJECT_DELETED;
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
 * ObjectGUI.java
 *
 * Child object of stackpane to provide a standardized implementation
 * of a graphical object, to reduce boilerplate from controllers
 * and control every ObjectGUI's behaviour from one place, avoiding the
 * addition of EventHandlers in various places of the code
 *
 * @author Dario Loi
 * @since 10/05/2021
 *
 * @see app.categories.Obj
 * @see StackPane
 */
public class ObjectGUI extends StackPane {
    double xCord;
    double yCord;

    private Pane parent;
    private Obj object;
    private double RAY = 30;

    /**
     *
     * @param X X coord of the ObjGUI
     * @param Y Y coord of the ObjGUI
     * @param object    Model representation of the ObjGUI
     * @param parent    Parent pane upon which the objGUI is attached
     * @throws IllegalArgumentsException
     */
    public ObjectGUI(double X, double Y, Obj object, Pane parent) throws IllegalArgumentsException {
        super();

        this.parent = parent;
        this.object = object;

        drawCircle(X, Y, object);                   //Graphical representation
        addHandlers(parent);     //Event Handling
        this.setCursor(Cursor.HAND);                //Cursor Icon, I think it's neat!
    }

    /**
     *
     * @return  Returns the size in pixels of the ray of the circle representing the ObjectGUI
     */
    public double getRay() {
        return RAY;
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
    }

    /**
     * Adds various behaviours to the object,
     * @param parent    the parent pane on which the object is spawned
     */
    private void addHandlers(Pane parent)
    {
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
                    double offsetX = event.getSceneX() - xCord;
                    double offsetY = event.getSceneY() - yCord;

                    //TODO: also bound to the max X and Y of the pane
                    // God, being a 2D videogame programmer has its benefits...

                    double temp = this.getLayoutX() + offsetX;
                    this.setLayoutX(temp < 0.0f ? 0.0f : temp);

                    temp = this.getLayoutY() + offsetY;
                    this.setLayoutY(temp < 0.0f ? 0.0f : temp);

                    xCord = event.getSceneX();
                    yCord = event.getSceneY();
                    event.consume();
                });

        this.addEventHandler(MouseEvent.MOUSE_PRESSED,
                event -> {
                    xCord = event.getSceneX();
                    yCord = event.getSceneY();});
        this.addEventHandler(MouseEvent.MOUSE_RELEASED,
                event -> {
                    this.setCursor(Cursor.HAND);
                    event.consume();
                });
    }

    /**
     * Pings the ContextMenu at coordinates (X, Y)
     *
     * @param X X coord of the contextMenu
     * @param Y Y coord of the contextMenu
     * @param parent    parent pane of the ContextMenu
     * @throws IllegalArgumentsException
     */
    private void generateContext(Double X, Double Y, Pane parent) throws IllegalArgumentsException {
        /*
        Creation of the object contextMenu, wordy.
         */
        String[] items = {"Spawn Morphism", "Remove Object"};
        EventHandler[] actions = {
                (event -> {
                    String name = GUIutil.spawnPrompt("Name: ", "Insert Morphism Name");
                }),
                /*
                        We bubble the event to parent level in order to have access to the currCat object
                */
                (event -> {
                    System.out.println(parent);
                    parent.fireEvent(new OBJECT_DELETED(this.object));
                })
        };

        GUIutil.pingCreationMenu(X, Y, parent, items, actions);
        }

    /**
     * removes the Object from the parent, leaving it at the mercy of the Garbage Collection
     */
    private void removeObjGui()
    {
        this.parent.getChildren().remove(this);
    }

    /**
     *
     * @return returns the Model representation of this graphical object
     */
    public Obj getObject() {
        return object;
    }
}
