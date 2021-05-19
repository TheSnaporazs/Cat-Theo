package app.categories;

import java.util.HashSet;


/**
 * A class to represent a mathematical space and its relations to
 * other spaces (i.e. containment).
 * @author Davide Marincione
 */
public class Space {
    public static final String RANGE_SYMBOL = "rng(%s)";
    public static final String IMAGE_SYMBOL = "img(%s)";

    private String name;
    HashSet<Arrow> toArrows = new HashSet<Arrow>();
    HashSet<Arrow> toCompositions = new HashSet<Arrow>();
    Obj object;
    boolean isOfObject = false;

    /**
     * Constructs a space with custom name.
     * @param name
     */
    Space(String name, Obj object, boolean isOfObject) {
        this.name = name;
        this.object = object;
        this.isOfObject = isOfObject;
    }

    /**
     * Returns the space's name
     * @return
     */
    public String getName() { return name; }

    void setName(String newName) { name = newName; }

    /**
     * If the space as any references left on it, useful for deletion.
     * @return
     */
    public boolean hasNoRefers() { return !isOfObject && toArrows.isEmpty(); }

    public static String makeRangeName(String name) { return String.format(RANGE_SYMBOL, name);}
    public static String makeImageName(String name) { return String.format(IMAGE_SYMBOL, name);} 
}