package app.GUI;

import app.events.ARROW_OPTION;
import app.events.ARROW_SELECTED;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;

public class MovableLabel extends StackPane {
    double xCord;
    double yCord;
    double coeff = .5f;
    double alpha = 0.1745f;
    private ArrGUI arrow;
    private double RAY = 40;
    private Label txt;
    private Pane parent;

    MovableLabel(ArrGUI arrow, Pane parent) {
        super();
        this.arrow = arrow;
        this.parent = parent;
        double x1 = arrow.src.getLayoutX();
        double y1 = arrow.src.getLayoutY();
        double x2 = arrow.trg.getLayoutX();
        double y2 = arrow.trg.getLayoutY();
        double dist = Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1, 2));
        double angle = GUIutil.computeAngle(arrow.src, arrow.trg);
        double X = arrow.src.getLayoutX() + Math.cos(alpha + angle) * coeff * dist;
        double Y = arrow.src.getLayoutY() + Math.sin(alpha + angle) * coeff * dist;
        drawCircle(X, Y);
        addHandlers();        //Event Handling
        this.setCursor(Cursor.HAND);//Cursor Icon, I think it's neat!
    }

    /**
     * Sets the text of this label, the name of the represented arrow
     * @param text
     */
    public void setText(String text) {
        txt.setText(text);
    }

    /**
     * Creates the circle for this label, in this case it will be transparent
     * since the circle is needed just for input from the user.
     * @param X
     * @param Y
     */
    private void drawCircle(double X, double Y)
    {
        Circle circle = new Circle(RAY, Color.TRANSPARENT);

        txt = new Label(arrow.getArrow().getName());
        txt.setFont(new Font(15));

        txt.setAlignment(Pos.CENTER);

        txt.setWrapText(true);

        this.getChildren().addAll(circle, txt);
        this.relocate(X, Y);
        parent.getChildren().add(this);
    }
    
    private void addHandlers() {
        this.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> {
                    if(event.getButton().equals(MouseButton.PRIMARY)) {
                        parent.fireEvent(new ARROW_SELECTED(arrow));
                        event.consume();
                    } else if(event.getButton().equals(MouseButton.SECONDARY)) {
                        Point2D position = this.localToScreen(new Point2D(0, 0));
                        parent.fireEvent(new ARROW_OPTION(position.getX(), position.getY(), arrow));
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

                    //Math Nadiona doesn't want you to know about
                    //I've sold my soul for this shit btw -Davide
                    double x1 = arrow.src.getLayoutX();
                    double y1 = arrow.src.getLayoutY();
                    double x2 = arrow.trg.getLayoutX();
                    double y2 = arrow.trg.getLayoutY();
                    if(arrow.getArrow().isEndomorphism()) {
                        x1 += arrow.src.getRay();
                        x2 -= arrow.src.getRay();
                    }
                    double t1 = currX + offsetX;
                    double t2 = currY + offsetY;
                    coeff = Math.sqrt((Math.pow(t1-x1,2) + Math.pow(t2-y1, 2)) / (Math.pow(x2-x1,2) + Math.pow(y2-y1, 2)));
                    alpha = Math.atan2(
                            t2-y1,
                            t1-x1) - GUIutil.computeAngle(arrow.src, arrow.trg);

                    this.setLayoutX(t1);
                    this.setLayoutY(t2);

                    arrow.processLineFromLabel();
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
}