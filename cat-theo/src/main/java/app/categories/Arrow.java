package app.categories;

import app.exceptions.ImpossibleArrowException;

import java.util.HashSet;
import java.util.Iterator;
import app.GUI.ArrGUI;

/**
 * Represents an Arrow from the Category Theory branch of mathematics.
 * @author Davide Marincione
 * @see {@link app.categories.Category Category}
 * @see {@link app.categories.Obj Obj}
 */
public class Arrow {
    public static final String IDENTITY_SYMBOL = "Id(%s)";
    public static final String COMPOSITION_SYMBOL = "%s • %s";
    private String name;
    private Obj src;
    private Obj trg;
    private Boolean identity = false;
    HashSet<Arrow> dependencies = new HashSet<Arrow>();
    Space range;
    Space image;
    Arrow firstAncestor;
    Arrow secondAncestor;
    public ArrGUI guiRepr;

    /**
     * Instances a new {@link app.categories.Arrow Arrow} representing a morphism from a source to a target.
     * Furthermore it is possible to define the range and the image of the morphism (also deciding if it is an
     * identity or not)
     * @param name
     * @param src
     * @param trg
     * @param range
     * @param image
     * @param identity
     * @throws ImpossibleArrowException
     */
    Arrow(String name, Obj src, Obj trg, Space range, Space image, Boolean identity) throws ImpossibleArrowException {
        if (identity && !src.equals(trg))
            throw new ImpossibleArrowException("An identity has to have same source and target!");

        this.name = name;
        this.src = src;
        this.trg = trg;
        this.range = range;
        this.image = image;
    }

    /**
     * Instances a new {@link app.categories.Arrow Arrow} representing a morphism from a source to a target.
     * Furthermore it is possible to define the range and the image of the morphism.
     * @param name
     * @param src
     * @param trg
     * @param range
     * @param image
     * @throws ImpossibleArrowException
     */
    Arrow(String name, Obj src, Obj trg, Space range, Space image) throws ImpossibleArrowException {
        this(name, src, trg, range, image, false);
    }

    /**
     * Returns the name of the {@link app.categories.Arrow Arrow} instance.
     * @return Name of the arrow.
     */
    public String getName() { return name; }

    /**
     * Returns the arrow's source.
     * @return Name of the source.
     */
    public Obj src() { return src; }

    /**
     * Returns the arrow's target.
     * @return Name of the target.
     */
    public Obj trg() { return trg; }

    /**
     * Function to easily compute a pretty print of the arrow
     * @return A string representing the arrow
     */
    public String represent() {
        return String.format("%s: %s->%s", getName(), src.getName(), trg.getName());
    }


    /**
     * Checks if the arrow is an identity
     * @return
     */
    public boolean isIdentity() { return identity; }

    /**
     * Checks if the arrow is a monomorphism
     * @return
     */
    public boolean isMonic() {
        Iterator<Arrow> iter = src.incoming.iterator();
        Space baseImage = Space.nullSpace;
        if (iter.hasNext())
            baseImage = iter.next().image;

        for(Space img = baseImage; iter.hasNext(); img = iter.next().image)
            if(!img.equals(baseImage))
                return false;

        return true;
    }

    /**
     * Checks if the arrow is an epimorphism
     * @return
     */
    public boolean isEpic() {
        Iterator<Arrow> iter = trg.outcoming.iterator();
        Space baseRange = Space.nullSpace;
        if (iter.hasNext())
            baseRange = iter.next().range;

        for(Space rng = baseRange; iter.hasNext(); rng = iter.next().range)
            if(!rng.equals(baseRange))
                return false;

        return true;
    }

    /**
     * Checks if the arrow is an isomorphism
     * @return
     */
    public boolean isIsomorphism() { return isMonic() && isEpic(); }

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
    public boolean isAutomorphism() { return isIsomorphism() && isEndomorphism(); }

    /**
     * Returns validity of the arrow.
     * @return
     */
    public boolean runCheck() {
        if(firstAncestor != null)
            return secondAncestor.range.contains(firstAncestor.image);
        return true;
    }

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

        if (f.trg().equals(g.src()) && g.range.contains(f.image)) {// Condition for a composition to be possible.
            return new Arrow(String.format(COMPOSITION_SYMBOL, g.getName(), f.getName()), f.src(), g.trg(), f.range, g.image);
        }
        else throw new ImpossibleArrowException(String.format("Tried to compose %s(%s), conditions not met.", g.getName(), f.getName()));
    }

    /**
     * Returns the name of an object's identity following the standard proposed by this package
     * @param obj Name of the object
     * @return Identity name following current standard
     */
    public static String makeIdentityName(String obj) { return String.format(IDENTITY_SYMBOL, obj); }

    void setName(String newName) {
        name = newName;
        if(guiRepr != null)
            guiRepr.nameGUI.setText(newName);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Arrow))
            return false;
    
        Arrow arr = (Arrow) obj;

        return arr.range.equals(range) && arr.image.equals(image) && arr.src().equals(src) && arr.trg().equals(trg);
    }
}
