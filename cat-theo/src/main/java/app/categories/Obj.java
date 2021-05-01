package app.categories;

import java.util.Set;
import java.util.HashSet;

public class Obj {
    private String name;
    private boolean del = false;
    Set<Arrow> arrowsIn = new HashSet<Arrow>();
    Set<Arrow> arrowsOut = new HashSet<Arrow>();

    /**
     * Creates a new Obj instance
     * @param name Name of the object (may come useful for a LaTeX implementation)
     */
    Obj(String name) {
        this.name = name;
    }

    /**
     * Returns name of Obj instance
     * @return
     */
    String getName() { return name; }

    /**
     * Flags the object for deletion, some things
     * should be programmed with this in mind now.
     */
    void setDeletion() { del = true; }

    /**
     * Returns if the object has been flagged for deletion.
     * @return
     */
    boolean toBeDeleted() { return del; }
}
