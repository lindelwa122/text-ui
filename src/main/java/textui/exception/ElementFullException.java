package textui.exception;

/**
 * Exception thrown when an element cannot accept more content or children
 * because it has reached its maximum capacity.
 */
public class ElementFullException extends Exception {

    /**
     * Constructs a new ElementFullException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public ElementFullException(String message) {
        super(message);
    }
}

