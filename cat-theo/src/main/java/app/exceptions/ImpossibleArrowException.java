package app.exceptions;

public class ImpossibleArrowException extends Exception {

    /**
     * Still don't know what a serialVersion is needed for... should really look into it.
     */
    private static final long serialVersionUID = -4372066418516410450L;

    public ImpossibleArrowException() {
        super();
    }

    public ImpossibleArrowException(String errorMessage) {
        super(errorMessage);
    }
    
}
