package app.categories;

import app.GUI.GUIutil;
import app.exceptions.BadObjectNameException;
import app.exceptions.ImpossibleArrowException;
import javafx.scene.layout.Pane;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a category from the Category Theory branch of mathematics.
 * @author Davide Marincione
 * @see {@link app.categories.Arrow Arrow}
 * @see {@link app.categories.Obj Obj}
 */
public class Category {
    HashMap<String, Arrow> arrows = new HashMap<String, Arrow>();
    HashMap<String, Obj> objects = new HashMap<String, Obj>();

    /**
     * Adds a new {@link app.categories.Arrow Arrow} with custom type
     * to the {@link app.categories.Category Category}.
     * @param name Name of the new arrow (watch out for a possible LaTeX implementation).
     * @param src Source object.
     * @param trg Target object.
     * @param type Type of the arrow.
     * @return Reference to the added arrow.
     * @throws ImpossibleArrowException
     * @see #removeArrow(Arrow)
     * @see app.categories.Arrow#Arrow(String, Obj, Obj, MorphType) Arrow(name, source, target, type)
     */
    public Arrow addArrow(String name, Obj src, Obj trg, MorphType type) throws ImpossibleArrowException {
        if(type == MorphType.IDENTITY)
            if (src.equals(trg))
                return addIdentity(name, src);

        Arrow arr = new Arrow(name, src, trg, type);

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
     * @return Reference to the added arrow.
     * @throws BadObjectNameException If the object does not exist.
     * @throws ImpossibleArrowException
     * @see #removeArrow(Arrow)
     * @see app.categories.Arrow#Arrow(String, Obj, Obj, MorphType) Arrow(name, source, target, type)
     */
    public Arrow addArrow(String name, String srcName, String trgName, MorphType type) throws BadObjectNameException, ImpossibleArrowException {
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
    
        return addArrow(name, src, trg, type);
    }

    /**
     * Adds a new {@link app.categories.Arrow Arrow} to the {@link app.categories.Category Category}.
     * @param name Name of the new arrow (watch out for a possible LaTeX implementation).
     * @param src Source object.
     * @param trg Target object.
     * @return Reference to the added arrow.
     * @throws ImpossibleArrowException
     * @see #removeArrow(Arrow)
     * @see app.categories.Arrow#Arrow(String, Obj, Obj) Arrow(name, source, target)
     */
    public Arrow addArrow(String name, Obj src, Obj trg) throws ImpossibleArrowException {
        return addArrow(name, src, trg, MorphType.MORPHISM);
    }

    /**
     * Adds a new {@link app.categories.Arrow Arrow} to the {@link app.categories.Category Category}.
     * @param name Name of the new arrow (watch out for a possible LaTeX implementation).
     * @param srcName Name of the source object.
     * @param trgName Name of the target object.
     * @return Reference to the added arrow.
     * @throws BadObjectNameException If the object does not exist.
     * @throws ImpossibleArrowException
     * @see #removeArrow(Arrow)
     * @see app.categories.Arrow#Arrow(String, Obj, Obj) Arrow(name, source, target)
     */
    public Arrow addArrow(String name, String srcName, String trgName) throws BadObjectNameException, ImpossibleArrowException {
        return addArrow(name, srcName, trgName, MorphType.MORPHISM);
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
        Arrow arr = new Arrow(name, obj, obj, MorphType.IDENTITY);

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
     * Removes an {@link app.categories.Arrow Arrow} and all the other arrows that
     * compose (thus depend) on it from the {@link app.categories.Category Category}.
     * @param arr Reference to the arrow to remove.
     */
    public void removeArrow(Arrow arr) {
        arrows.remove(arr.getName());

        // Remove reference from source
        if(objects.containsKey(arr.src().getName()))
            arr.src().outcoming.remove(arr);

        // Remove reference from target
        if(objects.containsKey(arr.trg().getName()))
            arr.trg().incoming.remove(arr);

        // Remove from the Category all the compositions depending on the
        // current arrow.
        for(Arrow comp: arr)
            removeArrow(comp);
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
        if (g.getType() == MorphType.IDENTITY)  // If g is identity then g•f = f (whether f is identity or not)
            return f;
        if (f.getType() == MorphType.IDENTITY)  // If f is identity then g•f = g (if here then g is not id.)
            return g;

        
        // Add composition as dependent of f.src()
        arr.src().outcoming.add(arr);

        // Add composition as dependent of g.trg()
        arr.trg().incoming.add(arr);

        // Add the arrow as dependent of f and g
        f.add(arr);
        g.add(arr);

        arrows.put(arr.getName(), arr);
        return arr;
    }

    /**
     * Removes all the {@link app.categories.Arrow Arrows} which compose from the given one.
     * @param arr Reference to the arrow from which the arrows to remove compose.
     * @see #addComposition(Arrow, Arrow)
     */
    public void removeArrowCompositions(Arrow arr) {
        for (Arrow comp: arr)
            removeArrow(comp);
        arr.clear();
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
     * @throws BadObjectNameException If an object with the same name already exists in
     * the category.
     * @see #removeObject(String name)
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
        }
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

        System.out.printf("Snapshot of all of %s's arrows:\n", objName);
        System.out.println("Outcoming: ");
        for(Arrow arr: obj.outcoming)
            System.out.printf("\t%s\n", arr.represent());
        
        System.out.println("Incoming: ");
        for(Arrow arr: obj.incoming)
            System.out.printf("\t%s\n", arr.represent());
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
     * Saves the category to a JSON file in '/saved_categories'
     * @param fileName name of the file (be sure to add .json at the end)
     * @param overwrite whether to overwrite existing files.
     * @throws IOException
     */
    public void save(String fileName, boolean overwrite) throws IOException{
        // Create folder if it doesn't exist
        File folder = new File(System.getProperty("user.dir") +"/saved_categories");
        if(!folder.exists())
            folder.mkdir();

        // Create file and throw error if can't overwrite.
        File file = new File(System.getProperty("user.dir") +"/saved_categories/"+ fileName);
        if(file.exists() && !overwrite)
            throw new IOException("File already exists.");
        
        file.createNewFile();


        //Make array of objs json representations.
        JSONArray objs = new JSONArray();
        for(Obj obj: objects.values()) {
            JSONObject jsObj = new JSONObject();
            jsObj.put("name", obj.getName());
            if(obj.getRepr() != null) {
                jsObj.put("x", obj.getRepr().getLayoutX());
                jsObj.put("y", obj.getRepr().getLayoutY());
            }
            objs.put(jsObj);
        }

        //Make array of arrows json representations.
        JSONArray arrs = new JSONArray();
        for(Arrow arr: arrows.values()) {
            JSONObject jsArr = new JSONObject();
            jsArr.put("name", arr.getName());
            jsArr.put("source", arr.src().getName());
            jsArr.put("target", arr.trg().getName());
            jsArr.put("type", arr.getType().ordinal());

            // Make array of the arrows which compose from the current one.
            JSONArray deps = new JSONArray();
            for(Arrow dep: arr) {
                deps.put(dep.getName());
            }
            jsArr.put("dependencies", deps);
            arrs.put(jsArr);
        }

        //Make object representing the whole category
        JSONObject category = new JSONObject();
        category.put("objects", objs);
        category.put("arrows", arrs);
        //Will maybe add Spaces in the future

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
    public static Category load(String fileName) throws IOException, BadObjectNameException, ImpossibleArrowException {
        //Oh almighty god of the algorithms, please, have mercy on me
        //because I have sinned: this method is O(n^2), I beg of you,
        //shed the knowledge on me such that I may honour your name
        //with worthy runtimes.
        MorphType[] types = MorphType.values();

        Category ct = new Category();
        // Create file and throw error if can't overwrite.
        File file = new File(System.getProperty("user.dir") +"/saved_categories/"+ fileName);
        if(!file.exists())
            throw new IOException("File does not exist.");
        
        FileReader reader = new FileReader(file);
        JSONObject category = new JSONObject(new JSONTokener(reader));

        reader.close();

        // Load all the objs
        JSONArray objs = category.getJSONArray("objects");
        for(Integer i = 0; i < objs.length(); i++)
            ct.addObject(objs.getJSONObject(i).getString("name"));

        // Load all the arrows
        JSONArray arrs = category.getJSONArray("arrows");
        for(Integer i = 0; i < arrs.length(); i++) {
            JSONObject jsonArr = arrs.getJSONObject(i);
            ct.addArrow(jsonArr.getString("name"), jsonArr.getString("source"), jsonArr.getString("target"), types[jsonArr.getInt("type")]);
        }

        //Put in the composition-dependencies
        for(Integer i = 0; i < arrs.length(); i++) {
            JSONObject jsonArr = arrs.getJSONObject(i);
            Arrow arr = ct.arrows.get(jsonArr.getString("name"));
            JSONArray deps = jsonArr.getJSONArray("dependencies");
            for(Integer j = 0; j < deps.length(); j++)
                arr.add(ct.arrows.get(deps.getString(j)));
        }

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
    public static Category loadForGUI(String fileName, Pane pane) throws IOException, BadObjectNameException, ImpossibleArrowException {
        Category ct = load(fileName);

        // Create file and throw error if can't overwrite.
        File file = new File(System.getProperty("user.dir") +"/saved_categories/"+ fileName);
        if(!file.exists())
            throw new IOException("File does not exist.");
        
        FileReader reader = new FileReader(file);
        JSONObject category = new JSONObject(new JSONTokener(reader));

        reader.close();

        // Put objs in GUI
        JSONArray objs = category.getJSONArray("objects");
        for(Integer i = 0; i < objs.length(); i++) {
            JSONObject jsonObj = objs.getJSONObject(i);
            Obj obj = ct.objects.get(jsonObj.getString("name"));
            GUIutil.spawnObject(jsonObj.getDouble("x"), jsonObj.getDouble("y"), obj, pane);
        }

        /* Do things for arrow GUI spawning
        for(Arrow arr: ct.arrows.values()) {
            //Things
        }
        */
        return ct;
    }

    public static void main(String[] args) throws BadObjectNameException, ImpossibleArrowException, IOException {
        /*
        // This is to test the model
        Category ct = new Category();

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

        Arrow a1 = ct.addArrow("ƒ", "A", "B");
        Arrow a2 = ct.addArrow("g", "B", "C");
        Arrow a3 = ct.addArrow("h", "C", "D");
        Arrow a4 = ct.addArrow("j", "D", "E");

        Arrow b1 = ct.addArrow("l", "A", "A'", MorphType.EPIMORPHISM);
        Arrow b2 = ct.addArrow("m", "B", "B'", MorphType.ISOMORPHISM);
        Arrow b3 = ct.addArrow("n", "C", "C'");
        Arrow b4 = ct.addArrow("p", "D", "D'", MorphType.ISOMORPHISM);
        Arrow b5 = ct.addArrow("q", "E", "E'", MorphType.MONOMORPHISM);

        Arrow c1 = ct.addArrow("r", "A'", "B'");
        Arrow c2 = ct.addArrow("s", "B'", "C'");
        Arrow c3 = ct.addArrow("t", "C'", "D'");
        Arrow c4 = ct.addArrow("u", "D'", "E'");
        */

        Category ct = Category.load("five_lemma.json");

        ct.printArrows();
        ct.printObjects();

        for(Obj o: ct.getInitialObjs())
            System.out.println(o.getName());

        for(Obj o: ct.getTerminalObjs())
            System.out.println(o.getName());
    }
}
