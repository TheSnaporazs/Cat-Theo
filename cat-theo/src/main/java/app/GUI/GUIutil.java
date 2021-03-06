package app.GUI;

import app.GUI.Bindings.FieldsBounding;
import app.GUI.Bindings.VEC;
import app.categories.Category;
import app.categories.Space;
import app.controllers.WorkController;
import app.exceptions.BadSpaceException;
import app.exceptions.IllegalArgumentsException;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

/**
 * GUIutil.java
 * Singleton to provide spawning methods of javafx objects
 * to avoid boilerplate implementations in each page's controllers
 *
 * @author Dario Loi
 * @since 07/05/2021
 * @see app.controllers
 */
public class GUIutil {

    /**
     * Positions the context menu abd prompts the user with generic options provided in the function's arguments
     * the Strings and the corresponding events must be paired at the same index in the two arrays.
     * @see app.categories.Obj
     * @see app.categories.Arrow
     * @param x X to position at
     * @param y Y to position at
     * @param parent The parent pane upon which to ping the contextMenu
     * @param items An array of strings to be displayed as menuitems on the contextMenu
     * @param actions An array of EventHandlers to be attached to each menuitems on the contextmenu
     * @throws IllegalArgumentsException
     */
    public static void pingCreationMenu(Double x, Double y, Pane parent, String[] items, EventHandler[] actions) throws IllegalArgumentsException {
        if(items.length != actions.length)
        {
            throw new IllegalArgumentsException("The Menu cannot have an unequal amount of items and actions!");
        }

        ArrayList<MenuItem> mItems = new ArrayList<MenuItem>();
        for(int c = 0; c < items.length; c++)
        {
            mItems.add(new MenuItem(items[c]));
            mItems.get(c).setOnAction(actions[c]);
        }
        WorkController.CtxMenu.getItems().clear();
        WorkController.CtxMenu.getItems().addAll(mItems);
        WorkController.CtxMenu.setAutoHide(true); //Dunno what this does
        WorkController.CtxMenu.show(parent, x, y);
    }


    /**
     * Wrapper method for spawnCreationMenu, provides an automatic implementation
     * of the ugly button trick utilized to pop a ContextMenu on a scrollPane
     * so that I do not have to look at it directly when implementing it
     * (sorry for the boilerplate, still friends? <3)
     *
     * WARNING: the button trick is not necessary anymore, one can instantiate
     * the contextMenu and then call the show method on a parent object
     * to cleanly display it without need for such voodoo tricks, therefore
     * this method is deprecated.
     * @see ContextMenu
     * @see Pane
     * @param X Double, the X coordinate at which to spawn the menu at
     * @param Y Double, the Y coordinate at which to spawn the menu at
     * @param items     Array of Strings, contains all the different text options
     *                  to be displayed on the menu
     * @param actions   Array of Eventhandlers, contains all the different actions
     *                  to be performed by the option
     * @param parent    Any child of class Pane, provides a parent upon which to attach
     *                  the button to do the ugly trick
     */
    @Deprecated
    public static void ButtonMenu(double X, double Y, String[] items, EventHandler[] actions, Pane parent)
    {
        Button temp = new Button();
        temp.relocate(X, Y);

        try {
            GUIutil.pingCreationMenu(X, Y, parent, items, actions);
        } catch (IllegalArgumentsException e) {
            e.printStackTrace();
        }

        temp.setContextMenu(WorkController.CtxMenu);
        temp.fire();
        WorkController.CtxMenu.setOnHidden((event1) -> {
            parent.getChildren().remove(temp);
        });

        parent.getChildren().add(temp);
    }



