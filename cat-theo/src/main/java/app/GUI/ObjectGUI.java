package app.GUI;

import app.categories.Obj;
import javafx.scene.Cursor;
import javafx.scene.control.MenuItem;
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

    public ObjectGUI(double X, double Y, Obj object) {
        super();

        final double[] xCord = new double[1];
        final double[] yCord = new double[1];

        Circle circle = new Circle(60,60,30, Color.WHITE);
        circle.setStroke(Color.BLACK);

        Text testo = new Text(object.getName());
        testo.setFont(new Font(30));

        this.getChildren().addAll(circle, testo);
        this.relocate(X, Y);

        object.setRepr(this); //After all we have to let the object know of this...

        this.setCursor(Cursor.HAND);
        this.setOnMousePressed((t) -> {
            switch (t.getButton())
            {
                case PRIMARY:
                    xCord[0] = t.getSceneX();
                    yCord[0] = t.getSceneY();
                    break;
                case SECONDARY:
                    GUIutil.spawnCreationMenu(
                            t.getSceneX(), t.getSceneY(),
                            "Spawn Morphism",
                            (event -> {
                                String name = GUIutil.spawnPrompt("Name: ", "Insert Morphism Name");

                            })
                    );
            }

        });
        this.setOnMouseDragged((t) -> {

            double offsetX = t.getSceneX() - xCord[0];
            double offsetY = t.getSceneY() - yCord[0];

            this.setLayoutX(this.getLayoutX() + offsetX);
            this.setLayoutY(this.getLayoutY() + offsetY);

            xCord[0] = t.getSceneX();
            yCord[0] = t.getSceneY();
        });
    }
}
