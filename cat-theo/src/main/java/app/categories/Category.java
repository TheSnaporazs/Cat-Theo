package app.categories;

import app.GUI.ArrGUI;
import app.GUI.ObjectGUI;
import app.exceptions.BadObjectNameException;
import app.exceptions.BadSpaceException;
import app.exceptions.IllegalArgumentsException;
import app.exceptions.ImpossibleArrowException;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Represents a category from the Category Theory branch of mathematics.
 * @author Davide Marincione
 * @see {@link app.categories.Arrow Arrow}
 * @see {@link app.categories.Obj Obj}
 */
public class Category {
    HashMap<String, Arrow> arrows = new HashMap<String, Arrow>();
    HashMap<String, Obj> objects = new HashMap<String, Obj>();
    HashMap<String, Space> spaces = new HashMap<String, Space>();
    String universeName;

    public Category(String universeName) {
        this.universeName = universeName;
    }

    /**
     * Adds a new {@link app.categories.Arrow Arrow} with custom type
     * to the {@link app.categories.Category Category}.
     * @param name Name of the new arrow (watch out for a possible LaTeX implementation).
     * @param src Source object.
     * @param trg Target object.
     * @param type Type of the arrow.
     * @param inheritsDomains if it will hold as range and image the domains of its source and target.
     * @return Reference to the added arrow.
     * @throws ImpossibleArrowException
     * @see #removeArrow(Arrow)
     * @see app.categories.Arrow#Arrow(String, Obj, Obj, MorphType) Arrow(name, source, target, type)
     */
    public Arrow addArrow(String name, Obj src, Obj trg) throws BadSpaceException, ImpossibleArrowException {
        if(arrows.containsKey(name))
            throw new ImpossibleArrowException("An arrow with the same name already exists!");
    
        Arrow arr = new Arrow(name, src, trg, src.getDomain(), trg.getDomain());

        src.getDomain().toArrows.add(arr);
        trg.getDomain().toArrows.add(arr);

        src.outcoming.add(arr);
        trg.incoming.add(arr);

        arrows.put(name, arr);
        return arr;
    }

    /**
     * Adds a new {@link app.categories.Arrow Arrow} with custom type
     * to the {@link app.categories.Category Category}.
     * @param name Name of the new arrow (watch out for a possible LaTeX implementation).
     * @param srcName Name of the source object.
     * @param trgName Name of the target object.
     * @param type Type of the arrow.
     * @param inheritsDomains if it will hold as range and image the domains of its source and target.
     * @return Reference to the added arrow.
     * @throws BadObjectNameException If the object does not exist.
     * @throws ImpossibleArrowException
     * @see #removeArrow(Arrow)
     * @see app.categories.Arrow#Arrow(String, Obj, Obj, MorphType) Arrow(name, source, target, type)
     */
    public Arrow addArrow(String name, String srcName, String trgName) throws BadSpaceException, BadObjectNameException, ImpossibleArrowException {
        Obj src;
        Obj trg;

        // Add reference to source
        try {
            src = objects.get(srcName);
        } catch (NullPointerException e) {
            throw new BadObjectNameException("Source object does not exist in category.");
        }

        // Add reference to target
        try {
            trg = objects.get(trgName);
        } catch (NullPointerException e) {
            throw new BadObjectNameException("Target object does not exist in category.");
        }
    
        return addArrow(name, src, trg);
    }


    /**
     * Adds the identty {@link app.categories.Arrow Arrow} of an object to the {@link app.categories.Category Category}.
     * @param name Name of the new arrow (watch out for a possible LaTeX implementation).
     * @param obj Object to make the identity of.
     * @return Reference to the identity.
     * @throws ImpossibleArrowException If the identity already is in the category
     * @see #removeArrow(Arrow)
     * @see app.categories.Arrow#Arrow(String, Obj) Arrow(name, obj)
     */
    public Arrow addIdentity(String name, Obj obj) throws ImpossibleArrowException {
        if(arrows.containsKey(name))
            throw new ImpossibleArrowException("Identity already exists in the category.");
        Arrow arr = new Arrow(name, obj, obj, obj.getDomain(), obj.getDomain(), true);

        obj.getDomain().toArrows.add(arr);

        // Add reference to object
        obj.outcoming.add(arr);
        obj.incoming.add(arr);

        arrows.put(name, arr);
        return arr;
    }

    /**
     * Adds the identty {@link app.categories.Arrow Arrow} of an object to the {@link app.categories.Category Category}.
     * @param name Name of the new arrow (watch out for a possible LaTeX implementation).
     * @param objName Name of the object.
     * @return Reference to the identity.
     * @throws BadObjectNameException If the object does not exist.
     * @throws ImpossibleArrowException If the identity already is in the category
     * @see #removeArrow(Arrow)
     * @see app.categories.Arrow#Arrow(String, Obj) Arrow(name, obj)
     */
    public Arrow addIdentity(String name, String objName) throws BadObjectNameException, ImpossibleArrowException {
        Obj obj;
        
        try {
            obj = objects.get(objName);
        } catch (NullPointerException e) {
            throw new BadObjectNameException("Object does not exist in category.");
        }

        return addIdentity(name, obj);
    }

