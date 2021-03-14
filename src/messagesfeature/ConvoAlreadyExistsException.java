package messagesfeature;

/**
 * Thrown when user tries to create a conversation with other users that already exists.
 */
public class ConvoAlreadyExistsException extends RuntimeException {

    public ConvoAlreadyExistsException() {
        super();
    }

    /**
     * Overrides toString to provide an accurate error message.
     */
    @Override
    public String toString() {
        return "This conversation already exists";
    }
}
