package messagesfeature;

/**
 * Thrown when user inputs multiple usernames to start a conversation with but
 * they inputted the same username more than once.
 */
public class UsersNotUniqueException extends RuntimeException {
    /**
     * Constructs a UsersNotUniqueException.
     */
    public UsersNotUniqueException() {
        super();
    }

    /**
     * Overrides the toString to provide an accurate error message.
     */
    @Override
    public String toString() {
        return "Error: Same user inputted more than once";
    }
}
