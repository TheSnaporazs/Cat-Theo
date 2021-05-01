package app;

import java.io.IOException;
import java.util.ArrayList;

import app.categories.*;
import javafx.fxml.FXML;

/**
 * Java-FX controller for the work page
 *
 * @author Dario
 * @since 30/04/2021
 */
public class WorkController {

    Category currCat = new Category() {
    }

    /**
     * Switches the view to the Primary panel
     * @throws IOException
     */
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("mainmenu");
    }

    
}