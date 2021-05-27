package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

import app.App;

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

        NewCat.hoverProperty().addListener((event) -> {
            Image image = new Image("/Theo-up.png");
            CatView.setImage(image);
        });

        LoadCat.hoverProperty().addListener((event) -> {
            Image image = new Image("/Theo-down.png");
            CatView.setImage(image);
        });

    }

    public void startWithLoad() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Standard", "*.json"),
                new FileChooser.ExtensionFilter("Any file", "*.*")
            );
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") +  "/saved_categories"));
        fileChooser.setTitle("Load new category");
        App.fileToLoad = fileChooser.showOpenDialog(root.getScene().getWindow());

        switchPage("workpage");
    }
}