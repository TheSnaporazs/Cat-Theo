package app.GUI;

import java.util.Set;

import app.categories.Obj;
import app.controllers.WorkController;
import app.events.ARROW_SPAWNED_SOURCE;
import app.events.ARROW_SPAWNED_TARGET;
import app.events.OBJECT_DELETED;
import app.events.OBJECT_SELECTED;
import app.exceptions.IllegalArgumentsException;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
    public double xCord;
    double yCord;
    Set<ArrGUI> guis;
    public Label txt;

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
        object.guiRepr = this;
        this.xCord = X;
        this.yCord = Y;

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


        txt = new Label(object.getName());
        txt.setFont(new Font(30));

        txt.setMaxWidth(this.getRay() * Math.sqrt(2));
        txt.setMaxHeight(this.getRay() * Math.sqrt(2));
        txt.setAlignment(Pos.CENTER);

        txt.setWrapText(true);

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
                        } catch (IllegalArgumentsException e) {
                            System.out.println(e.getMessage() + "\n" + "error got while creating context menu for object");
                            e.printStackTrace();
                        }
                        event.consume();
                    }
                    if(event.getButton().equals(MouseButton.PRIMARY)) {
                        parent.fireEvent(new OBJECT_SELECTED(this));

                        if(WorkController.isCreatingArrow())    //ouch
                        {
                            ARROW_SPAWNED_SOURCE lastEvent = WorkController.getCurrSource();
                            parent.fireEvent(new ARROW_SPAWNED_TARGET(
                                    lastEvent.getSrc(), this, lastEvent.getName()));
                        }
                        event.consume();
                    }
                    });

        this.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                event -> {
                    this.setCursor(Cursor.MOVE);
                    double offsetX = event.getSceneX() - xCord;
                    double offsetY = event.getSceneY() - yCord;
                    double currX = this.getLayoutX();
                    double currY = this.getLayoutY();

                    // God, being a 2D videogame programmer has its benefits...
                    double temp = currX + offsetX;
                    if(temp < 0.0f)
                        offsetX = -currX;
                    else if(temp > parent.getWidth())
                        offsetX = parent.getWidth() - currX;

                    temp = currY + offsetY;
                    if(temp < 0.0f)
                        offsetY = -currY;
                    else if(temp > parent.getHeight())
                        offsetY = parent.getHeight() - currY;

                    this.setLayoutX(currX + offsetX);
                    this.setLayoutY(currY + offsetY);

                    for(ArrGUI arr: guis) {
                        arr.processLine();
                    }

                    xCord = event.getSceneX();
                    yCord = event.getSceneY();
                    event.consume();
                });

        this.addEventHandler(MouseEvent.MOUSE_PRESSED,
                event -> {
                    xCord = event.getSceneX();
                    yCord = event.getSceneY();
                    guis = object.getArrowGUIs();});
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
                    parent.fireEvent(new ARROW_SPAWNED_SOURCE(this, name));
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
    public void removeObjGui()
    {
        this.parent.getChildren().remove(this);
    }

    /**
     *
     * @return returns the Model representation of this graphical object
     */
    public Obj getObject() { return object; }

    /**
     *
     * @return a Point2D representing the center of the object
     */
    public Point2D getCenter()
    {
        return new Point2D(getLayoutX() + getRay(), getLayoutY() + getRay());
    }
    public String getxCord() {return String.valueOf(xCord); }

    public String getyCord() {return String.valueOf(yCord); }

    public void setxCord(String x) {
        xCord = Double.parseDouble(x);
        this.setLayoutX(xCord);
    }

    public void setyCord(String y) {
        yCord = Double.parseDouble(y);
        this.setLayoutY(yCord);
    }

}
