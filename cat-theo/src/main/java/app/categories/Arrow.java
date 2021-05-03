package app.categories;

import app.exceptions.ImpossibleArrowException;

/**
 * Represents an Arrow from the Category Theory branch of mathematics.
 * @author Davide Marincione
 * @see {@link app.categories.Arrow Arrow}
 */
public class Arrow {
    public static final String IDENTITY_SYMBOL = "Id(%s)";
    private String name;
    private String src;
    private String trg;
    private MorphType type;

    /**
     * Instances a new {@link app.categories.Arrow Arrow} representing a morphism from a source to a target
     * with custom type.
     * @param name Name of the arrow.
     * @param src Name of the source object.
     * @param trg Name of the target object.
     * @param type Type of the arrow.
     * @see app.categories.Category#addArrow(String, String, String) addArrow(name, source, target)
     */
    Arrow(String name, String src, String trg, MorphType type) throws ImpossibleArrowException {
        if (type == MorphType.IDENTITY && !src.equals(trg))
            throw new ImpossibleArrowException("An identity has to have same source and target!");

        this.name = name;
        this.src = src;
        this.trg = trg;
        this.type = type;
    }

    /**
     * Instances a new {@link app.categories.Arrow Arrow} representing a morphism from a source to a target.
     * @param name Name of the arrow.
     * @param src Name of the source object.
     * @param trg Name of the target object.
     * @see app.categories.Category#addArrow(String, String, String) addArrow(name, source, target)
     */
    Arrow(String name, String src, String trg) throws ImpossibleArrowException {
        this(name, src, trg, MorphType.MORPHISM);
    }

    /**
     * Instances a new {@link app.categories.Arrow Arrow} representing an object's <b>endomorphism</b> of custom type.
     * @param name Name of the arrow.
     * @param obj Name of the object.
     * @param type Type of the endomorphism.
     * @see app.categories.Category#addArrow(String, String, String) addArrow(name, source, target)
     */
    Arrow(String name, String obj, MorphType type) throws ImpossibleArrowException {    //Creates an Endomorphism for obj
        this(name, obj, obj, type);
    }

    /**
     * Instances a new {@link app.categories.Arrow Arrow} representing an object's <b>identity</b>.
     * @param name Arrow's custom name, thus won't use <b>IDENTITY_SYMBOL</b> standard.
     * @param obj Name of the object.
     * @see app.categories.Category#addArrow(String, String, String) addArrow(name, source, target)
     */
    Arrow(String name, String obj) throws ImpossibleArrowException {                    //Creates an Identity for obj using custom name
        this(name, obj, obj, MorphType.IDENTITY);
    }

    /**
     * Instances a new {@link app.categories.Arrow Arrow} representing an object's <b>identity</b>.
     * This constructor will name the arrow following the <b>IDENTITY_SYMBOL</b> standard.
     * @param obj Name of the object.
     * @see app.categories.Category#addArrow(String, String, String) addArrow(name, source, target)
     */
    Arrow(String obj) throws ImpossibleArrowException {                                 // Creates an Identity for obj using the standard IDENTITY_SYMBOL as template
        this(String.format(IDENTITY_SYMBOL, obj), obj, obj, MorphType.IDENTITY);
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
     * Returns the arrow's type.
     * @return Type of the arrow.
     */
    MorphType getType() { return type; }

    /**
     * Function to easily compute a pretty print of the arrow
     * @return A string representing the arrow
     */
    String represent() {
        return String.format("%s: %s→%s",getName(),src,trg);
    }
}
