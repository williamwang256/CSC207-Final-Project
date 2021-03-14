package usersfeature;

/**
 * Thrown when username wasn't found in the AttendeeHandler.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super();
    }

    /**
     * Overrides the toString to provide an accurate error message.
     */
    @Override
    public String toString() {
        return "Error: User not found.";
    }
}
