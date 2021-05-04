package app.categories;

import app.exceptions.*;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
/**
 * Represents a category from the Category Theory branch of mathematics.
 * @author Davide Marincione
 * @see {@link app.categories.Arrow Arrow}
 * @see {@link app.categories.Obj Obj}
 */
public class Category {
    HashMap<Arrow, Set<Arrow>> arrows = new HashMap<Arrow, Set<Arrow>>();
    HashMap<String, Obj> objects = new HashMap<String, Obj>();

    /**
     * Adds a new {@link app.categories.Arrow Arrow} with custom type
     * to the {@link app.categories.Category Category}.
     * @param name Name of the new arrow (watch out for a possible LaTeX implementation).
     * @param src Source object.
     * @param trg Target object.
     * @param type Type of the arrow.
     * @return Reference to the added arrow.
     * @throws BadObjectNameException If the object does not exist.
     * @throws ImpossibleArrowException
     * @see #removeArrow(Arrow)
     * @see app.categories.Arrow#Arrow(String, Obj, Obj, MorphType) Arrow(name, source, target, type)
     */
    public Arrow addArrow(String name, Obj src, Obj trg, MorphType type) throws BadObjectNameException, ImpossibleArrowException {
        if(!objects.containsValue(src))
            throw new BadObjectNameException("Source object does not exist in category.");
        if(!objects.containsValue(trg))
            throw new BadObjectNameException("Target object does not exist in category.");

        if(type == MorphType.IDENTITY)
            if (src.equals(trg))
                return addIdentity(name, src);

        Arrow arr = new Arrow(name, src, trg, type);

        src.outcoming.add(arr);
        trg.incoming.add(arr);

        arrows.put(arr, new HashSet<Arrow>());
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
     * @throws BadObjectNameException If the object does not exist.
     * @throws ImpossibleArrowException
     * @see #removeArrow(Arrow)
     * @see app.categories.Arrow#Arrow(String, Obj, Obj) Arrow(name, source, target)
     */
    public Arrow addArrow(String name, Obj src, Obj trg) throws BadObjectNameException, ImpossibleArrowException {
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
     * @throws BadObjectNameException If the object does not exist.
     * @throws ImpossibleArrowException If the identity already is in the category
     * @see #removeArrow(Arrow)
     * @see app.categories.Arrow#Arrow(String, Obj) Arrow(name, obj)
     */
    public Arrow addIdentity(String name, Obj obj) throws BadObjectNameException, ImpossibleArrowException {
        if (!objects.containsValue(obj))
            throw new BadObjectNameException("Object does not exist in category.");

        for(Arrow dep: obj.outcoming)
            if(dep.getType() == MorphType.IDENTITY)
                throw new ImpossibleArrowException("An object's identity is unique.");

        Arrow arr = new Arrow(name, obj, obj, MorphType.IDENTITY);

        // Add reference to object
        obj.outcoming.add(arr);
        obj.incoming.add(arr);

        arrows.put(arr, new HashSet<Arrow>());
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
     * @throws BadObjectNameException If the object does not exist.
     * @throws ImpossibleArrowException If an identity of the object is already in the category.
     * @see #removeArrow(Arrow)
     * @see app.categories.Arrow#Arrow(Obj) Arrow(obj)
     */
    public Arrow addIdentity(Obj obj) throws BadObjectNameException, ImpossibleArrowException {
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
        // Remove reference from source
        if (objects.containsValue(arr.src()))
            arr.src().outcoming.remove(arr);

        // Remove reference from target
        if (objects.containsValue(arr.trg()))
            arr.trg().incoming.remove(arr);

        // Remove from the Category all the compositions depending on the
        // current arrow.
        Set<Arrow> tempSet;
        if((tempSet = arrows.remove(arr)) != null)
            for(Arrow comp: tempSet)
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
        if(!objects.containsValue(f.src()))
            throw new BadObjectNameException("Source object does not exist in category.");
        if(!objects.containsValue(g.trg()))
            throw new BadObjectNameException("Target object does not exist in category.");
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
        arrows.get(f).add(arr);
        arrows.get(g).add(arr);

        arrows.put(arr, new HashSet<Arrow>());
        return arr;
    }

    /**
     * Removes all the {@link app.categories.Arrow Arrows} which compose from the given one.
     * @param arr Reference to the arrow from which the arrows to remove compose.
     * @see #addComposition(Arrow, Arrow)
     */
    public void removeArrowCompositions(Arrow arr) {
        for (Arrow comp: arrows.get(arr))
            removeArrow(comp);
        arrows.get(arr).clear();
    }

    /**
     * Prints all the arrows currently present in the {@link app.categories.Category Category}.
     * @see #printObjects()
     */
    public void printArrows() {
        System.out.println("Snapshot of all the arrows:");
        for (Arrow arr: arrows.keySet())
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

    public static void main(String[] args) throws BadObjectNameException, ImpossibleArrowException {
        // This is to test the model
        Category ct = new Category();

        ct.addObject("A");
        ct.addObject("B");
        ct.addObject("C");
        ct.addObject("D");

        Arrow a1 = ct.addArrow("ƒ", "A", "B");
        Arrow a2 = ct.addArrow("g", "B", "C");
        Arrow a3 = ct.addArrow("h", "C", "D");

        Arrow c1 = ct.addComposition(a2, a1);
        ct.addComposition(a3, a2);
        ct.addComposition(a3, c1);
        
        ct.addIdentity("A");

        ct.printArrows();
        ct.printObjects();
        ct.printObjectArrows("B");

        /*
        ct.removeObject("B");
        ct.printArrows();
        ct.printObjects();

        ct.addObject("B");
        ct.addArrow("g", "B", "C");
        ct.printArrows();
        ct.printObjects();
        */
    }
}