    /**
     * Adds a the identity {@link app.categories.Arrow Arrow} of an object to the {@link app.categories.Category Category}.
     * @param obj Object to make the identity of.
     * @return Reference to the identity.
     * @throws ImpossibleArrowException If an identity of the object is already in the category.
     * @see #removeArrow(Arrow)
     * @see app.categories.Arrow#Arrow(Obj) Arrow(obj)
     */
    public Arrow addIdentity(Obj obj) throws ImpossibleArrowException {
        return addIdentity(Arrow.makeIdentityName(obj.getName()), obj);
    }

    /**
     * Adds a the identity {@link app.categories.Arrow Arrow} of an object to the {@link app.categories.Category Category}.
     * @param objName Name of the object.
     * @return Reference to the identity.
     * @throws BadObjectNameException If the object does not exist.
     * @throws ImpossibleArrowException If an identity of the object is already in the category.
     * @see #removeArrow(Arrow)
     * @see app.categories.Arrow#Arrow(Obj) Arrow(obj)
     */
    public Arrow addIdentity(String objName) throws BadObjectNameException, ImpossibleArrowException {
        return addIdentity(Arrow.makeIdentityName(objName), objName);
    }


    /**
     * Changes range of an arrow and, if no other things depend on it,
     * removes the previous range from the category.
     * @param arr Arrow on which to apply the change
     * @param range New space that will be range
     */
    public void arrowChangeRange(Arrow arr, Space range) throws BadSpaceException {
        // Look at arrowChangeImage's comments for a better explaination.
        // on this method.

        if(arr.range != arr.image)
            arr.range.toArrows.remove(arr);
        
        Space temp = arr.range;
        arr.range = range;
        range.toArrows.add(arr);

        for(Arrow comp: arr.dependencies) {
            if(temp != comp.range) {
                temp.toCompositions.remove(comp);
                range.toCompositions.add(comp);
            }
            if(!comp.runCheck())
                removeArrow(comp, arr);
        }

        if(temp.hasNoRefers())
            removeSpace(temp);

        for(Arrow dep: arr.dependencies)
            if(dep.range == temp)
                arrowChangeRange(dep, range);
    }

    /**
     * Changes range of an arrow and, if no other things depend on it,
     * removes the previous range from the category.
     * @param arr Arrow on which to apply the change.
     * @param rangeName Name of the space that will be range (be it new or already present).
     * @throws BadSpaceException
     */
    public void arrowChangeRange(Arrow arr, String rangeName) throws BadSpaceException {
        Space range;
        if((range = spaces.get(rangeName)) == null) {
            if(arr.range == arr.src().getDomain())
                range = addSpace(rangeName, arr.src());
        }

        arrowChangeRange(arr, range);
    }

    /**
     * Changes range of an arrow and, if no other things depend on it,
     * removes the previous range from the category.
     * @param arrName Name of the arrow on which to apply the change.
     * @param rangeName Name of the space that will be range (be it new or already present).
     * @throws BadSpaceException
     */
    public void arrowChangeRange(String arrName, String rangeName) throws BadSpaceException  {
        Arrow arr;
        if((arr = arrows.get(arrName)) == null)
            throw new NullPointerException("No arrow with this name exists.");
        arrowChangeRange(arr, rangeName);
    }

    /**
     * Changes image of an arrow and, if no other things depend on it,
     * removes the previous image from the category.
     * @param arr Arrow on which to apply the change
     * @param image New space that will be image
     */
    public void arrowChangeImage(Arrow arr, Space image) throws BadSpaceException {
        // Otherwise may get problems with identities
        if(arr.range != arr.image)
            arr.image.toArrows.remove(arr);

        // Pass to new space
        Space temp = arr.image;
        arr.image = image;
        image.toArrows.add(arr);

        // Check compositions dependent on this
        // if they will still work
        for(Arrow comp: arr.dependencies) {
            if(temp != comp.image) {
                temp.toCompositions.remove(comp);
                image.toCompositions.add(comp);
            }

            if(!comp.runCheck())
                removeArrow(comp, arr);
        }

        //  Optionally remove this space
        if(temp.hasNoRefers())
            removeSpace(temp);

        for(Arrow dep: arr.dependencies)
            if(dep.image == temp)
                arrowChangeImage(dep, image);
    }

