package eventsfeature;

/**
 * Thrown when the user inputs invalid fields when interacting with the events system.
 * @author Kevin Cecco, Daniel Chan, William Wang
 */
public class InvalidEventFieldsException extends RuntimeException {

    private final String field;

    /**
     * Constructs an InvalidEventFieldsException.
     * @param field The field input that went wrong.
     */
    public InvalidEventFieldsException(String field) {
        super();
        this.field = field;
    }

    /**
     * Overrides the toString to provide an accurate error message.
     * @return The string message describing the exception.
     */
    @Override
    public String toString() {
        return this.field;
    }

}
