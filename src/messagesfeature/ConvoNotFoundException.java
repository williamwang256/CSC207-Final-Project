package messagesfeature;

/**
 * Thrown when user tries to perform an action with a conversation id of a conversation
 * that does not exist.
 */
public class ConvoNotFoundException extends RuntimeException {
    /**
     * Constructs a ConvoNotFoundException.
     */
    public ConvoNotFoundException() {
        super();
    }

    /**
     * Overrides the toString to  provide an accurate error message.
     */
    @Override
    public String toString() {
        return "Error: conversation not found";
    }
}