    /**
     * Changes image of an arrow and, if no other things depend on it,
     * removes the previous image from the category.
     * @param arr Arrow on which to apply the change.
     * @param imageName Name of the space that will be image (be it new or already present).
     * @throws BadSpaceException
     */
    public void arrowChangeImage(Arrow arr, String imageName) throws BadSpaceException {
        Space image;
        if((image = spaces.get(imageName)) == null) {
            if(arr.image == arr.trg().getDomain())
                image = addSpace(imageName, arr.trg());
        }

        arrowChangeImage(arr, image);
    }

    /**
     * Changes image of an arrow and, if no other things depend on it,
     * removes the previous image from the category.
     * @param arrName Name of the arrow on which to apply the change.
     * @param imageName Name of the space that will be image (be it new or already present).
     * @throws BadSpaceException
     */
    public void arrowChangeImage(String arrName, String imageName) throws BadSpaceException  {
        Arrow arr;
        if((arr = arrows.get(arrName)) == null)
            throw new NullPointerException("No arrow with this name exists.");
        arrowChangeImage(arr, imageName);
    }


    /**
     * Removes an {@link app.categories.Arrow Arrow} and all the other arrows that
     * compose (thus depend) on it from the {@link app.categories.Category Category}.
     * @param arr Reference to the arrow to remove.
     * @param noTouchAncestor Needed such that we won't change sets
     * being read at the same time.
     */
    public void removeArrow(Arrow arr, Arrow noTouchAncestor) {
        arrows.remove(arr.getName());

        // Remove reference from source
        if(objects.containsKey(arr.src().getName()))
            arr.src().outcoming.remove(arr);

        // Remove reference from target
        if(objects.containsKey(arr.trg().getName()))
            arr.trg().incoming.remove(arr);

        // Remove from the Category all the compositions depending on the
        // current arrow.
        for(Arrow comp: arr.dependencies)
            removeArrow(comp, arr);

        // Remove references from ancestors
        if(arr.firstAncestor != null) {
            if(noTouchAncestor != null) {
                if(arr.firstAncestor != noTouchAncestor)
                    arr.firstAncestor.dependencies.remove(arr);
                else
                    arr.secondAncestor.dependencies.remove(arr);
            } else {
                arr.firstAncestor.dependencies.remove(arr);
                arr.secondAncestor.dependencies.remove(arr);
            }
        }

        // Remove references from image and range
        arr.image.toArrows.remove(arr);
        if(arr.image.hasNoRefers())
            removeSpace(arr.image);

        arr.range.toArrows.remove(arr);
        if(arr.range.hasNoRefers())
            removeSpace(arr.range);

        // Remove references from ancestor's internal spaces
        if(arr.firstAncestor != null)
            arr.firstAncestor.image.toCompositions.remove(arr);
        if(arr.secondAncestor != null)
            arr.secondAncestor.range.toCompositions.remove(arr);

        if(arr.guiRepr != null)
            arr.guiRepr.removeArrGui();
    }

    /**
     * Removes an {@link app.categories.Arrow Arrow} and all the other arrows that
     * compose (thus depend) on it from the {@link app.categories.Category Category}.
     * @param arr Reference to the arrow to remove.
     */
    public void removeArrow(Arrow arr) {
        removeArrow(arr, null);
    }

    /**
     * Inserts in the category (if possible) a new {@link app.categories.Arrow Arrow}
     * result of the composition of two others.
     * @param g Reference to the latter function g:B->C
     * @param f Reference to the first function f:A->B
     * @return Reference to the composed arrow h:A->C, h = g(f) = g ○ f
     * @throws ImpossibleArrowException If the two given arrows can't be composed with each other.
     * @throws BadObjectNameException If for any reason the objects the given arrows
     * source or target to don't exist in the category.
     * reference to do not exist.
     * @see #compose(Arrow g, Arrow f)
     * @see #removeArrowCompositions(Arrow)
     */
    public Arrow addComposition(Arrow g, Arrow f) throws BadObjectNameException, ImpossibleArrowException {
        Arrow arr = Arrow.compose(g, f);

        // If here then g and f are composable, now we check for identity
        // (otherwise we do all the things such that we can add to category the new composition)
        if (g.isIdentity())  // If g is identity then g•f = f (whether f is identity or not)
            return f;
        if (f.isIdentity())  // If f is identity then g•f = g (if here then g is not id.)
            return g;

        
        // Add composition as dependent of f.src()
        arr.src().outcoming.add(arr);

        // Add composition as dependent of g.trg()
        arr.trg().incoming.add(arr);

        // Add the arrow as dependent of f and g
        f.dependencies.add(arr);
        g.dependencies.add(arr);
        arr.firstAncestor = f;
        arr.secondAncestor = g;

        f.range.toArrows.add(arr);
        g.image.toArrows.add(arr);

        f.image.toCompositions.add(arr);
        g.range.toCompositions.add(arr);

        arrows.put(arr.getName(), arr);
        return arr;
    }

