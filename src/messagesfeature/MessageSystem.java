package messagesfeature;

import eventsfeature.EventNotFoundException;
import usersfeature.UserHandler;
import eventsfeature.EventHandler;
import usersfeature.UserNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Represents the Controller class for the Messaging System.
 * @author Caroline McKenzie, Anastasia Young, Yijia Zhou
 */
public class MessageSystem{

    private List<String> allUsers;
    private final MessageHandler messageHandler;
    private final UserHandler userHandler;
    private final EventHandler eventHandler;
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    /**
     * Constructs the message system.
     * @param data the data for the file reading
     * @param userHandler the userhandler object
     * @param eventHandler the eventhandler object
     * @param messageHandler the messagehandler object
     */
    public MessageSystem(String data, UserHandler userHandler, EventHandler eventHandler,
                         MessageHandler messageHandler){
        this.userHandler = userHandler;
        this.eventHandler = eventHandler;
        this.messageHandler = messageHandler;
        this.fileReader(data);
    }

    /**
     * Gets the username of the current user
     * @return a string that represents the username of the user
     */
    public String getUsername(){
        return userHandler.getCurrentUsername();
    }

    /**
     * Gets the user type of the current type
     * @return a string that represents the user type of the user
     */
    public String getUserType(){ return this.userHandler.getUserType(getUsername()); }

    /**
     * Updates the variable allUsers whenever a new user signs up to the system.
     */
    public void updateAllUsers(){
        this.allUsers = userHandler.getAllUsernames();
    }


    /**
     * Asks user to input a list of users and reads this input. Throws exception
     * if a username that was inputted does not exist.
     * @return list of strings representing usernames.
     * @throws UserNotFoundException if a user does not exist.
     * @throws UsersNotUniqueException if user inputs duplicate usernames
     */
    public List<String> checkUsers (List<String> users){
        boolean exists = false;
        //Loop over all users to check if usernames inputted exist
        for(String s: users){
            for(String u: allUsers){
                if(s.equals(u)){
                    exists = true;
                    break;
                }
            }
            if(!exists){
                throw new UserNotFoundException();
            }
            for (String u: users){
                if(Collections.frequency(users, u) > 1){
                    throw new UsersNotUniqueException();
                }
            }
        }
        // adds current user to list of users being messaged
        boolean userexists = false;
        int i = 0;
        while (i < users.size() && !userexists){
            if (users.get(i).equals(getUsername())){
                userexists = true;
            }
            i++;
        }
        if (!userexists){
            users.add(getUsername());
        }

        users.remove(null);

        return users;
    }

    /**
     * Checks if there is an organizer in a given list of usernames. Throws an exception
     * if an organizer is found.
     * @param users a list of usernames
     * @throws CannotMessageUserException
     */
    public void checkForOrganizer(List<String> users) throws CannotMessageUserException{
        boolean organizer = false;
        for (String u: users){
            if (userHandler.getUserType(u).equals("organizer")) {
                organizer = true;
            }
        }
        if(organizer){
            throw new CannotMessageUserException();
        }
    }

    /**
     * Sends a message to a conversation given the conversation ID and message
     * content
     */
    public void sendMessage(Integer convoID, String content){
        LocalDateTime timestamp = LocalDateTime.now();
        this.sendMessageToConvo(convoID, getUsername(), content, 0, timestamp);
    }

    /**
     * Replies to a message given the conversation ID, the message content,
     * and the parent ID
     */
    public void reply(Integer convoId, String content, Integer parentId){
        LocalDateTime timestamp = LocalDateTime.now();
        this.sendMessageToConvo(convoId, getUsername(), content, parentId, timestamp);
    }

    /**
     * Returns a list that contains the information about a message given its ID
     * @param messageId an integer representing the message ID
     * @return a list representing the information of the message
     */
    public List<String> getMessageInfo(Integer messageId){
        return messageHandler.messageInfo(messageId);
    }

    /**
     * Returns all the message IDs inside a conversation given the conversation ID
     * @param convoId the ID of the conversation
     * @return a list of message IDs
     */
    public List<Integer> getConvoMessages(Integer convoId){
        return messageHandler.getConvoActive(convoId);
    }

    /**
     * Returns the conversation IDs that a user is a part of
     * @return a list of conversation IDs
     */
    public List<Integer> getUserConversations(){
        return messageHandler.getUserConversations(getUsername());
    }

    /**
     * Returns the conversation IDs of the conversations that have been read by the user
     * @return a list of conversation IDs
     */
    public List<Integer> getUserReadConversations(){
        return messageHandler.getUserReadConversations(getUsername());
    }

    /**
     * Sets conversation as read by the current user.
     * @param convoId id of the conversation being set as read.
     */
    public void setConvoRead(Integer convoId){
        messageHandler.setConvoasRead(getUsername(), convoId);
    }

    /**
     * Sets conversation as unread by the current user.
     * @param convoId id of the conversation being set as unread.
     */
    public void setConvoUnread(Integer convoId){
        messageHandler.setConvoasUnread(getUsername(), convoId);
    }

    /**
     * Creates a conversation given a list of usernames.
     * @param users List of strings representing usernames of users in the conversation.
     * @return id of the conversation created.
     */
    public Integer createConvo(List<String> users){
        users.remove(null);
        Integer convoId = messageHandler.createConversation(users);
        messageHandler.setConvoasRead(getUsername(), convoId);
        return convoId;
    }

    /**
     * Creates a new conversation given a list of usernames and sends a message from the current user.
     * @param users List of strings representing usernames of users in the conversation.
     * @param content the message being sent in the conversation.
     * @throws ConvoAlreadyExistsException If a conversation with that id already exists.
     */
    public void sendNewConvo(List<String> users, String content) throws ConvoAlreadyExistsException{
        if (convoExistsWithUsers(users)) {
            throw new ConvoAlreadyExistsException();
        }
        Integer convoId = createConvo(users);
        sendMessage(convoId, content);
    }

