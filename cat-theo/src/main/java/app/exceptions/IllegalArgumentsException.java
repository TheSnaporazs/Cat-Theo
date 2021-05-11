package app.exceptions;

/**
 * Exceptions to be thrown whenever there is a problem with a function's arguments
 * one such case is the SpawnContextMenu function in GUIutil having an unequal amount
 * of options and eventhandlers
 *
 * @see Exception
 * @see app.GUI.GUIutil
 * @author Dario Loi
 * @since 11/5/2021
 */
public class IllegalArgumentsException extends Exception{
    public IllegalArgumentsException() {
    }

    public IllegalArgumentsException(String message) {
        super(message);
    }

    public IllegalArgumentsException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalArgumentsException(Throwable cause) {
        super(cause);
    }
}
