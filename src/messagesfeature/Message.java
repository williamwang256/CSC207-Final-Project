package messagesfeature;

import java.time.*;

/**
 * Represents a message.
 * @author Caroline McKenzie, Anastasia Young, Yijia Zhou
 */
public class Message {

    private int id;
    private String sender;
    private LocalDateTime timestamp;
    private String content;
    private int parentId;
    private boolean archived;
    /**
     * Creates a message.
     * @param id The message's ID.
     * @param content The message's content.
     * @param sender The message's sender.
     * @param parentId The message's parent ID.
     * @param timestamp The message's timestamp.
     */
    public Message(int id, String content, String sender, int parentId, LocalDateTime timestamp){
        this.id = id;
        this.content = content;
        this.sender = sender;
        this.timestamp = timestamp;
        this.parentId = parentId;
        this.archived = false;
    }

    /**
     * Creates a message when file is loaded in.
     * @param id The message's ID.
     * @param content The message's content.
     * @param sender The message's sender.
     * @param parentId The message's parent ID.
     * @param timestamp The message's timestamp.
     */

    public Message(int id, String content, String sender, int parentId, LocalDateTime timestamp, boolean archived){
        this.id = id;
        this.content = content;
        this.sender = sender;
        this.timestamp = timestamp;
        this.parentId = parentId;
    }

    /**
     * Get the ID of the message.
     * @return An integer representing the message's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the parent ID the message.
     * @return An integer representing the message's parent ID. If the message is not a reply, it's parent ID is 0.
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * Get the time stamp of when the message was sent.
     * @return A string representing the time stamp.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Get the content for the message.
     * @return A string representing the content of the message.
     */
    public String getContent() {
        return content;
    }

    /**
     * Get the sender of the message.
     * @return A string representing the sender of the message.
     */
    public String getSender() {
        return sender;
    }


}


