package app.categories;

import app.exceptions.*;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
/**
 * Represents a category from the Category Theory branch of mathematics.
 * @author Davide Marincione
 * @see {@link app.categories.Arrow Arrow}
 */
public class Category {
    HashMap<Arrow, Set<Arrow>> arrows = new HashMap<Arrow, Set<Arrow>>();
    HashMap<String, Set<Arrow>> objects = new HashMap<String, Set<Arrow>>();

    /**
     * Adds a new {@link app.categories.Arrow Arrow} to the {@link app.categories.Category Category}.
     * @param name Name of the new arrow (watch out for a possible LaTeX implementation).
     * @param src Name of the source object.
     * @param trg Name of the target object.
     * @return Reference to the added arrow.
     * @throws BadObjectNameException
     * @see #removeArrow(Arrow)
     * @see app.categories.Arrow#Arrow(String, String, String) Arrow(name, source, target)
     */
    Arrow addArrow(String name, String src, String trg) throws BadObjectNameException {
        Arrow arr = new Arrow(name, src, trg);

        // Add reference to source
        try {
            objects.get(src).add(arr);
        } catch (NullPointerException e) {
            throw new BadObjectNameException("Source object does not exist in category.");
        }

        // Add reference to target
        try {
            objects.get(trg).add(arr);
        } catch (NullPointerException e) {
            throw new BadObjectNameException("Target object does not exist in category.");
        }
        arrows.put(arr, new HashSet<Arrow>());
        return arr;
    }

    /**
     * Removes an {@link app.categories.Arrow Arrow} and all the other arrows that
     * compose (thus depend) on it from the {@link app.categories.Category Category}.
     * @param arr Reference to the arrow to remove.
     */
    void removeArrow(Arrow arr) {
        Set<Arrow> temp; // Needed for all of these things.

        // Remove reference from source
        if((temp = objects.get(arr.src())) != null)
            temp.remove(arr);

        // Remove reference from target
        if((temp = objects.get(arr.trg())) != null)
            temp.remove(arr);

        // Remove from the Category all the compositions depending on the
        // current arrow.
        if((temp = arrows.remove(arr)) != null)
            for(Arrow comp: temp)
                removeArrow(comp);
    }

    /**
     * Creates (if possible) a new {@link app.categories.Arrow Arrow} result of the
     * composition of two others, <b>doesn't</b> add it to any {@link app.categories.Category Category}.
     * Composition is the result of passing as input of a function another function-
     * in our case the composed function 'h' will be equal to: h = g(f) = g ○ f
     * In Category Theory the composition h between two arrows f, g such that f:A->B
     * and g:B->C is equal to creating a new arrow h: A->C
     * @param g Reference to the latter function g:B->C
     * @param f Reference to the first function f:A->B
     * @return Reference to the composed arrow h:A->C, h = g(f) = g ○ f
     * @throws BadCompositionException
     * @see #addComposition(Arrow g, Arrow f)
     */
    static Arrow compose(Arrow g, Arrow f) throws BadCompositionException {
        if (f.trg().equals(g.src())) // Condition for a composition to be possible.
            return new Arrow(String.format("%s ○ %s", g.getName(), f.getName()), f.src(), g.trg());
        else throw new BadCompositionException(String.format("Tried to compose %s(%s), conditions not met.", g.getName(), f.getName()));
    }

    /**
     * Inserts in the category (if possible) a new {@link app.categories.Arrow Arrow}
     * result of the composition of two others.
     * @param g Reference to the latter function g:B->C
     * @param f Reference to the first function f:A->B
     * @return Reference to the composed arrow h:A->C, h = g(f) = g ○ f
     * @throws BadCompositionException If the two given arrows can't be composed with each other.
     * @throws NullPointerException If for any reason the objects the given arrows
     * source or target to don't exist in the category.
     * reference to do not exist.
     * @see #compose(Arrow g, Arrow f)
     * @see #removeArrowCompositions(Arrow)
     */
    Arrow addComposition(Arrow g, Arrow f) throws BadCompositionException {
        Arrow arr = compose(g, f);

        // Add composition as dependent of f.src()
        try {
            objects.get(arr.src()).add(arr);
        } catch (NullPointerException e) {
            throw new NullPointerException("Source of 'f' does not exist in the category.");
        }

        // Add composition as dependent of g.trg()
        try {
            objects.get(arr.trg()).add(arr);
        } catch (NullPointerException e) {
            throw new NullPointerException("Target of 'g' does not exist in the category.");
        }

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
    void removeArrowCompositions(Arrow arr) {
        for (Arrow comp: arrows.get(arr))
            removeArrow(comp);
        arrows.get(arr).clear();
    }

    /**
     * Prints all the arrows currently present in the {@link app.categories.Category Category}.
     * @see #printObjects()
     */
    void printArrows() {
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
    void addObject(String name) throws BadObjectNameException {
        if (objects.containsKey(name)) throw new BadObjectNameException("An object with the same name already exists in the category.");
        objects.put(name, new HashSet<Arrow>());
    }

    /**
     * Removes an Object and all the {@link app.categories.Arrow Arrows}
     * which source or target it from the {@link app.categories.Category Category}.
     * @param name Name of the object to remove.
     * @throws BadObjectNameException If the object does not exist in the category.
     * @see #addObject(String name)
     */
    void removeObject(String name) throws BadObjectNameException {
        Set<Arrow> set;
        try {
            set = objects.remove(name);
        } catch (NullPointerException e) {
            throw new BadObjectNameException("Object does not exist in the category.");
        }
        for(Arrow arr: set)
            removeArrow(arr);
    }

    /**
     * Prints all the Objects currently in the {@link app.categories.Category Category}.
     * @see #printArrows()
     */
    void printObjects() {
        String str = "Snapshot of all the objects: ";
        for(String name: objects.keySet())
            str = String.format("%s%s ", str, name);
        
        System.out.println(str);
    }

    public static void main(String[] args) throws BadCompositionException, BadObjectNameException {
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

        ct.printArrows();
        ct.printObjects();
        ct.removeObject("B");
        ct.printArrows();
        ct.printObjects();
    }
}
