package messagesfeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a conversation.
 * @author Caroline McKenzie, Anastasia Young, Yijia Zhou
 */
public class Conversation {
    private Integer convoId;
    private List<Integer> activeMessages;
    private List<String> users;
    private List<Integer> archivedMessages;
    private HashMap<String, Integer> read;

    /**
     * Creates a conversation object with list of users, messages, current message ID, ad conversation ID.
     * @param users A list of strings that represent the users in the conversation.
     * @param convoId An integer that represents the ID of the of the conversation.
     */
    public Conversation(List<String> users, Integer convoId, HashMap<String, Integer> read, List<Integer> activeMessages, List<Integer> archivedMessages){
        this.convoId = convoId;
        this.activeMessages = activeMessages;
        this.users = users;
        this.archivedMessages = archivedMessages;
        this.read = read;
    }

    /**
     * Get the ID of the conversation.
     * @return An integer representing the conversation's ID.
     */
    public Integer getConvoId() {
        return convoId;
    }

    /**
     * Get a list of all messages in this conversation.
     * @return ArrayList of message ids of all the messages in this conversation.
     */
    public List<Integer> getActiveMessages(){
        return activeMessages;
    }

    /**
     * Get list of archived messages in this conversation.
     * @return ArrayList of integers representing the message ids of all archived messages in this conversation.
     */
    public List<Integer> getArchivedMessages(){
        return archivedMessages;
    }

    /**
     * Get users in this conversation.
     * @return ArrayList of strings representing usernames of the users in this conversation.
     */
    public List<String> getUsers(){
        return users;
    }

    /**
     * Adds a message id to this conversation.
     * @param messageId An integer representing the message id.
     */
    public void addMessage(int messageId){
        activeMessages.add(messageId);
    }

    /**
     * Deletes a message id to this conversation.
     * @param messageId An integer representing the message id.
     */
    public void deleteMessage(int messageId){
        activeMessages.remove(new Integer(messageId));
    }

    /**
     * Add messages id to archived message list.
     * @param messageId An integer representing the message id.
     */
    public void archiveMessage(int messageId){
        archivedMessages.add(messageId);
        activeMessages.remove(Integer.valueOf(messageId));
    }

    /**
     * Removes messages id to archived message list.
     * @param messageId An integer representing the message id.
     */
    public void unarchiveMessage(int messageId){
        archivedMessages.remove(Integer.valueOf(messageId));
        for(int i=0; i<activeMessages.size();i++){
            if(activeMessages.get(i) > messageId){
                activeMessages.add(i, messageId);
                return;
            }
        }
    }

    /**
     * Sets this conversation as read by a certain user.
     * @param user username of the user who has unread the conversation.
     */
    public void setConvoasRead(String user){
        this.read.put(user, 1);
    }

    /**
     * Sets this conversation as unread by a certain user.
     * @param user username of the user who has unread the conversation.
     */
    public void setConvoasUnread(String user){
        this.read.put(user, 0);
    }

    /**
     * Gets a list of all users who have read this conversation.
     * @return ArrayList of the usernames of all users of this conversation who have read it.
     */
    public List<String> getReadUsers(){
        ArrayList<String> users = new ArrayList<>();
        for (String user: this.read.keySet()){
            if(this.read.get(user).equals(1)){
                users.add(user);
            }
        }
        users.remove(null);
        return users;
    }
}
