package messagesfeature;

import java.time.format.DateTimeFormatter;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Use case class that handles messages and conversations.
 * @authors Caroline McKenzie, Anastasia Young, Yijia Zhou
 */

public class MessageHandler {

    private List<Message> messages;
    private List<Conversation> conversations;
    private int newMessageId;
    private int newConvoId;
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
    //builder objects
    private ConversationBuilder convoBuilder;
    private MessageBuilder messageBuilder;

    /**
     * Constructor for MessageHandler
     */
    public MessageHandler(){
        this.messages = new ArrayList<> ();
        this.conversations = new ArrayList<> ();
        this.newMessageId = 1;
        this.newConvoId = 1;
        this.convoBuilder = new ConversationBuilder();
        this.messageBuilder = new MessageBuilder();
    }

    /**
     * Get a list of the ids of all conversations in the entire program.
     * @return list of Conversation ids.
     */
    public List<Integer> getConversationIDs(){
        ArrayList<Integer> list_id = new ArrayList<Integer>();
        for (Conversation c: conversations){
            list_id.add(c.getConvoId());
        }
        return list_id;
        }


    /**
     * Construct a message that is a reply to another message and adds it to the list of messages.
     * @param content The content of the message.
     * @param sender The sender of the message.
     * @param parentId The ID of the message that this message is replying to.
     * @param timestamp The time that the message has been created.
     */
    public void createMessage(String content, String sender, int parentId, LocalDateTime timestamp, int convoId){
        String dateTime = DateTimeFormatter.ofPattern(DATE_FORMAT).format(timestamp);
        LocalDateTime actual_dateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(DATE_FORMAT));
        // build message
        messageBuilder.buildMessageID(this.getNewMessageId());
        messageBuilder.buildContent(content);
        messageBuilder.buildSender(sender);
        messageBuilder.buildParentID(parentId);
        messageBuilder.buildTimestamp(actual_dateTime);
        Message m = messageBuilder.buildMessage();
        // add message to conversation
        messages.add(m);
        Conversation c = getConvoWithId(convoId);
        c.addMessage(m.getId());
    }

    /**
     * Construct a new conversation.
     * @param users An Arraylist of the usernames of all the members of the conversation.
     * @return the conversation id of the new conversation.
     */
    public Integer createConversation(List<String> users){
        convoBuilder.buildConvoID(getNewConvoId());
        convoBuilder.buildUsers(users);
        convoBuilder.buildRead(users);
        convoBuilder.buildActiveMessages();
        convoBuilder.buildArchivedMessages();
        Conversation c = convoBuilder.buildConversation();
        conversations.add(c);
        return c.getConvoId();
    }

    /**
     * Given an id value, this method finds the corresponding conversation object if it exists.
     * @param convoId Id value of the conversation to find.
     * @return Conversation object matching the id if it exists.
     */
    private Conversation getConvoWithId(int convoId){
        for(Conversation c: conversations){
            if(c.getConvoId() == convoId){
                return c;
            }
        }
        ArrayList<String> emptyUsers = new ArrayList<>();
        // build fake message
        convoBuilder.buildConvoID(-1);
        convoBuilder.buildUsers(emptyUsers);
        convoBuilder.buildRead(emptyUsers);
        convoBuilder.buildActiveMessages();
        convoBuilder.buildArchivedMessages();
        return convoBuilder.buildConversation();
    }

    /**
     * Gets the message info of a specific message.
     * @param id The id of the message.
     * @return A list that contains message's content, sender, parent ID, time stamp, and if the message has been read.
     */
    public List<String> messageInfo(int id){
        ArrayList<String> info = new ArrayList<>();
        if(messages.size() < id){
            return info;
        }
        info.add(messages.get(id-1).getContent());
        info.add(messages.get(id-1).getSender());
        info.add(Integer.toString(messages.get(id-1).getParentId()));
        info.add(messages.get(id-1).getTimestamp().toString());
        return info;
    }

    /**
     * Gets an Arraylist of all the members of a conversation.
     * @param convoId the conversation id
     * @return An ArrayList of all the usernames of conversation members.
     */
    public List<String> getConvoUsers(int convoId){
        Conversation c = getConvoWithId(convoId);
        return c.getUsers();
    }

    /**
     * Archives a message in a given conversation.
     * @param messageId id of the message being archived.
     * @param convoId conversation id.
     */
    public void archiveMessage(int messageId, int convoId){
        Conversation c = getConvoWithId(convoId);
        c.archiveMessage(messageId);
    }


    /**
     * Unarchives a message in a given conversation.
     * @param messageId id of the message being unarchived.
     * @param convoId conversation id.
     */
    public void unarchiveMessage(int messageId, int convoId){
        Conversation c = getConvoWithId(convoId);
        c.unarchiveMessage(messageId);
    }

    /**
     * Deletes a message from a given conversation.
     * @param messageId id of the message being deleted.
     * @param convoId conversation id.
     */
    public void deleteMessage(int messageId, int convoId){
        Conversation c = getConvoWithId(convoId);
        c.deleteMessage(messageId);
    }

    /**
     * Gets the message object given a message id.
     * @param messageId The id value of the message.
     * @return Message object.
     * @throws MessageNotFoundException if the inputted message id value does not match
     * any messages in this conversation.
     */
    public Message getMessageWithId(int messageId) throws MessageNotFoundException{
        for (Message m: messages){
            if(m.getId() == (messageId)){
                return m;
            }
        }
        throw new MessageNotFoundException();
    }

    /**
     * Checks if a conversation with the given list of users already exists.
     * @param users ArrayList of strings representing usernames of users in the conversation.
     * @return boolean value that represents whether or not there already exists a conversation
     * with these users.
     */
    public boolean convoExistsWithUsers(List<String> users){
        boolean exists = false;
        for(Conversation c: conversations){
            if(users.equals(c.getUsers())){
                exists = true;
                break;
            }
        }
        return exists;
    }

    /**
     * Get a list of all conversation ids that the logged in user is a part of.
     * @return list of conversation ids.
     */
    public List<Integer> getUserConversations(String username){
        ArrayList<Integer> convoIds = new ArrayList<>();
        for(Conversation c: conversations){
            if(c.getUsers().contains(username)){
                convoIds.add(c.getConvoId());
            }
        }
        return convoIds;
    }

    /**
     * Gets all message ids of messages from a given conversation.
     * @param convoId Id value of the conversation.
     * @return List of Message objects that represent all messages in the conversation.
     */
    public List<Integer> getMessagesFromConvo(int convoId){
        List<Integer> messages = this.getConvoWithId(convoId).getActiveMessages();
        messages.addAll(this.getConvoWithId(convoId).getArchivedMessages());
        return messages;
    }

    /**
     * Gets the id value of the conversation that a message is in given its message id.
     * @param messageId Id value of the message.
     * @return Integer value representing the id value of the conversation the message is in.
     * @throws MessageNotFoundException if the message id does not exist.
     */
    public Integer getConvoWithMessage(int messageId) throws MessageNotFoundException {
        for (Conversation c: conversations){
            List<Integer> messages = this.getMessagesFromConvo(c.getConvoId());
            for(Integer i : messages){
                if(i == messageId){
                    return c.getConvoId();
                }
            }
        }
        throw new MessageNotFoundException();
    }

    /**
     * Gets an list of all the archived messages within a conversation.
     * @param convoId the conversation id
     * @return List of all archived message ids within a conversation.
     */
    public List<Integer> getConvoArchive (int convoId){
        Conversation c = getConvoWithId(convoId);
        return c.getArchivedMessages();
    }

    /**
     * Gets an list of all the active messages within a conversation.
     * @param convoId the conversation id
     * @return List of all active message ids within a conversation.
     */
    public List<Integer> getConvoActive(int convoId){
        Conversation c = getConvoWithId(convoId);
        return c.getActiveMessages();
    }

    private String encryptMessage(int id, boolean archived){
        StringBuilder s = new StringBuilder();
        Integer big_id = id;
        s.append(big_id.toString()).append("%%");
        for (String sub_info: messageInfo(id)){
            s.append(sub_info).append("%%");
        }
        if(archived){
            s.append("**");
            s.append("%%");
        }
        return s.toString();
    }

    private String encryptUsers(List<String> convoUsers) {
        StringBuilder s = new StringBuilder();
        for (String sub_info: convoUsers){
            s.append(sub_info).append("%%");
        }
        return s.toString();
    }

    private String encryptConversation(Integer convoId){
        StringBuilder convo_info_stringb = new StringBuilder();
        convo_info_stringb.append(convoId.toString());
        convo_info_stringb.append("##");
        convo_info_stringb.append(encryptUsers(getConvoUsers(convoId)));
        convo_info_stringb.append("##");
        convo_info_stringb.append(encryptUsers(getReadUsers(convoId)));
        convo_info_stringb.append("##");
        for (Integer m_id : getConvoActive(convoId)) {
            Message m = getMessageWithId(m_id);
            convo_info_stringb.append(encryptMessage(m.getId(), false)).append("##");
        }
        for (Integer m_id : getConvoArchive(convoId)) {
            Message m = getMessageWithId(m_id);
            convo_info_stringb.append(encryptMessage(m.getId(), true)).append("##");
        }
        return convo_info_stringb.toString();
    }

    /**
     * Sets a conversation as read by a certain user.
     * @param user the username of the user reading the message.
     * @param convoID the id of the conversation being marked as read.
     */
    public void setConvoasRead(String user, Integer convoID){
        Conversation c = getConvoWithId(convoID);
        c.setConvoasRead(user);
    }

    /**
     * Sets conversation as unread by a certain user.
     * @param user username of the user who has unread the conversation.
     * @param convoId id of the conversation being marked as unread.
     */
    public void setConvoasUnread(String user, Integer convoId){
        getConvoWithId(convoId).setConvoasUnread(user);
    }

    /**
     * Gets a list of all users who have read the conversation.
     * @param convoID the conversation id
     * @return List of the usernames of all users of the conversation who have read it.
     */
    public List<String> getReadUsers(Integer convoID){
        Conversation c = getConvoWithId(convoID);
        return c.getReadUsers();
    }

    /**
     * Gets a list of all the conversations the user has read.
     * @param username username of user
     * @return List of all the ids of the conversations the user has read.
     */
    public List<Integer> getUserReadConversations(String username){
        ArrayList<Integer> convoIds = new ArrayList<>();
        for (Conversation c: conversations){
            for (String user: c.getReadUsers()){
                if (user.equals(username)){
                    convoIds.add(c.getConvoId());
                }
            }
        }
        return convoIds;
    }

    private int getNewMessageId(){
        int i = this.newMessageId;
        this.newMessageId++;
        return i;
    }

    private int getNewConvoId(){
        int i = this.newConvoId;
        this.newConvoId++;
        return i;
    }

    /**
     * Method calls the Persistence class to update the file which stores the data associated with Messages.
     * @return the string of information to save to the file.
     */
    public String fileWriter(){
        StringBuilder all_msg_info_ever = new StringBuilder();
        for (Integer c_id : getConversationIDs()) {
            all_msg_info_ever.append(encryptConversation(c_id));
            all_msg_info_ever.append("&&");
        }
        return all_msg_info_ever.toString();
    }

}
