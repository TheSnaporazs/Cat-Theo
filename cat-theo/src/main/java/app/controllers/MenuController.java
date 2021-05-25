package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Java-FX controller for the main menu page
 *
 * @author Dario
 * @since 30/04/2021
 */
public class MenuController extends GenericController {
    @FXML Button NewCat;
    @FXML ImageView CatView;
    @FXML Button LoadCat;
    @FXML AnchorPane root;
    public MenuController()
    {

    }

    @FXML
    public void initialize() {
        CatView.fitWidthProperty().bind(root.maxWidthProperty());
        CatView.fitHeightProperty().bind(root.prefHeightProperty());

        NewCat.hoverProperty().addListener((event) -> {
            Image image = new Image("/Theo-up.png");
            CatView.setImage(image);
        });

        LoadCat.hoverProperty().addListener((event) -> {
            Image image = new Image("/Theo-down.png");
            CatView.setImage(image);
        });

    }


}