    /**
     * Utility method to spawn a prompt (TextInputDialog), the prompts acts as an interrupt, waiting for the input
     * to be provided before resuming the program's execution
     *
     * @see TextInputDialog
     * @param msg   a String to be displayed as a message to the user
     * @param Title a String to be used as the prompt window's title
     * @return      the String inserted by the user
     */
    public static TextInputDialog spawnPrompt(String msg, String Title) {

        TextInputDialog prova = new TextInputDialog();

        BooleanBinding txtFilled = Bindings.createBooleanBinding(() ->
                prova.getEditor().getText() == "", prova.getEditor().textProperty());

        prova.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(txtFilled);

        prova.setTitle(Title);
        prova.setGraphic(null);
        prova.setHeaderText("");
        prova.setContentText(msg);

        prova.setResultConverter(dialogButton -> {
            switch (dialogButton.getButtonData())
            {
                case OK_DONE:
                    return prova.getEditor().getText();
                default:
                    System.out.println("Cancelled!");
                    return null;
            }
        });

        return prova;
    }

    /**
     * Spawns a prompt for multiple user inputs
     *
     * @param msgs      Array of strings, each of which will generate an input textfield to prompt the user for input
     * @param Title     String, Title of the dialog
     * @return
     */
    public static Dialog<ArrayList<String>> spawnMultiPrompt(String[] msgs, String Title) {

        Dialog<ArrayList<String>> prompt = new Dialog<>();
        prompt.setTitle(Title);

        prompt.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ArrayList<TextField> fields = new ArrayList();
        for(int c = 0; c < msgs.length; c++)
        {
            TextField field = new TextField();
            fields.add(field);

            field.setPromptText(msgs[c]);
            grid.add(new Label(msgs[c] + ":"), 0, c);
            grid.add(field, 1, c);
        }

        prompt.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(new FieldsBounding(fields));
        prompt.getDialogPane().setContent(grid);

        prompt.setResultConverter(dialogButton -> {
            switch (dialogButton.getButtonData())
            {
                case OK_DONE:
                        ArrayList<String> returns = new ArrayList<>();
                        fields.forEach(textField -> returns.add(textField.getText()));
                        return returns;
                default:
                    System.out.println("Cancelled!");
                    return null;
            }
        });

        return prompt;
    }

    /**
     * Wrapper for the usual arctan method to compute the angle between two objects
     * @param A The source Object
     * @param B The target Object
     * @return  a Double alpha, between -pi and pi, representing the angle between A and B
     */
    public static double computeAngle(Node A, Node B)
    {
        if(A == B)
            return 1f;
        return Math.atan2(
                        B.getLayoutY() - A.getLayoutY(),
                        B.getLayoutX() - A.getLayoutX());
    }

    /**
     * Updates the inspector to show an ObjGUI object, loading all it's parameters
     * in the corresponding graphical components
     *
     * @param obj           the ObjGUI to load
     * @param NameField     TextField containing the name of the Object
     * @param XField        The X position of the object in the graphical pane
     * @param YField        The Y position of the object in the graphical pane
     * @param combogg       ComboBox containing all the spaces and subspaces of the object
     *
     * @see ObjectGUI
     * @see app.categories.Obj
     */
    public static void updateInspectObj(ObjectGUI obj, TextField NameField, TextField XField,
                                        TextField YField, ComboBox<String> combogg, TextField spaceText) {
        combogg.getItems().clear();
        combogg.getItems().removeAll();
        NameField.setEditable(true);
        NameField.setText(obj.getObject().getName());
        XField.setText(Double.toString(obj.getLayoutX()));
        YField.setText(Double.toString(obj.getLayoutY()));

        combogg.getItems().add(obj.getObject().getDomain().getName());

        for (Space space: obj.getObject().getSubspaces()) {
            combogg.getItems().add(space.getName());
        }

        spaceText.setText("");
    }

