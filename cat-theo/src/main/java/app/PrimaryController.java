package app;

import java.io.IOException;
import javafx.fxml.FXML;

/**
 * Java-FX controller for the primary window panel
 *
 * (Is this stuff deprecated?)
 */
public class PrimaryController {

    /**
     * Switches the view to the Secondary panel
     * @throws IOException
     */
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
