package messagesfeature;

/**
 * Thrown when user tries to message a user that they are not allowed to based on the
 * restrictions outlined in phase 1.
 */
public class CannotMessageUserException extends RuntimeException {
    /**
     * Constructs a CannotMessageUserException.
     */
    public CannotMessageUserException() {
        super();
    }

    /**
     * Overrides toString to provide an accurate error message.
     */
    @Override
    public String toString() {
        return "Unable to message user";
    }
}
