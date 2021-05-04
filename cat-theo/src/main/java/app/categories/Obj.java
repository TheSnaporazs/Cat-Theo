package app.categories;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an Object from the Category Theory branch of mathematics.
 * @author Davide Marincione
 * @see {@link app.categories.Category Category}
 * @see {@link app.categories.Arrow Arrow}
 */
public class Obj {
    private String name;
    Set<Arrow> incoming = new HashSet<Arrow>();
    Set<Arrow> outcoming = new HashSet<Arrow>();

    /**
     * Instances a new {@link app.categories.Obj Obj} representing an object.
     * @param name Name of the object.
     */
    public Obj(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the object
     * @return
     */
    public String getName() { return name; }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Obj))
            return false;
        return name.equals(((Obj) obj).getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
