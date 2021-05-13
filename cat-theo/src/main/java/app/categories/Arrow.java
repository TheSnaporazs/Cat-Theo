package app.categories;

import app.exceptions.ImpossibleArrowException;

import java.util.HashSet;

/**
 * Represents an Arrow from the Category Theory branch of mathematics.
 * @author Davide Marincione
 * @see {@link app.categories.Category Category}
 * @see {@link app.categories.Obj Obj}
 */
public class Arrow extends HashSet<Arrow>{
    public static final String IDENTITY_SYMBOL = "Id(%s)";
    public static final String COMPOSITION_SYMBOL = "%s • %s";
    private String name;
    private Obj src;
    private Obj trg;
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
    Arrow(String name, Obj src, Obj trg, MorphType type) throws ImpossibleArrowException {
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
    Arrow(String name, Obj src, Obj trg) throws ImpossibleArrowException {
        this(name, src, trg, MorphType.MORPHISM);
    }

    /**
     * Returns the name of the {@link app.categories.Arrow Arrow} instance.
     * @return Name of the arrow.
     */
    public String getName() { return name; }

    /**
     * Returns the name of the arrow's source.
     * @return Name of the source.
     */
    public Obj src() { return src; }

    /**
     * Returns the name of the arrow's target.
     * @return Name of the target.
     */
    public Obj trg() { return trg; }

    /**
     * Returns the arrow's type.
     * @return Type of the arrow.
     */
    public MorphType getType() { return type; }

    /**
     * Function to easily compute a pretty print of the arrow
     * @return A string representing the arrow
     */
    public String represent() {
        return String.format("%s: %s→%s", getName(), src.getName(), trg.getName());
    }

    /**
     * Checks whether the arrow is and endomorphism or not
     * (if the source and the target are the same)
     * @return
     */
    public boolean isEndomorphism() { return src.equals(trg); }

    /**
     * Checks whether the arrow is an automorphism or not
     * (if it is an {@link app.categories.Arrow#isEndomorphism() Endomorphism}
     * and an {@link app.categories.MorphType#ISOMORPHISM Isomorphism})
     * @return
     */
    public boolean isAutomorphism() { return type == MorphType.ISOMORPHISM && isEndomorphism(); }

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
     * @throws ImpossibleArrowException If the arrows are not composable with each other.
     * @see app.categories.Category#addComposition(Arrow g, Arrow f)
     */
    public static Arrow compose(Arrow g, Arrow f) throws ImpossibleArrowException {
        if (f.trg().equals(g.src())) // Condition for a composition to be possible.
            return new Arrow(String.format(COMPOSITION_SYMBOL, g.getName(), f.getName()), f.src(), g.trg());
        else throw new ImpossibleArrowException(String.format("Tried to compose %s(%s), conditions not met.", g.getName(), f.getName()));
    }

    /**
     * Returns the name of an object's identity following the standard proposed by this package
     * @param obj Name of the object
     * @return Identity name following current standard
     */
    public static String makeIdentityName(String obj) { return String.format(IDENTITY_SYMBOL, obj); }

    @Override
    public int hashCode() {
        return String.format("%s%s%s%s", name, src.getName(), trg.getName(), type.toString()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Arrow))
            return false;
    
        Arrow arr = (Arrow) obj;

        return arr.getType() == type && arr.getName().equals(name)
               && arr.src().equals(src) && arr.trg().equals(trg);
    }
}
