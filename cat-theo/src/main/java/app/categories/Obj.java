package app.categories;

import app.GUI.ArrGUI;
import app.GUI.ObjectGUI;

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
    Set<Space> spaces = new HashSet<Space>();
    Set<Arrow> incoming = new HashSet<Arrow>();
    Set<Arrow> outcoming = new HashSet<Arrow>();
    public ObjectGUI guiRepr;

    /**
     * Instances a new {@link app.categories.Obj Obj} representing an object.
     * @param name Name of the object.
     */
    Obj(String name) {
        this.name = name;
        this.domain = new Space(String.format(DOMAIN_SYMBOL, name), this, true);
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
    public void setRepr(ObjectGUI repr) { guiRepr = repr; }

    /**
     * Returns a reference to the group representing the
     * object in the GUI
     * @return
     */
    public ObjectGUI getRepr() { return guiRepr; }

    /**
     * Returns domain of the object (i.e. the "spiritual" collection of
     * mathematical objects it represents)
     * @return
     */
    public Space getDomain() { return domain; }

    /**
     * Changes the name of this object.
     * @param newName
     */
    void setName(String newName) {
        name = newName;
        domain.setName(makeDomainName(name));
        if(guiRepr != null)
            guiRepr.txt.setText(newName);
    }

    /**
     * Returns the ArrowGUIs of the arrows connected to this object.
     * @return
     */
    public Set<ArrGUI> getArrowGUIs() {
        Set<ArrGUI> guis = new HashSet<ArrGUI>();
        for(Arrow arr: incoming)
            guis.add(arr.guiRepr);
        
        for(Arrow arr: outcoming)
            guis.add(arr.guiRepr);

        return guis;
    }

    /**
     * Returns the subspaces of this object's space 
     * @return
     */
    public Set<Space> getSubspaces() { return spaces; }

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
}
