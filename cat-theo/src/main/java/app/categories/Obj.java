package app.categories;

import javafx.scene.Node;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an Object from the Category Theory branch of mathematics.
 * @author Davide Marincione
 * @see {@link app.categories.Category Category}
 * @see {@link app.categories.Arrow Arrow}
 */
public class Obj {
    public static final String DOMAIN_SYMBOL = "dom(%s)";
    private String name;
    Space domain;
    Set<Arrow> incoming = new HashSet<Arrow>();
    Set<Arrow> outcoming = new HashSet<Arrow>();
    Node guiRepr;

    /**
     * Instances a new {@link app.categories.Obj Obj} representing an object.
     * @param name Name of the object.
     */
    Obj(String name, Space domain) {
        this.name = name;
        this.domain = domain;
    }

    /**
     * Returns the name of the object
     * @return
     */
    public String getName() { return name; }

    /**
     * Checks whether the Obj is initial or not.
     * @return
     */
    public boolean isInitial() { return incoming.size() <= 0; }

    /**
     * Checks whether the Obj is terminal or not.
     * @return
     */
    public boolean isTerminal() { return outcoming.size() <= 0; }

    /**
     * Sets the group representing this object in the GUI
     * @param repr the group
     */
    public void setRepr(Node repr) { guiRepr = repr; }

    /**
     * Returns a reference to the group representing the
     * object in the GUI
     * @return
     */
    public Node getRepr() { return guiRepr; }

    /**
     * Returns domain of the object (i.e. the "spiritual" collection of
     * mathematical objects it represents)
     * @return
     */
    public Space getDomain() { return domain; }

    /**
     * Standard for representing an object's domain
     * @param objName Name of the object
     * @return standard name for the object's domain
     */
    public static String makeDomainName(String objName) { return String.format(DOMAIN_SYMBOL, objName); }

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