    /**
     * Inserts in the category, if possible, the composition g(f)
     * @param g
     * @param f
     * @return A reference to the composition.
     * @throws BadObjectNameException If one or both of the two functions do not exist in the category
     * @throws ImpossibleArrowException If the composition is impossible
     */
    public Arrow addComposition(String g, String f) throws BadObjectNameException, ImpossibleArrowException {
        Arrow gArr;
        Arrow fArr;
        if((gArr = arrows.get(g)) == null)
            throw new BadObjectNameException("Arrow g does not exist in the category!");
        if((fArr = arrows.get(f)) == null)
            throw new BadObjectNameException("Arrow f does not exist in the category!");

        return addComposition(gArr, fArr);
    }

    /**
     * Removes all the {@link app.categories.Arrow Arrows} which compose from the given one.
     * @param arr Reference to the arrow from which the arrows to remove compose.
     * @see #addComposition(Arrow, Arrow)
     */
    public void removeArrowCompositions(Arrow arr) {
        for (Arrow comp: arr.dependencies)
            removeArrow(comp, arr);
        arr.dependencies.clear();
    }

    /**
     * Prints all the arrows currently present in the {@link app.categories.Category Category}.
     * @see #printObjects()
     */
    public void printArrows() {
        System.out.println("Snapshot of all the arrows:");
        for (Arrow arr: arrows.values())
            System.out.printf("\t%s\n",arr.represent());
    }

    /**
     * Adds a new Object to the {@link app.categories.Category Category}.
     * In Category Theory objects are called Obj.
     * @param name Name of the new object (watch out for a possible LaTeX implementation).
     * @param domain Reference to the specified domain to give the object.
     * @return
     * @throws BadObjectNameException If an object with the same name already exists in
     * the category.
     */
    public Obj addObject(String name) throws BadObjectNameException {
        if (objects.containsKey(name)) throw new BadObjectNameException("An object with the same name already exists in the category.");
        Obj obj = new Obj(name);
        objects.put(name, obj);
        return obj;
    }

    /**
     * Removes an Object and all the {@link app.categories.Arrow Arrows}
     * which source or target it from the {@link app.categories.Category Category}.
     * @param obj Object to remove.
     * @see #addObject(String name)
     */
    public void removeObject(Obj obj) {
        if(obj.getRepr() != null)
            ((Pane) obj.getRepr().getParent()).getChildren().remove(obj.getRepr());
            // Oh god Dario... why the hell did you make me do this...
        objects.remove(obj.getName());
        for(Arrow arr: obj.outcoming)
            removeArrow(arr);
        for(Arrow arr: obj.incoming)
            removeArrow(arr);

        if(obj.guiRepr != null)
            obj.guiRepr.removeObjGui();
    }

    /**
     * Removes an Object and all the {@link app.categories.Arrow Arrows}
     * which source or target it from the {@link app.categories.Category Category}.
     * @param objName Name of the object to remove.
     * @see #addObject(String name)
     */
    public void removeObject(String objName) {
        Obj obj;
        if ((obj = objects.remove(objName)) != null) {
            if(obj.getRepr() != null)
                ((Pane) obj.getRepr().getParent()).getChildren().remove(obj.getRepr());
            for(Arrow arr: obj.outcoming)
                removeArrow(arr);
            for(Arrow arr: obj.incoming)
                removeArrow(arr);

            if(obj.guiRepr != null)
                obj.guiRepr.removeObjGui();
        }
    }

    /**
     * Adds a new space to the category
     * @param spaceName Name of the new space
     * @param superSpace Custom super space of the space
     * @return a reference to the new space
     * @throws BadSpaceException
     */
    public Space addSpace(String spaceName, Obj superSpace) throws BadSpaceException {
        if(spaces.containsKey(spaceName))
            throw new BadSpaceException("A space with the same name already exists!");
        Space space = new Space(spaceName, superSpace, false);
        spaces.put(spaceName, space);
        superSpace.spaces.add(space);

        return space;
    }

    /**
     * Removes the space from the category, passes all its dependencies to its
     * super space
     * @param space
     */
    public void removeSpace(Space space) {
        for(Arrow arr: space.toArrows) {
            //Pretty much running a changeRange and changeImage 
            // in combo.
            if(arr.range == space) {
                arr.range = space.object.getDomain();
                arr.range.toArrows.add(arr);

                for(Arrow comp: arr.dependencies) {
                    if(space != comp.range) {
                        space.toCompositions.remove(comp);
                        arr.range.toCompositions.add(comp);
                    }
                }
            }
        
            if(arr.image == space) {
                arr.image = space.object.getDomain();
                arr.image.toArrows.add(arr);

                for(Arrow comp: arr.dependencies) {
                    if(space != comp.image) {
                        space.toCompositions.remove(comp);
                        arr.image.toCompositions.add(comp);
                    }
                }
            }
        }
        
        // Checking existance of the dependencies
        HashSet<Arrow> arrowsToRemove = new HashSet<Arrow>();
        for(Arrow arr: space.toArrows)
            for(Arrow comp: arr.dependencies)
                if(!comp.runCheck())
                    arrowsToRemove.add(comp);

        for(Arrow comp: arrowsToRemove)
            removeArrow(comp);

        spaces.remove(space.getName());
        space.object.spaces.remove(space);
    }

