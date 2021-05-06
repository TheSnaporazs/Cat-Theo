package app.controllers;


import app.categories.Category;
import app.categories.Obj;
import app.exceptions.BadObjectNameException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Java-FX controller for the work page
 *
 * @author Dario Loi
 * @since 30/04/2021
 */
public class WorkController extends GenericController{

    private Category currCat = new Category();

    @FXML private AnchorPane scroll_wrap;

    double xCord, yCord;

    public WorkController()
    {

    }

    /**
     * Updates the model by adding the object specifed by the user
     *
     * @param e Contains the attributes of the ActionEvent that invoked the method
     */
    @FXML
    private void addObj(ActionEvent e) throws BadObjectNameException {
        /*
        TODO get object name from View
        We do it twice just to be able to then
        test the addArr method, final implementation
        will of course only perform one addition.
         */
        currCat.addObject("A");
    }

    /**
     * Updates the model by adding the arrow specified by the user
     * @param e
     */
    private void addArr(ActionEvent e)
    {
        // TODO get arrow attributes from View

    }



    /**
     * Debug method, prints to terminal the contents of the current category
     * being displayed
     */
    public void printCurrCat()
    {
        // TODO add printAll method to category object
    }

    /**
     * Method to create a draggable circle
     *
     * stub for the creation of an object
     */
    @FXML
    public void createCircle(String stringa) {
        Circle circle = new Circle(60,60,30, Color.WHITE);
        circle.setStroke(Color.BLACK);
        Group CircleGroup = new Group();
        Text testo = new Text(stringa);
        testo.setFont(new Font(30));
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(circle, testo);
        stackPane.setLayoutX(30);
        stackPane.setLayoutY(30);
        CircleGroup.getChildren().add(stackPane);
        scroll_wrap.getChildren().add(CircleGroup);
        
        CircleGroup.setCursor(Cursor.HAND);
        CircleGroup.setOnMousePressed((t) -> {
            xCord = t.getSceneX();
            yCord = t.getSceneY();
        });
        CircleGroup.setOnMouseDragged((t) -> {
            double offsetX = t.getSceneX() - xCord;
            double offsetY = t.getSceneY() - yCord;

            CircleGroup.setLayoutX(CircleGroup.getLayoutX() + offsetX);
            CircleGroup.setLayoutY(CircleGroup.getLayoutY() + offsetY);

            xCord = t.getSceneX();
            yCord = t.getSceneY();
        });
    }

    
    public void getInput() {
        TextField tf = new TextField();

        tf.setLayoutX(150);
        tf.setLayoutY(100);
        tf.setPrefWidth(90);
        scroll_wrap.getChildren().add(tf);
        Button bot = new Button("Create");
        bot.setLayoutX(250);
        bot.setLayoutY(100);
        scroll_wrap.getChildren().add(bot);
        bot.setOnAction(event -> {
            String objName = tf.getText();
            createCircle(objName);
            scroll_wrap.getChildren().removeAll(tf,bot);
        });
    }

}
