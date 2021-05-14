package app.categories;

import java.util.HashSet;


/**
 * A class to represent a mathematical space and its relations to
 * other spaces (i.e. containment).
 * @author Davide Marincione
 */
public class Space {
    public static Space nullSpace = new Space("W̴̨̧̡͔͈̦̫̘̰̘̮̫͍̗̊̏̆̀̇̈́T̴̯̖̝̮̈́͋͊̃̚͝ͅF̸̟͚̜̘̠͓̣̠̖̗̪̘̟̈́̆̈̏̅͛̄̆̈́̋̄̋͠"){
        @Override
        public boolean contains(Space space) { return false; }
    };
    private String name;
    Space superSpace = null;
    HashSet<Space> subspaces = new HashSet<Space>();

    //Would have loved to make a HashSet<Arrow>: but I don't know why in
    // the Lord's name that would give me errors, I would literally call
    // .remove(arr) on this collection and, when printing its contents,
    // find that arr would still be inside it... bah, leave it as a set of strings.
    HashSet<String> toArrows = new HashSet<String>();
    
    HashSet<Obj> toObjs = new HashSet<Obj>();

    /**
     * Constructs a space with custom name.
     * @param name
     */
    Space(String name) {
        this.name = name;
    }

    /**
     * Returns the space's name
     * @return
     */
    public String getName() { return name; }
    
    /**
     * Returns the space's superspace
     * @return
     */
    public Space getSuperSpace() { return superSpace; }

    /**
     * If the space as any references left on it, useful for deletion.
     * @return
     */
    public boolean hasNoRefers() { return toArrows.isEmpty() && toObjs.isEmpty(); }

    /**
     * Checks if the space or any of its subspaces contains a space as a subspace
     * @param subspace 
     * @return
     */
    public boolean contains(Space subspace) {
        if(equals(subspace))
            return true;
        if(subspace.equals(nullSpace))
            return true;
        if (subspaces.contains(subspace))
            return true;
        
        for (Space spc : subspaces)
            if (spc.contains(subspace))
                return true;
        
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Space))
            return false;
        return name.equals(((Space) obj).getName());
    }
}