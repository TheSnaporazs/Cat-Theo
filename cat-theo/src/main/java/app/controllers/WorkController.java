package app.controllers;


import app.GUI.*;
import app.GUI.ToolBar;
import app.categories.Arrow;
import app.categories.Category;
import app.events.ARROW_SELECTED;
import app.events.ARROW_SPAWNED_SOURCE;
import app.events.ARROW_SPAWNED_TARGET;
import app.events.COMPOSITION_SPAWNED;
import app.events.OBJECT_DELETED;
import app.events.OBJECT_SPAWNED;
import app.events.OBJECT_SELECTED;
import app.exceptions.BadObjectNameException;
import app.exceptions.BadSpaceException;
import app.exceptions.IllegalArgumentsException;
import app.exceptions.ImpossibleArrowException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;

import java.util.ArrayList;
import java.util.Optional;
import java.io.File;
import java.io.IOException;


/**
 * Java-FX controller for the work page
 *
 * @author Dario Loi
 * @since 30/04/2021
 */
public class WorkController extends GenericController{
    public static ContextMenu CtxMenu = new ContextMenu();
    private static Category currCat;
    private static ObjectGUI currObj;
    private static ArrGUI currArr;

    @FXML private AnchorPane scroll_wrap;
    @FXML private RadioButton Mor;
    @FXML private RadioButton Epi;
    @FXML private RadioButton Mono;
    @FXML private RadioButton Iso;

    @FXML private ScrollPane pannable;
    @FXML private AnchorPane root;

    @FXML private TextField NameFieldObj;
    @FXML private TextField NameFieldArr;
    @FXML private TextField SourceField;
    @FXML private TextField TargetField;
    @FXML private TextField XField;
    @FXML private TextField YField;
    @FXML private AnchorPane ObjInsp;
    @FXML private AnchorPane ArrInsp;
    @FXML private ComboBox<String> combor;
    @FXML private ComboBox<String> comboi;
    @FXML private ComboBox<String> combogg;

    @FXML private Button addSpace;
    @FXML private TextField spaceField;

    private static boolean isCreatingArrow = false;
    private static ARROW_SPAWNED_SOURCE currSource;
    private Line tempArrow = new Line();

    public WorkController()
    {
        currCat = new Category("UniverseName");
    }

