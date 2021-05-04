package app.controllers;


import app.categories.Category;
import app.categories.Obj;
import app.exceptions.BadObjectNameException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Java-FX controller for the work page
 *
 * @author Dario Loi
 * @since 30/04/2021
 */
public class WorkController extends GenericController{

    private Category currCat = new Category();

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


}