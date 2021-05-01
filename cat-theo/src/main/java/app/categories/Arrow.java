package app.categories;

public class Arrow {
    private String name;
    private String src;
    private String trg;


    /**
     * Creates a new Arrow instance
     * @param name Name of the arrow (may come useful for a LaTeX implementation)
     * @param src The source Obj instance
     * @param trg The target Obj instance
     */
    Arrow(String name, String src, String trg) {
        this.name = name;
        this.src = src;
        this.trg = trg;
    }

    /**
     * Returns name of Arrow instance
     * @return 
     */
    String getName() { return name; }

    /**
     * Returns source Obj of Arrow instance
     * @return
     */
    String src() { return src; }

    /**
     * Returns target Obj of Arrow instance
     * @return
     */
    String trg() { return trg; }

    /**
     * Function to easily compute a pretty print of the arrow
     * @return a string representing the function
     */
    String represent() {
        return String.format("%s: %s→%s",getName(),src,trg);
    }
}
