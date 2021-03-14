package messagesfeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A builder that creates a Conversation object.
 * @author Caroline McKenzie, Anastasia Young, Yijia Zhou
 */
public class ConversationBuilder{
    private Integer convoID;
    private List<String> users;
    private List<Integer> activeMessages;
    private List<Integer> archivedMessages;
    private HashMap<String, Integer> read;

    /**
     * Builds the ID of the conversation.
     * @param convoID the ID of the conversation
     */
    public void buildConvoID(Integer convoID){
        this.convoID = convoID;
    }

    /**
     * Builds the list of users in a conversation.
     * @param users A list of usernames
     */
    public void buildUsers(List<String> users){
        this.users = users;
    }

    /**
     * Builds the read hashmap that keeps track of which users have read the conversation
     * @param users a list of usernames
     */
    public void buildRead(List<String> users){
        this.read = new HashMap<>();
        for (String user: users){
            this.read.put(user, 0);
        }
    }

    /**
     * Builds list of active messages of conversation
     */
    public void buildActiveMessages(){
        activeMessages = new ArrayList<>();
    }

    /**
     * Builds list of archived messages of conversation
     */
    public void buildArchivedMessages(){
        archivedMessages = new ArrayList<>();
    }

    /**
     * Builds conversation object
     * @return the conversation object
     */
    public Conversation buildConversation(){
        return new Conversation(users, convoID, read, activeMessages, archivedMessages);
    }
}
