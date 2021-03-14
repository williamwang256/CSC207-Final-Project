package messagesfeature;

/**
 * Thrown when speaker tries to message all attendees of an event they do not speak at.
 */
public class EventNotYoursException extends RuntimeException {
    public EventNotYoursException() {
        super();
    }

    /**
     * Overrides the toString to provide an accurate error message.
     */
    @Override
    public String toString() {
        return "Error: You are not the speaker for this event.";
    }
}