    /**
     * Updates the inspector to show an arrow object, loading all it's parameters
     * in the corresponding graphical components
     *
     * @param arr               The arrow to load
     * @param NameField         TextField containing the name of the arrow
     * @param SourceField       TextField containing the source object of the arrow
     * @param TargetField       TextField containing the target object of the arrow
     * @param Epi               RadioButton, true if the arrow is an epimorphism
     * @param Iso               RadioButton, true if the arrow is an isomorphism
     * @param Mono              RadioButton, true if the arrow is a monomorphism
     * @param combor            ComboBox, contains all the range subspaces of the arrow
     * @param comboi            ComboBox, contains all the images of the arrow
     * @see ArrGUI
     * @see app.categories.Arrow
     */
    public static void updateInspectArr(ArrGUI arr, TextField NameField, TextField SourceField, TextField TargetField, RadioButton Endo, RadioButton Epi, RadioButton Mono, RadioButton Iso, RadioButton Auto, ComboBox<String> combor, ComboBox<String> comboi, Category category) {
        comboi.setDisable(false);
        combor.setDisable(false);
        NameField.setEditable(true);
        NameField.setText(arr.getArrow().getName());
        SourceField.setText(arr.getSrc().getObject().getName());
        TargetField.setText(arr.getTrg().getObject().getName());

        if (arr.getArrow().isEndomorphism() ^ Endo.isSelected())
            Endo.fire();
        if (arr.getArrow().isEpic() ^ Epi.isSelected())
            Epi.fire();
        if (arr.getArrow().isMonic() ^ Mono.isSelected())
            Mono.fire();
        if (arr.getArrow().isIsomorphism() ^ Iso.isSelected())
            Iso.fire();
        if (arr.getArrow().isAutomorphism() ^ Auto.isSelected())
            Auto.fire();

        List<String> ranges = new ArrayList<String>();
        ranges.add(arr.getArrow().src().getDomain().getName());
        for(Space spce: arr.getArrow().src().getSubspaces())
            ranges.add(spce.getName());
        combor.getItems().addAll(ranges);
        combor.getSelectionModel().select(arr.getArrow().getRange().getName());

        combor.setOnAction(actionEvent ->
        {
            String newSpace = combor.getSelectionModel().getSelectedItem();
            try {
                if(!newSpace.equals(arr.getArrow().getRange().getName())) {
                    if(newSpace.equals(arr.getArrow().src().getDomain().getName()))
                        category.arrowChangeRange(arr.getArrow(), arr.getArrow().src().getDomain());
                    else
                        category.arrowChangeRange(arr.getArrow(), newSpace);
                    combor.setOnAction(null);
                    comboi.setOnAction(null);
                    combor.getItems().clear();
                    comboi.getItems().clear();
                    updateInspectArr(arr, NameField, SourceField, TargetField, Endo, Epi, Mono, Iso, Auto, combor, comboi, category);
                }
            } catch (BadSpaceException e){
                e.printStackTrace();
                System.err.println("Impossible...");
            }
        });

        List<String> images = new ArrayList<String>();
        images.add(arr.getArrow().trg().getDomain().getName());
        for(Space spce: arr.getArrow().trg().getSubspaces())
            images.add(spce.getName());
        comboi.getItems().addAll(images);
        comboi.getSelectionModel().select(arr.getArrow().getImage().getName());

        comboi.setOnAction(actionEvent ->
        {
            String newSpace = comboi.getSelectionModel().getSelectedItem();
            try {
                if(!newSpace.equals(arr.getArrow().getImage().getName())) {
                    if(newSpace.equals(arr.getArrow().trg().getDomain().getName()))
                        category.arrowChangeImage(arr.getArrow(), arr.getArrow().trg().getDomain());
                    else
                        category.arrowChangeImage(arr.getArrow(), newSpace);
                    combor.setOnAction(null);
                    comboi.setOnAction(null);
                    combor.getItems().clear();
                    comboi.getItems().clear();
                    updateInspectArr(arr, NameField, SourceField, TargetField, Endo, Epi, Mono, Iso, Auto, combor, comboi, category);
                }
            } catch (BadSpaceException e){
                e.printStackTrace();
                System.err.println("Impossible...");
            }
        });

        if(arr.getArrow().isComposition() || arr.getArrow().isIdentity()) {
            comboi.setDisable(true);
            combor.setDisable(true);
        }
    }
}