    /**
     * Removes the space from the category, passes all its dependencies to its
     * super space
     * @param spaceName
     * @throws BadSpaceException
     */
    public void removeSpace(String spaceName) throws BadSpaceException {
        Space space;
        if((space = spaces.get(spaceName)) == null)
            throw new BadSpaceException();

        removeSpace(space);
    }

    /**
     * Prints all the Objects currently in the {@link app.categories.Category Category}.
     * @see #printArrows()
     */
    public void printObjects() {
        String str = "Snapshot of all the objects: ";
        for(String name: objects.keySet())
            str = String.format("%s%s ", str, name);
        
        System.out.println(str);
    }

    /**
     * Prints all of an object's incoming and outcoming arrows
     * @param obj Object to print the arrows of.
     * @throws BadObjectNameException If the object does not exist.
     */
    public static void printObjectArrows(Obj obj) throws BadObjectNameException {
        System.out.printf("Snapshot of all of %s's arrows:\n", obj.getName());
        System.out.println("Outcoming: ");
        for(Arrow arr: obj.outcoming)
            System.out.printf("\t%s\n", arr.represent());
        
        System.out.println("Incoming: ");
        for(Arrow arr: obj.incoming)
            System.out.printf("\t%s\n", arr.represent());
    }

    /**
     * Prints all of an object's incoming and outcoming arrows
     * @param objName Object to print the arrows of.
     * @throws BadObjectNameException If the object does not exist.
     */
    public void printObjectArrows(String objName) throws BadObjectNameException {
        Obj obj;
        try {
            obj = objects.get(objName);
        } catch (NullPointerException e) {
            throw new BadObjectNameException("Object does not exist in the category.");
        }

        printObjectArrows(obj);
    }

    /**
     * Prints all the spaces in the category
     */
    public void printSpaces() {
        String str = "Snapshot of all the spaces: ";
        for(String name: spaces.keySet())
            str = String.format("%s%s ", str, name);
        for(Obj obj: objects.values())
            str = String.format("%s%s ", str, obj.getDomain().getName());
        
        System.out.println(str);
    }

    /**
     * Prints all the arrows and objects which make use of the space
     * @param space
     */
    public void printSpaceDependecies(Space space) {
        System.out.printf("Snapshot of all of %s's dependencies:\n", space.getName());
        System.out.println("Arrows: ");
        for(Arrow arr: space.toArrows)
            System.out.printf("\t%s\n", arr.represent());
        
        if(space.object != null)
            System.out.println("Object: " + space.object.getName());
    }

    /**
     * Prints all the arrows and objects which make use of the space
     * @param spaceName
     * @throws BadSpaceException
     */
    public void printSpaceDependecies(String spaceName) throws BadSpaceException {
        Space spc;
        try {
            spc = spaces.get(spaceName);
        } catch (NullPointerException e) {
            throw new BadSpaceException("Space does not exist in the category.");
        }

        printSpaceDependecies(spc);
    }

    /**
     * Finds all the initial objects.
     * @return
     */
    public Set<Obj> getInitialObjs() {
        Set<Obj> initialObjs = new HashSet<Obj>();

        for (Obj obj : objects.values())
            if (obj.isInitial())
                initialObjs.add(obj);

        return initialObjs;
    }

    /**
     * Finds all the terminal objects.
     * @return
     */
    public Set<Obj> getTerminalObjs() {
        Set<Obj> terminalObjs = new HashSet<Obj>();

        for (Obj obj : objects.values())
            if (obj.isTerminal())
                terminalObjs.add(obj);

        return terminalObjs;
    }

    /**
     * Changes name of the given object
     * @param obj Reference to the object
     * @param newName New name of the object
     * @throws BadObjectNameException If an object of the same name already exists.
     * @throws BadSpaceException If things happen while changing name of the identity based on this object.
     */
    public void objectChangeName(Obj obj, String newName) throws BadObjectNameException, BadSpaceException {
        if(objects.containsKey(newName))
            throw new BadObjectNameException("Object with the same name already exists!");
        String oldIdName = Arrow.makeIdentityName(obj.getName());
        String newIdName = Arrow.makeIdentityName(newName);
        for(Arrow arr: obj.incoming)
            if(arr.getName().equals(oldIdName))
                arrowChangeName(arr, newIdName);
        
        objects.remove(obj.getName());
        objects.put(newName, obj);

        obj.setName(newName);
    }

