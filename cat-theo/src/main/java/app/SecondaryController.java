package app;

import java.io.IOException;
import javafx.fxml.FXML;

/**
 * Java-fx controller for the secondary window panel
 *
 * (Is this stuff deprecated?)
 */
public class SecondaryController {

    /**
     * Switches the view to the Primary panel
     * @throws IOException
     */
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}