    /**
     * Gets a list of names of attendees for an Event within the EventHandler schedule.
     * @param eventId The unique ID of the Event in question.
     * @return Returns the list of attendees for the Event in Schedule or an empty list if no such Event exists.
     * @throws EventNotFoundException If the event does not exist in the schedule.
     */
    public List<String> getAttendeesAtEvent(Integer eventId){
        return eventHandler.getAttendeeNames(eventId);
    }

    /**
     * Gets a list of Attendees that have been created in AttendeeHandler.
     * @return Returns a Collection Object of Attendee elements.
     */
    public List<String> getAllAttendees(){
        return userHandler.getAllAttendeesUsernames();
    }

    /**
     * Gets a list of Speakers that have been created by an Organizer.
     * @return Returns a Collection Object of Speaker elements.
     */
    public List<String> getAllSpeakers(){
        return userHandler.getAllSpeakersUsernames();
    }

    /**
     * Checks if a conversation with the given list of users already exists.
     * @param users List of strings representing usernames of users in the conversation.
     * @return boolean value that represents whether or not there already exists a conversation
     * with these users.
     */
    public boolean convoExistsWithUsers(List<String> users){
        return messageHandler.convoExistsWithUsers(users);
    }


    /**
     * Sends a message to a given conversation the user is a part of when file is read.
     * @param convoId Id value of the conversation the message is to be sent to.
     * @param sender String of sender of message.
     * @param content String of content of the message.
     * @param parentId Id value of the message that is being replied to.
     * @param timestamp Time when the message was sent.
     */
    public void sendMessageToConvo(int convoId, String sender, String content, int parentId, LocalDateTime timestamp) {
        messageHandler.createMessage(content, sender, parentId, timestamp, convoId);
    }


    /**
     * Unarchives a message.
     * @param messageId id of the message being unarchived.
     */
    public void unarchiveMessage(Integer messageId, Integer convoId){
        messageHandler.unarchiveMessage(messageId, convoId);
    }

    /**
     * Archives a message.
     * @param messageId id of the message being archived.
     */
    public void archiveMessage(Integer messageId){
        Integer convoId = messageHandler.getConvoWithMessage(messageId);
        messageHandler.archiveMessage(messageId, convoId);
    }

    /**
     * Deletes a message.
     * @param m_id id of the message being deleted.
     */
    public void deleteMessage(Integer m_id){
        Integer convoId = messageHandler.getConvoWithMessage(m_id);
        messageHandler.deleteMessage(m_id, convoId);
    }

    /**
     * Gets an list of all the members of a conversation.
     * @param convoId the conversation id
     * @return An list of all the usernames of conversation members.
     */
    public List<String> getUsers(Integer convoId){
        return messageHandler.getConvoUsers(convoId);
    }

    /**
     * Gets a list of all users who have read the conversation.
     * @param convoID the conversation id
     * @return List of the usernames of all members of the conversation who have read it.
     */
    public List<String> getReadUsers(Integer convoID){
        return messageHandler.getReadUsers(convoID);
    }

    /**
     * Gets an list of all the archived messages within a conversation.
     * @param convoId the conversation id
     * @return List of all archived message ids within a conversation.
     */
    public List<Integer> getConvoArchive (int convoId){
        return messageHandler.getConvoArchive(convoId);
    }

    /**
     * Method calls the Persistence class to update the file which stores the data associated with Messages.
     * @return the string of information to save to the file.
     */
    public String fileWriter(){
        return messageHandler.fileWriter();
    }

    /**
     * Method reads the information related to a singular message.
     * @param messageInfo A list of strings that represents the information associated with a message.
     * @param convoId The conversation id of the message.
     */
    public void readMessage(String [] messageInfo, Integer convoId) {
        String dateTime = messageInfo[4].replace("T", " ");
        LocalDateTime passedin_time = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(DATE_FORMAT));
        sendMessageToConvo(convoId, messageInfo[2], messageInfo[1], Integer.parseInt(messageInfo[3]),
                passedin_time);
        Integer messageId = Integer.parseInt(messageInfo[0]);
        if(messageInfo.length == (6)){
            messageHandler.archiveMessage(messageId, convoId);
        }
    }

    /**
     * Method calls the Persistence class to read the file which stores the data associated with Messages.
     * @param input the string of information retrieved from the file.
     */
    public void fileReader(String input){
        if (input.isEmpty()){
            return;
        }
        ArrayList<String> inputSplit = new ArrayList<>(Arrays.asList(input.split("&&"))); // splits text file into different conversations
        System.out.println(inputSplit.remove(inputSplit.size()-1));
        for (String line : inputSplit) {
            String[] subdata = line.split("##"); //split into messages
            Integer convoId = Integer.parseInt(subdata[0]); //conversation id
            String[] conversationParticipants = subdata[1].split("%%"); // adds participants of a conversation to an arraylist.
            ArrayList<String> passedin_participants = new ArrayList<>(Arrays.asList(conversationParticipants));
            createConvo(passedin_participants);
            String[] initial_read_users = subdata[2].split("%%"); // list of all users who have read convo
            ArrayList<String> readUsers = new ArrayList<>(Arrays.asList(initial_read_users));
            for(String user: readUsers){
                messageHandler.setConvoasRead(user, convoId);
            }
            for (int i = 3; i < subdata.length; i++) {
                String[] messageInfo = subdata[i].split("%%");
                readMessage(messageInfo, convoId);
            }
        }

    }
}