    /**
     * Changes name of the given arrow
     * @param arr Reference to the arrow
     * @param newName New name of the arrow
     * @throws BadObjectNameException If an arrow of the same name already exists.
     * @throws BadSpaceException If things happen while changing name of the range/image based on this arrow
     */
    public void arrowChangeName(Arrow arr, String newName) throws BadObjectNameException, BadSpaceException {
        if(arrows.containsKey(newName))
            throw new BadObjectNameException("Arrow with the same name already exists!");
        if(arr.range.getName().equals(Space.makeRangeName(arr.getName())))
            spaceChangeName(arr.range, Space.makeRangeName(newName));
        if(arr.image.getName().equals(Space.makeImageName(arr.getName())))
            spaceChangeName(arr.image, Space.makeImageName(newName));

        arrows.remove(arr.getName());
        arrows.put(newName, arr);

        arr.setName(newName);
    }


    /**
     * Changes the name of the given space.
     * @param space Reference to the space 
     * @param newName New name of the space
     * @throws BadSpaceException If a space with the same name already exists.
     */
    public void spaceChangeName(Space space, String newName) throws BadSpaceException {
        if(spaces.containsKey(newName))
            throw new BadSpaceException("A space with the same name already exists!");

        spaces.remove(space.getName());
        spaces.put(newName, space);
        space.setName(newName);
    }

    /**
     * Returns object with the given name, if it exists.
     * @param name
     * @return
     */
    public Obj getObject(String name) {
        return objects.get(name);
    }

    /**
     * Returns arrow with the given name, if it exists.
     * @param name
     * @return
     */
    public Arrow getArrow(String name) {
        return arrows.get(name);
    }

    /**
     * Saves the category to a JSON file in '/saved_categories'
     * @param fileName name of the file (be sure to add .json at the end)
     * @param overwrite whether to overwrite existing files.
     * @throws IOException
     */
    public void save(String path, String fileName, boolean overwrite) throws IOException {
        // Create folder if it doesn't exist
        File folder = new File(path);
        if(!folder.exists())
            folder.mkdir();

        // Create file and throw error if can't overwrite.
        File file = new File(path + fileName);
        if(file.exists() && !overwrite)
            throw new IOException("File already exists.");
        
        file.createNewFile();

        save(file);
    }

    /**
     * Saves the category to a JSON file
     * @param file
     * @throws IOException
     */
    public void save(File file) throws IOException{
        JSONObject jsUni = new JSONObject();
        jsUni.put("name", universeName);

        //Make array of objs json representations.
        JSONArray objs = new JSONArray();
        for(Obj obj: objects.values()) {
            JSONObject jsObj = new JSONObject();
            jsObj.put("name", obj.getName());
            if(obj.getRepr() != null) {
                jsObj.put("x", obj.getRepr().getLayoutX());
                jsObj.put("y", obj.getRepr().getLayoutY());
            }
            JSONArray jsSpaces = new JSONArray();
            for(Space space: spaces.values()) {
                JSONObject jsSpc = new JSONObject();
                jsSpc.put("name", space.getName());
                jsSpaces.put(jsSpc);
            }
            jsObj.put("spaces", jsSpaces);
            objs.put(jsObj);
        }

        //Make array of arrows json representations.
        JSONArray arrs = new JSONArray();
        for(Arrow arr: arrows.values()) {
            JSONObject jsArr = new JSONObject();
            jsArr.put("name", arr.getName());
            jsArr.put("source", arr.src().getName());
            jsArr.put("target", arr.trg().getName());
            if(arr.range != arr.src().getDomain())
                jsArr.put("range", arr.range.getName());
            if(arr.image != arr.trg().getDomain())
                jsArr.put("image", arr.image.getName());
            jsArr.put("identity", arr.isIdentity());

            // Make array of the arrows which compose from the current one.
            JSONArray deps = new JSONArray();
            for(Arrow dep: arr.dependencies) {
                deps.put(dep.getName());
            }
            jsArr.put("dependencies", deps);
            arrs.put(jsArr);
        }

        //Make object representing the whole category
        JSONObject category = new JSONObject();
        category.put("universe", jsUni);
        category.put("objects", objs);
        category.put("arrows", arrs);

        //Save file
        FileWriter shakespeare = new FileWriter(file); // Hihihi I am so fun...
        shakespeare.write(category.toString(4));
        shakespeare.close();
    }

    /**
     * Loads and returns the category corresponding to the given .json file
     * @param fileName
     * @return
     * @throws IOException
     * @throws BadObjectNameException
     * @throws ImpossibleArrowException
     */
    public static Category load(String fileName) throws IOException, BadObjectNameException, ImpossibleArrowException, BadSpaceException {
        File file = new File(System.getProperty("user.dir") +"/saved_categories/"+ fileName);
        if(!file.exists())
            throw new IOException("File does not exist.");

        return load(file);
    }

