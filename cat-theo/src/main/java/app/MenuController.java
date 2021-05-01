package app;

import java.io.IOException;
import javafx.fxml.FXML;

/**
 * Java-FX controller for the main menu page
 *
 * @author Dario
 * @since 30/04/2021
 */
public class MenuController {

    /**
     * Switches the view to the Secondary panel
     * @throws IOException
     */
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("workpage");
    }
}
