package app.categories;

/**
 * Represents an Arrow from the Category Theory branch of mathematics.
 * @author Davide Marincione
 * @see {@link app.categories.Arrow Arrow}
 */
public class Arrow {
    private String name;
    private String src;
    private String trg;

    /**
     * Instances a new {@link app.categories.Arrow Arrow} representing the morphism from a source to a target.
     * @param name Name of the arrow.
     * @param src Name of the source object.
     * @param trg Name of the target object.
     * @see app.categories.Category#addArrow(String, String, String) addArrow(name, source, target)
     */
    Arrow(String name, String src, String trg) {
        this.name = name;
        this.src = src;
        this.trg = trg;
    }

    /**
     * Returns the name of the {@link app.categories.Arrow Arrow} instance.
     * @return Name of the arrow.
     */
    String getName() { return name; }

    /**
     * Returns the name of the arrow's source.
     * @return Name of the source.
     */
    String src() { return src; }

    /**
     * Returns the name of the arrow's target.
     * @return Name of the target.
     */
    String trg() { return trg; }

    /**
     * Function to easily compute a pretty print of the arrow
     * @return A string representing the arrow
     */
    String represent() {
        return String.format("%s: %s→%s",getName(),src,trg);
    }
}
