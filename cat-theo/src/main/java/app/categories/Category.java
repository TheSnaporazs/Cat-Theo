package app.categories;

import app.exceptions.BadCompositionException;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class Category {
    Set<Arrow> arrows = new HashSet<Arrow>();
    Set<Obj> objects = new HashSet<Obj>();

    /**
     * Adds a new arrow to the category,
     * updates the objects to which it sources and
     * targets such that their sets get updated.
     * @param name Name of the new arrow (may come useful for a LaTeX implementation)
     * @param src Source object of the arrow
     * @param trg Target object of the arrow
     * @return A reference to the new arrow
     */
    Arrow addArrow(String name, Obj src, Obj trg) {
        Arrow arr = new Arrow(name, src, trg);
        arrows.add(arr);
        src.arrowsOut.add(arr);
        trg.arrowsIn.add(arr);
        return arr;
    }

    /**
     * Removes an arrow from the category,
     * updates the objects to which it sources and 
     * targets such that their sets get updated.
     * @param arr
     */
    void removeArrow(Arrow arr) {
        arrows.remove(arr);
        arr.src().arrowsOut.remove(arr);
        arr.trg().arrowsIn.remove(arr);
        removeCompositionsOfArrow(arr);
    }

    /**
     * Removes all the arrows which compose from arr.
     * @param arr
     */
    void removeCompositionsOfArrow(Arrow arr) {
        for (Iterator<Arrow> i = arr.dependencies.iterator(); i.hasNext();) {
            Arrow dep = i.next();
            i.remove(); // This can be done since removal during iteration over a Set is permitted.
            if (arrows.contains(dep))
                removeArrow(dep);
        }
    }

    /**
     * Adds a new object to the category.
     * @param name The name of the new object (may come useful for a LaTeX implementation)
     * @return A reference to the new object
     */
    Obj addObject(String name) {
        Obj obj = new Obj(name);
        objects.add(obj);
        return obj;
    }

    /**
     * Removes an object from the category,
     * removes also the arrows having the aforementioned
     * object as either source or target.
     * @param obj
     */
    void removeObject(Obj obj) {
        objects.remove(obj);
        while(!obj.arrowsOut.isEmpty())
            removeArrow(obj.arrowsOut.get(0));
        while(!obj.arrowsIn.isEmpty())
            removeArrow(obj.arrowsIn.get(0));
    }


    /**
     * Prints all the arrows currently present in the category
     * to the terminal.
     */
    void printAllArrows() {
        System.out.println("Snapshot of all the arrows:");
        for (Arrow arr: arrows)
            System.out.printf("\t%s\n",arr.represent());
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
        else throw new BadCompositionException();
    }

    /**
     * Inserts in the category a composition (if possible)
     * @param g Latter function of the composition
     * @param f Inner function of the composition
     * @return reference to the inserted composition h=g(f)
     * @throws BadCompositionException
     */
    Arrow insertComposition(Arrow g, Arrow f) throws BadCompositionException {
        Arrow arr = compose(g, f);
        arrows.add(arr);
        arr.src().arrowsOut.add(arr);
        arr.trg().arrowsIn.add(arr);
        // Add the arrow as dependency of the inputs
        f.dependencies.add(arr);
        g.dependencies.add(arr);
        return arr;
    }

    public static void main(String[] args) throws BadCompositionException {
        Category ct = new Category();

        Obj o1 = ct.addObject("A");
        Obj o2 = ct.addObject("B");
        Obj o3 = ct.addObject("C");
        Obj o4 = ct.addObject("D");

        Arrow a1 = ct.addArrow("ƒ", o1, o2);
        Arrow a2 = ct.addArrow("g", o2, o3);
        Arrow a3 = ct.addArrow("h", o3, o4);

        Arrow c1 = ct.insertComposition(a2, a1);
        Arrow c2 = ct.insertComposition(a3, a2);
        Arrow c3 = ct.insertComposition(c2, a1);

        ct.printAllArrows();
        ct.removeObject(o2);
        ct.printAllArrows();
    }
}
