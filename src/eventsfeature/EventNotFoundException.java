package eventsfeature;

/**
 * Thrown when we try to perform actions on an event that does not exist in the system.
 * @author Kevin Cecco, Daniel Chan, William Wang
 */
public class EventNotFoundException extends RuntimeException {

    /**
     * Constructs an EventNotFoundException.
     */
    public EventNotFoundException() {
        super();
    }

    /**
     * Overrides the toString.
     * @return A string describing the exception.
     */
    @Override
    public String toString() {
        return "event not found";
    }

}
