package usersfeature;

/**
 * Thrown when username and name are the same while signing up.
 */
public class UsernameEqualsNameException extends RuntimeException {
    public UsernameEqualsNameException() {
        super();
    }

    /**
     * Overrides the toString to provide an accurate error message.
     */
    @Override
    public String toString() {
        return "Error: Username cannot be the same as the name";
    }
}
