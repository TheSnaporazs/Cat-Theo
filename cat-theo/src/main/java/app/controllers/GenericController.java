package app.controllers;

import app.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

import java.io.IOException;

/**
 * GenericController.java
 *
 * Abstract class that describes default behaviours of a generic window controller
 * prevents boilerplate by specifying common methods on a superclass level
 *
 * @author Dario Loi
 * @since 01/05/2021
 */
public abstract class GenericController {

    /**
     * Switches the scene from the current one to the one specified by the userData of the caller button
     *
     * @param event An event thrown on button click
     * @throws IOException
     */
    @FXML
    private void switchPage(ActionEvent event) throws IOException{
        /*
        Get the event object, cast it into a string representing the destination scene
         */
        String destination = null;

        if ( event.getSource() instanceof Node) {
            destination = (String) ((Node) event.getSource()).getUserData();
        } else if (event.getSource() instanceof MenuItem) {
            destination = (String) ((MenuItem) event.getSource()).getUserData();
        }

        App.setRoot(destination);

    }

    public GenericController() {

    }
}
