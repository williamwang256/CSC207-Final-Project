package messagesfeature;

/**
 * Thrown when user tries to perform an action with a message id of a message
 * that does not exist.
 */
public class MessageNotFoundException extends RuntimeException{
    /**
     * Constructs a MessageNotFoundException.
     */
    public MessageNotFoundException(){super();}

    /**
     * Overrides the toString to provide an accurate error message.
     */
    @Override
    public String toString(){return "Error: message with that id does not exist.";}
}

