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
    HashSet<Arrow> toArrows = new HashSet<Arrow>();
    HashSet<Obj> toObjs = new HashSet<Obj>();

    Space(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    
    public Space getSuperSpace() { return superSpace; }

    public boolean hasNoRefers() { return toArrows.isEmpty() && toObjs.isEmpty(); }

    public boolean contains(Space space) {
        if(equals(space))
            return true;
        if(space.equals(nullSpace))
            return true;
        if (subspaces.contains(space))
            return true;
        
        for (Space spc : subspaces)
            if (spc.contains(space))
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