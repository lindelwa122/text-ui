package textui.exception;

/**
 * Exception thrown when an attempt is made to set a value on an element
 * that is not allowed, typically because the element's state does not permit it.
 */
public class ValueCannotBeSetException extends Exception {

    /**
     * Constructs a new ValueCannotBeSetException with the specified detail message.
     *
     * @param message the detail message explaining why the value cannot be set
     */
    public ValueCannotBeSetException(String message) {
        super(message);
    }
}