    /**
     * Loads and returns the category corresponding to the given .json file
     * @param file
     * @return
     * @throws IOException
     * @throws BadObjectNameException
     * @throws ImpossibleArrowException
     */
    public static Category load(File file) throws IOException, BadObjectNameException, ImpossibleArrowException, BadSpaceException {
        //Oh almighty god of the algorithms, please, have mercy on me
        //because I have sinned: this method is O(n^2), I beg of you,
        //shed the knowledge on me such that I may honour your name
        //with worthy runtimes.

        //Leaving jokes aside, this method brakes the rules I gave myself of only using
        //methods I already wrote to modify the category by directly mingling with its
        //internal collections: it is damn good though, but don't ever try to do it yourself
        //- or at least ask me first.
        
        FileReader reader = new FileReader(file);
        JSONObject category = new JSONObject(new JSONTokener(reader));

        reader.close();
        JSONObject jsUniverse = category.getJSONObject("universe");

        Category ct = new Category(jsUniverse.getString("name"));

        // Load all the objs
        JSONArray objs = category.getJSONArray("objects");
        for(Integer i = 0; i < objs.length(); i++) {
            JSONObject jsonObj = objs.getJSONObject(i);
            Obj obj = ct.addObject(jsonObj.getString("name"));
            JSONArray jsSpaces = jsonObj.getJSONArray("spaces");
            // Load all spaces
            for(Integer j = 0; j < jsSpaces.length(); j++) {
                JSONObject jsonSpace = jsSpaces.getJSONObject(j);
                ct.addSpace(jsonSpace.getString("name"), obj);
            }
        }

        // Load all the arrows
        JSONArray arrs = category.getJSONArray("arrows");
        for(Integer i = 0; i < arrs.length(); i++) {
            JSONObject jsonArr = arrs.getJSONObject(i);
            //Essentially a super version of addArrow()
            String name = jsonArr.getString("name");
            Obj source = ct.objects.get(jsonArr.getString("source"));
            Obj target = ct.objects.get(jsonArr.getString("target"));
            String spaceName;
            Space range;
            Space image;
            if((spaceName = jsonArr.optString("range",null)) != null)
                range = ct.spaces.get(spaceName);
            else
                range = source.getDomain();

            if((spaceName = jsonArr.optString("image",null)) != null)
                image = ct.spaces.get(spaceName);
            else
                image = target.getDomain();

            Arrow arr = new Arrow(name, source, target, range, image, jsonArr.getBoolean("identity"));
            range.toArrows.add(arr);
            image.toArrows.add(arr);

            source.outcoming.add(arr);
            target.incoming.add(arr);

            ct.arrows.put(name, arr);
        }


        //Put in the composition-dependencies
        for(Integer i = 0; i < arrs.length(); i++) {
            JSONObject jsonArr = arrs.getJSONObject(i);
            Arrow arr = ct.arrows.get(jsonArr.getString("name"));
            JSONArray deps = jsonArr.getJSONArray("dependencies");
            for(Integer j = 0; j < deps.length(); j++) {
                Arrow comp = ct.arrows.get(deps.getString(j));
                arr.dependencies.add(comp);
                if(comp.range == arr.range) {
                    comp.firstAncestor = arr;
                    arr.image.toCompositions.add(comp);
                } else {
                    comp.secondAncestor = arr;
                    arr.range.toCompositions.add(comp);
                }

            }
        }

        for(Arrow arr: ct.arrows.values())
            if(!arr.runCheck())
                throw new IOException("Something went wrong with loading the category, some arrows shouldn't exist");

        return ct;
    }

    /**
     * Loads and returns the category corresponding to the given .json file
     * @param fileName
     * @return
     * @throws IOException
     * @throws BadObjectNameException
     * @throws ImpossibleArrowException
     */
    public static Category loadForGUI(File file, Pane pane) throws IOException, BadObjectNameException, ImpossibleArrowException, IllegalArgumentsException, BadSpaceException {
        Category ct = load(file);
        pane.getChildren().clear();
        
        FileReader reader = new FileReader(file);
        JSONObject category = new JSONObject(new JSONTokener(reader));

        reader.close();

        // Put objs in GUI
        JSONArray objs = category.getJSONArray("objects");
        for(Integer i = 0; i < objs.length(); i++) {
            JSONObject jsonObj = objs.getJSONObject(i);
            Obj obj = ct.objects.get(jsonObj.getString("name"));
            pane.getChildren().add(
                new ObjectGUI(jsonObj.getDouble("x"), jsonObj.getDouble("y"), obj, pane));
        }

        // Put arrows in the GUI
        for(Arrow arr: ct.arrows.values()) {
            pane.getChildren().add(
                new ArrGUI(arr.src().guiRepr, arr.trg().guiRepr, arr, pane));
        }

        return ct;
    }

    class PairSetup<A,B> {
        A a;
        B b;

