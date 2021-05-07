package app.categories;

import java.util.Set;
import java.util.HashSet;

//This is just an idea: don't know if we want to develop this
/**
 * A class to represent a mathematical space and its relations to
 * other spaces (i.e. containment).
 * @author Davide Marincione
 */
public class Space {
    private String name;
    private Set<Space> superspaces = new HashSet<Space>();
    private Set<Space> subspaces = new HashSet<Space>();
}
