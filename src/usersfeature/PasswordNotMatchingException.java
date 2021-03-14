package usersfeature;

/**
 * Thrown when password fails to authenticate.
 */
public class PasswordNotMatchingException extends RuntimeException {
    public PasswordNotMatchingException() {
        super();
    }

    /**
     * Overrides the toString to provide an accurate error message.
     */
    @Override
    public String toString() {
        return "Error: Entered Password does not match records.";
    }
}
