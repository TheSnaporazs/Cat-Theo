package app.controllers;


import app.categories.Category;
import app.categories.Obj;
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

    @FXML
    private void addObj(ActionEvent e)
    {
        /*
        TODO get object name from View
        We do it twice just to be able to then
        test the addArr method, final implementation
        will of course only perform one addition.
         */
        currCat.addObject("A");
        currCat.addObject("B");
    }

    private void addArr(ActionEvent a)
    {
        // TODO get arrow attributes from View

    }



    /**
     * Debug method, prints to terminal the contents of the current category
     * being displayed
     */
    public void printCurrCat()
    {
        currCat.printAllObjects();
    }
}