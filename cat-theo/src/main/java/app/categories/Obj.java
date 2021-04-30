package app.categories;

import java.util.List;
import java.util.LinkedList;

public class Obj {
    private String name;
    List<Arrow> arrowsIn = new LinkedList<Arrow>();
    List<Arrow> arrowsOut = new LinkedList<Arrow>();

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
}
