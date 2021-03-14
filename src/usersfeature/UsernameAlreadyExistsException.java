package usersfeature;

/**
 * Thrown when username already exists while signing up.
 */
public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException() {
        super();
    }

    /**
     * Overrides the toString to provide an accurate error message.
     */
    @Override
    public String toString() {
        return "Error: Username Already Exists";
    }
}
