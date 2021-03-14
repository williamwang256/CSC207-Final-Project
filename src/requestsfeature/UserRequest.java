package requestsfeature;

/**
 * An entity class representing a User Request.
 * @author Caroline McKenzie, William Wang
 */
public class UserRequest {

    private final int eventId;
    private final String username;
    private final String item;
    private String status;

    /**
     * Creates an instance of UserRequest.
     * @param eventId The event id associated with this UserRequest, as an integer.
     * @param user The username associated with this UserRequest, as a string.
     * @param item The description, or item, of this UserRequest, as a string.
     */
    protected UserRequest(int eventId, String user, String item) {
        this.eventId = eventId;
        this.username = user;
        this.item = item;
        this.status = "pending";
    }

    /**
     * Creates an instance of UserRequest (for use when loading in the program from a file).
     * @param eventId The event id associated with this UserRequest, as an integer.
     * @param user The username associated with this UserRequest, as a string.
     * @param item The description, or item, of this UserRequest, as a string.
     * @param status The status of the Request.
     */
    protected UserRequest(int eventId, String user, String item, String status) {
        this.eventId = eventId;
        this.username = user;
        this.item = item;
        this.status = status;
    }

    /**
     * Gets the description/item of this UserRequest.
     * @return The description, as a string.
     */
    protected String getItem() {
        return item;
    }

    /**
     * Gets the id associated with this UserRequest.
     * @return The id, as an integer.
     */
    protected int getEventId() {
        return eventId;
    }

    /**
     * Gets the username associated with this UserRequest.
     * @return The username, as a string.
     */
    protected String getUsername() {
        return username;
    }

    /**
     * Gets the status of this UserRequest.
     * @return The status, as a string. The status can either be "pending" or "resolved".
     */
    protected String getStatus() {
        return status;
    }

    /**
     * Sets this UserRequest as "resolved".
     */
    protected void setResolved() {
        status = "resolved";
    }

}