    @FXML
    public void initialize() {

        tempArrow.setMouseTransparent(true);
        NameFieldObj.setEditable(false);

        // Mapping right click to a context menu
        scroll_wrap.addEventHandler(MouseEvent.MOUSE_CLICKED,
            event -> {
                switch(event.getButton()) {
                    case PRIMARY:
                        // Hide inspector on clicking ground
                        ObjInsp.setVisible(false);
                        ArrInsp.setVisible(false);

                        currObj = null;
                        currArr = null;
                        break;
                    case SECONDARY:
                        String[] items = {"Create Object"};
                        EventHandler[] actions = {
                            ((event1) -> {
                                String name = GUIutil.spawnPrompt("Name: ", "Insert Object Name");
                                scroll_wrap.fireEvent(new OBJECT_SPAWNED(event.getX(),
                                        event.getY(), name));
                            })
                        };
                        try {
                            GUIutil.pingCreationMenu(event.getScreenX(), event.getScreenY(), scroll_wrap, items, actions);
                        } catch (IllegalArgumentsException e) {
                            System.out.println("Something went wrong in the contextMenu init! " +
                                    "(This shouldn't really happen!)");
                            e.printStackTrace();
                        }
                        event.consume();
                        break;
                    default:
                        CtxMenu.hide();
                        event.consume();
                        break;
                }

            });
        
        // Mapping left-button-drag to panning the scroll pane
        scroll_wrap.addEventHandler(MouseEvent.MOUSE_DRAGGED,
            event -> {
                if(event.getButton() != MouseButton.PRIMARY)
                    event.consume();
            });

        scroll_wrap.addEventHandler(OBJECT_SPAWNED.OBJECT_SPAWNED_TYPE, event -> {
            try {
                scroll_wrap.getChildren().add(
                        new ObjectGUI(event.getX(), event.getY(), currCat.addObject(event.getObjName()), scroll_wrap)
                );  //Woah that's a lot!
                printCurrCat();
            } catch (BadObjectNameException e) {
                e.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("Duplicate Object Error");
                error.setContentText("Cannot have two objects with the same name in the same category!");
                error.showAndWait();
            } catch (IllegalArgumentsException e) {

                e.printStackTrace();
                System.out.println("Something went wrong in the contextMenu init! (This shouldn't really happen!)");
            }
        });

        /*
        Add a line that chases the mouse when creating an arrow
         */
        scroll_wrap.addEventHandler(MouseEvent.MOUSE_MOVED, (event) ->
        {
            if(isCreatingArrow)
            {
                double MouseX = event.getX();
                double MouseY = event.getY();

                tempArrow.setEndX(MouseX);
                tempArrow.setEndY(MouseY);
            }
        });

        /*
        Start a morphism creation from a source object (waits for user to click on the target object
         */
        scroll_wrap.addEventHandler(ARROW_SPAWNED_SOURCE.ARROW_SPAWNED_SOURCE_TYPE, event -> {
            System.out.println("This happened!");

            currSource = event;

            Point2D objCenter = event.getSrc().getCenter();

            tempArrow.setStartX(objCenter.getX());
            tempArrow.setStartY(objCenter.getY());

            tempArrow.setEndX(objCenter.getX());
            tempArrow.setEndY(objCenter.getY());

            scroll_wrap.getChildren().add(tempArrow);

            isCreatingArrow = true;

        });

        //Spawn a morphism
        scroll_wrap.addEventHandler(ARROW_SPAWNED_TARGET.ARROW_SPAWNED_TARGET_TYPE, event -> {
            scroll_wrap.getChildren().remove(tempArrow);
            isCreatingArrow = false;

            try {
                ObjectGUI src = event.getSrc();
                ObjectGUI trg = event.getTrg();

                double[] src_coord = {src.getLayoutX(), src.getLayoutY()};
                double[] trg_coord = {trg.getLayoutX(), trg.getLayoutY()};


                scroll_wrap.getChildren().add(
                        new ArrGUI(src, trg,
                                currCat.addArrow(event.getName(),src.getObject(), trg.getObject()), scroll_wrap)
                );


            } catch (ImpossibleArrowException e) {
                e.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("Duplicate Arrow Error");
                error.setContentText("Cannot have two arrows with the same name in the same category!");
                error.showAndWait();
            } catch (BadSpaceException e) {
                e.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("Space error");
                error.setContentText("Some things related to spaces had an error while creating this arrow.");
                error.showAndWait();
            }
            printCurrCat();
        });

        //Spawn a composition
        scroll_wrap.addEventHandler(COMPOSITION_SPAWNED.COMPOSITION_SPAWNED_TYPE, event -> {
            try {
                Arrow g = event.getG();
                Arrow f = event.getF();
                ObjectGUI src = f.src().getRepr();
                ObjectGUI trg = g.trg().getRepr();

                double[] src_coord = {src.getLayoutX(), src.getLayoutY()};
                double[] trg_coord = {trg.getLayoutX(), trg.getLayoutY()};


                scroll_wrap.getChildren().add(
                        new ArrGUI(src, trg,
                        currCat.addComposition(g, f), scroll_wrap)
                );


            } catch (ImpossibleArrowException e) {
                e.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("Impossible Composition!");
                error.setContentText("The proposed composition is not feasible!");
                error.showAndWait();
            } catch (BadObjectNameException e) {
                e.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("Object error");
                error.setContentText("Some things related to objects had an error while creating this composition.");
                error.showAndWait();
            }
            printCurrCat();
        });

        //Remove object
        scroll_wrap.addEventHandler(OBJECT_DELETED.OBJECT_DELETED_TYPE, event -> {
                currCat.removeObject(event.getObject());
                if(event.getObject().getRepr().equals(currObj))
                {
                    currObj = null;
                    ObjInsp.setVisible(false);
                }

        });

        //Load object to inspector
        scroll_wrap.addEventHandler(OBJECT_SELECTED.OBJECT_SELECTED_TYPE, event -> {

            currObj = event.getObj();
            currArr = null;

            ObjInsp.setVisible(true);
            ArrInsp.setVisible(false);
            System.out.println(currObj.getObject().getSubspaces());
            System.out.println(currObj.getObject().getDomain().getName());


            ToolBar.updateToolBar(currObj, NameFieldObj, XField, YField, combogg);
        });

        //Load arrow to inspector
        scroll_wrap.addEventHandler(ARROW_SELECTED.ARROW_SELECTED_TYPE, event -> {

            currArr = event.getArr();
            currObj = null;

            ArrInsp.setVisible(true);
            ObjInsp.setVisible(false);
            combor.getItems().clear();
            comboi.getItems().clear();
            ToolBarArr.updateToolBArr(currArr, NameFieldArr, SourceField, TargetField,Mor,Epi,Mono, Iso, combor, comboi);
            System.out.println("range: " + currArr.getArrow().getRange().getName());
            System.out.println("image: " + currArr.getArrow().getImage().getName());

        });

        //add Space to object and refresh toolbar
        addSpace.setOnAction(actionEvent ->
        {
            try {
                currCat.addSpace(spaceField.getText(), currObj.getObject());
                ToolBar.updateToolBar(currObj, NameFieldObj, XField, YField, combogg);
            } catch (BadSpaceException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Debug method, prints to terminal the contents of the current category
     * being displayed
     *
     */
    public void printCurrCat()
    {
        currCat.printObjects();
        currCat.printArrows();
    }

    /**
     * Action called from the view, provides a simple method to instantiate an object at a generic
     * location of the view without having to do particular GUI interactions
     */
    @FXML
    private void objectFromMenu()
    {
        //all of this GUI voodoo is to properly spawn the object on the center of the currently visible scrollpane
        Bounds bounds = pannable.getViewportBounds();
        scroll_wrap.fireEvent(new OBJECT_SPAWNED(bounds.getCenterX(),
                bounds.getCenterY(),
                GUIutil.spawnPrompt("Name: ", "Insert Object Name")));
    }

    @FXML
    private void arrowFromMenu()
    {
        String[] msgs = {"Source Object", "Target Object", "Arrow Name"};
        Dialog<ArrayList<String>> prompt = GUIutil.spawnMultiPrompt(msgs,"Spawn Arrow");

        // The fact that I am using this is giving me an aneurysm the same way that seeing it is giving it to you
        Optional<ArrayList<String>> objects = prompt.showAndWait();
        ArrayList<String> list = objects.orElseThrow(NullPointerException::new);


        scroll_wrap.fireEvent(new ARROW_SPAWNED_TARGET(
                (currCat.getObject(list.get(0))).getRepr(),   //Source object
                (currCat.getObject(list.get(1))).getRepr(),   //Target object
                list.get(2)));                              //Arrow  Name
    }

    @FXML
    private void compositionFromMenu() {
        String[] msgs = {"Outer arrow", "Inner arrow"};
        Dialog<ArrayList<String>> prompt = GUIutil.spawnMultiPrompt(msgs,"Spawn Arrow");

        // The fact that I am using this is giving me an aneurysm the same way that seeing it is giving it to you
        Optional<ArrayList<String>> objects = prompt.showAndWait();
        ArrayList<String> list = objects.orElseThrow(NullPointerException::new);

        scroll_wrap.fireEvent(new COMPOSITION_SPAWNED(
                currCat.getArrow(list.get(0)),
                currCat.getArrow(list.get(1))));
    }

    @FXML
    private void checkCommutativity() {
        if(currCat.commutes())
            System.out.println("Commutes");
        else
            System.out.println("Does not commute");
    }

    @FXML
    private void saveCategory() {
        // Not the best looking thing, but I mean
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Standard", "*.json"),
                new FileChooser.ExtensionFilter("Any file", "*.*")
            );
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/saved_categories"));
        fileChooser.setTitle("Save current category");
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());
        try {
            currCat.save(file);
        } catch (IOException e){
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("Save error");
            error.setContentText("Something went wrong while saving.");
            error.showAndWait();
        }
    }

    private void loadCategory(File file) {
        try {
            currCat = Category.loadForGUI(file, scroll_wrap);
        } catch (Exception e){
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("Load error");
            error.setContentText("Something went wrong while loading.");
            error.showAndWait();
        }
    }

    @FXML
    public void loadCategory() {
        // Not the best looking thing, same as above pretty much
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Standard", "*.json"),
                new FileChooser.ExtensionFilter("Any file", "*.*")
            );
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") +  "/saved_categories"));
        fileChooser.setTitle("Load new category");
        loadCategory(fileChooser.showOpenDialog(root.getScene().getWindow()));
    }

    public static boolean isCreatingArrow() {
        return isCreatingArrow;
    }

    public static ARROW_SPAWNED_SOURCE getCurrSource()
    {
        return currSource;
    }

    public void getInp() {
        System.out.println(currObj.getObject().getName());
        System.out.println(NameFieldObj.getText());
        if (NameFieldObj.getText()!= currObj.getObject().getName()) {
            try {
                currCat.objectChangeName(currObj.getObject(), NameFieldObj.getText());
            } catch (BadObjectNameException e) {
                e.printStackTrace();
            } catch (BadSpaceException e) {
                e.printStackTrace();
            }
        }
    }

    public void getInp2() {
        System.out.println(currArr.getArrow().getName());
        System.out.println(NameFieldObj.getText());
        if (NameFieldObj.getText()!= currArr.getArrow().getName()) {
            try {
                currCat.arrowChangeName(currArr.getArrow(), NameFieldArr.getText());
            } catch (BadObjectNameException e) {
                e.printStackTrace();
            } catch (BadSpaceException e) {
                e.printStackTrace();
            }
        }
    }

    public void inpX() {
        currObj.setxCord(XField.getText());
    }

    public void inpY() {
        currObj.setyCord(YField.getText());

    }
}
