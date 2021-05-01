package app.categories;

import app.exceptions.*;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

public class Category {
    HashMap<Arrow, Set<Arrow>> arrows = new HashMap<Arrow, Set<Arrow>>();
    HashMap<String, Set<Arrow>> objects = new HashMap<String, Set<Arrow>>();

    /**
     * Adds a new arrow to the category,
     * updates the objects to which it sources and
     * targets such that their sets get updated.
     * @param name Name of the new arrow (may come useful for a LaTeX implementation)
     * @param src Source object of the arrow
     * @param trg Target object of the arrow
     * @return A reference to the new arrow
     */
    Arrow addArrow(String name, String src, String trg) {
        Arrow arr = new Arrow(name, src, trg);
        arrows.put(arr, new HashSet<Arrow>());
        objects.get(src).add(arr);
        objects.get(trg).add(arr);
        return arr;
    }

    /**
     * Removes an arrow from the category,
     * updates the objects to which it sources and 
     * targets such that their sets get updated.
     * @param arr
     */
    void removeArrow(Arrow arr) {
        Set<Arrow> temp;
        if((temp = objects.get(arr.src())) != null)
            temp.remove(arr);
        if((temp = objects.get(arr.trg())) != null)
            temp.remove(arr);

        if((temp = arrows.remove(arr)) != null)
            for(Arrow comp: temp)
                removeArrow(comp);
    }

    /**
     * Composes two arrows into one (if possible),
     * does not update the category.
     * @param g Latter function of the composition
     * @param f Inner function of the composition
     * @return The new arrow h=g(f)
     * @throws BadCompositionException
     */
    static Arrow compose(Arrow g, Arrow f) throws BadCompositionException {
        if (g.src() == f.trg())
            return new Arrow(String.format("%s ○ %s", g.getName(), f.getName()), f.src(), g.trg());
        else throw new BadCompositionException(String.format("Tried to compose %s(%s), conditions not met.", g.getName(), f.getName()));
    }

    /**
     * Inserts in the category a composition (if possible)
     * @param g Latter function of the composition
     * @param f Inner function of the composition
     * @return reference to the inserted composition h=g(f)
     * @throws BadCompositionException
     */
    Arrow addComposition(Arrow g, Arrow f) throws BadCompositionException {
        Arrow arr = compose(g, f);
        arrows.put(arr, new HashSet<Arrow>());
        objects.get(arr.src()).add(arr);
        objects.get(arr.trg()).add(arr);
        // Add the arrow as dependency of the inputs
        arrows.get(f).add(arr);
        arrows.get(g).add(arr);
        return arr;
    }

    /**
     * Removes all the arrows which compose from arr.
     * @param arr
     */
    void removeArrowCompositions(Arrow arr) {
        for (Arrow comp: arrows.get(arr))
            removeArrow(comp);
        arrows.get(arr).clear();
    }

    /**
     * Prints all the arrows currently present in the category
     * to the terminal.
     */
    void printArrows() {
        System.out.println("Snapshot of all the arrows:");
        for (Arrow arr: arrows.keySet())
            System.out.printf("\t%s\n",arr.represent());
    }

    /**
     * Adds a new object to the category.
     * @param name The name of the new object (may come useful for a LaTeX implementation)
     * @return A reference to the new object
     */
    void addObject(String name) throws ObjectHomonymException {
        if (objects.containsKey(name)) throw new ObjectHomonymException();
        objects.put(name, new HashSet<Arrow>());
    }

    /**
     * Removes an object from the category,
     * removes also the arrows having the aforementioned
     * object as either source or target.
     * @param obj
     */
    void removeObject(String name) {
        for(Arrow arr: objects.remove(name))
            removeArrow(arr);
    }

    /**
     * Prints all objects currently in the category
     */
    void printObjects() {
        String str = "Snapshot of all the objects: ";
        for(String name: objects.keySet())
            str = String.format("%s%s ", str, name);
        
        System.out.println(str);
    }

    public static void main(String[] args) throws BadCompositionException, ObjectHomonymException {
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
        Arrow c2 = ct.addComposition(a3, a2);
        Arrow c3 = ct.addComposition(c2, a1);

        ct.printArrows();
        ct.printObjects();
        ct.removeObject("B");
        ct.printArrows();
        ct.printObjects();
    }
}