        PairSetup(A a, B b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public int hashCode() {
            return a.hashCode() + b.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof PairSetup))
                return false;
            PairSetup other = (PairSetup) obj;
            return other.a.equals(a) && other.b.equals(b);
        }
    }

    /**
     * Checks whether the category commutes or not.
     * Peak Algorithm-class moment
     * @return
     */
    public boolean commutes() {
        Map<Space, Set<Arrow>> spacesGoTo = new HashMap<Space, Set<Arrow>>();
        Map<PairSetup<Space, Obj>, Space> mapping = new HashMap<PairSetup<Space, Obj>, Space>();
        Set<Obj> objectsCopy = new HashSet<Obj>();

        for(Obj o: objects.values())
            objectsCopy.add(o);

        while(!objectsCopy.isEmpty()) {
            Obj currO = objectsCopy.iterator().next();
            objectsCopy.remove(currO);

            Set<PairSetup<Space, Arrow>> checkingPairs = new HashSet<PairSetup<Space, Arrow>>();
            for(Arrow arr: currO.outcoming)
                checkingPairs.add(new PairSetup<Space, Arrow>(arr.range, arr));

            while(!checkingPairs.isEmpty()) {
                PairSetup<Space, Arrow> pair = checkingPairs.iterator().next();
                Arrow arr = pair.b;

                // Check if holds the commutative condition
                PairSetup<Space,Obj> expPair = new PairSetup<Space,Obj>(pair.a, arr.trg());
                Space expectedSpace;
                if((expectedSpace = mapping.get(expPair)) == null) {
                    expectedSpace = arr.image;
                    mapping.put(expPair, expectedSpace);
                } else if(expectedSpace != arr.image)
                    return false;

                // Check if it is new space
                Set<Arrow> spaceSet;
                if((spaceSet = spacesGoTo.get(pair.a)) == null) {
                    spaceSet = new HashSet<Arrow>();
                    spacesGoTo.put(pair.a, spaceSet);
                }

                // Check if for space it is new arrow
                if(!spaceSet.contains(arr)) {
                    for(Arrow othr: arr.trg().outcoming) {
                        if(othr.range == arr.image || othr.range == arr.trg().domain)
                            checkingPairs.add(new PairSetup<Space, Arrow>(pair.a, othr));
                        if(objectsCopy.contains(arr.trg()))
                            checkingPairs.add(new PairSetup<Space, Arrow>(othr.range, othr));
                    }
                    objectsCopy.remove(arr.trg());
                    spaceSet.add(arr);
                }
                checkingPairs.remove(pair);
            }
        }

        return true;
    }

    public static void main(String[] args) throws BadObjectNameException, ImpossibleArrowException, IOException, BadSpaceException {
        /*
        // This is to test the model
        Category ct = new Category("Pippo");

        // This is the premise of the five lemma
        ct.addObject("A");
        ct.addObject("B");
        ct.addObject("C");
        ct.addObject("D");
        ct.addObject("E");

        ct.addObject("A'");
        ct.addObject("B'");
        ct.addObject("C'");
        ct.addObject("D'");
        ct.addObject("E'");

        Arrow a1 = ct.addArrow("f", "A", "B");
        Arrow a2 = ct.addArrow("g", "B", "C");
        Arrow a3 = ct.addArrow("h", "C", "D");
        Arrow a4 = ct.addArrow("j", "D", "E");

        Arrow b1 = ct.addArrow("l", "A", "A'");
        ct.arrowChangeImage(b1, "dom(A')");
        Arrow b2 = ct.addArrow("m", "B", "B'", true);
        Arrow b3 = ct.addArrow("n", "C", "C'");
        Arrow b4 = ct.addArrow("p", "D", "D'", true);
        Arrow b5 = ct.addArrow("q", "E", "E'");
        ct.arrowChangeRange(b5, "dom(E)");

        Arrow c1 = ct.addArrow("r", "A'", "B'");
        Arrow c2 = ct.addArrow("s", "B'", "C'");
        Arrow c3 = ct.addArrow("t", "C'", "D'");
        Arrow c4 = ct.addArrow("u", "D'", "E'");
        */
        
        Category ct = new Category("Pluto");
        ct.addObject("A");
        ct.addObject("B");
        ct.addObject("C");
        Arrow a1 = ct.addArrow("f", "A", "B");
        Arrow a2 = ct.addArrow("g", "B", "C");
        Arrow a3 = ct.addArrow("h", "A", "C");
        ct.arrowChangeImage(a1, "img(f)");
        ct.arrowChangeRange(a2, a1.image);
        ct.arrowChangeImage(a3, "img(h)");
        ct.addComposition(a2, a1);
        //Category ct = Category.load("five_lemma.json");

        ct.printArrows();
        ct.printObjects();
        ct.printSpaces();
        System.out.println(ct.commutes());

        //ct.save(System.getProperty("user.dir") +"/saved_categories", "five_lemma.json", true);
    }
}