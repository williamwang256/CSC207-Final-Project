package messagesfeature;

import java.time.LocalDateTime;

/**
 * A builder that creates a Message object.
 * @author Caroline McKenzie, Anastasia Young, Yijia Zhou
 */

public class MessageBuilder {
    private Integer messageID;
    private String content;
    private String sender;
    private Integer parentID;
    private LocalDateTime timestamp;

    /**
     * Builds the message id of the message.
     * @param messageID Integer that represents the message id.
     */
    public void buildMessageID(Integer messageID){
        this.messageID = messageID;
    }

    /**
     * Builds the content of the message.
     * @param content String that represents the content of the message.
     */
    public void buildContent(String content){
        this.content = content;
    }

    /**
     * Builds the sender of the message.
     * @param sender String that represents the sender of the message.
     */
    public void buildSender(String sender){
        this.sender = sender;
    }

    /**
     * Builds the parent ID of the message.
     * @param parentID Integer that represents the parent ID of the message.
     */
    public void buildParentID(Integer parentID){
        this.parentID = parentID;
    }

    /**
     * Builds the time stamp of the message.
     * @param timestamp Represents the time the message was sent.
     */
    public void buildTimestamp(LocalDateTime timestamp){
        this.timestamp = timestamp;
    }

    /**
     * Builds a message object.
     * @return A message object.
     */
    public Message buildMessage(){
        return new Message(messageID, content, sender, parentID, timestamp);
    }

}
