package textui.exception;

/**
 * Exception thrown when attempting to add child elements to
 * a UI element that does not support children.
 */
public class ChildrenNotAllowedException extends Exception {

    /**
     * Constructs a new ChildrenNotAllowedException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public ChildrenNotAllowedException(String message) {
        super(message);
    }
}
