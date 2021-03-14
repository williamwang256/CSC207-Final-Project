package eventsfeature;

/**
 * Thrown when we try to perform actions on a room that does not exist in the system.
 * @author Kevin Cecco, Daniel Chan, William Wang
 */
public class RoomNotFoundException extends RuntimeException {

    /**
     * Constructs a RoomNotFoundException.
     */
    public RoomNotFoundException() {
        super();
    }

    /**
     * Overrides the toString to provide an accurate error message.
     * @return A description of the error, as a string.
     */
    @Override
    public String toString() {
        return "room not found";
    }

}