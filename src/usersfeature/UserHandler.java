package usersfeature;

import java.util.*;

/**
 * Represents a UserHandler, a use case for all user entities.
 * @author Manav Shah, Muhammad Sohaib Saqib
 */
public class UserHandler extends UserInformation{

    /**
     * Constructs an instance of UserHandler.
     */
    public UserHandler(){
        super();
    }

    /**
     * Constructs an instance of UserInformation.
     * @param data A String representing the stored data.
     */
    public UserHandler(String data){
        super(data);
    }


    /**
     * Checks if the username is taken by any other user or not.
     * @param username: A String representing a username.
     * @return Boolean true iff the username already exists.
     */
    public boolean isTaken(String username){
        return attendees.containsKey(username) || organizers.containsKey(username) || admins.containsKey(username)
                || speakers.containsKey(username);
    }

    /**
     * Checks if given username is taken by any other user or not and creates a User object,
     * given the name, username and usertype
     * @param username: A String representing a username.
     * @param name: A String representing a name.
     * @param userType: A String representing a usertype.
     */
    public void createUser(String name, String username, String userType) throws UsernameAlreadyExistsException,
            UsernameEqualsNameException {
        if (isTaken(username)){
            throw new UsernameAlreadyExistsException();
        }
        if(name.equals(username)){
            throw new UsernameEqualsNameException();
        }

        if (userType.equalsIgnoreCase("organizer")){
            Organizer org = new Organizer(name, username);
            this.organizers.put(org.getUsername(), org);
        }

        else if (userType.equalsIgnoreCase("admin")){
            Admin adm = new Admin(name, username);
            this.admins.put(adm.getUsername(), adm);
        }

        else if (userType.equalsIgnoreCase("attendee")){
            Attendee attendee = new Attendee(name, username);
            this.attendees.put(attendee.getUsername(), attendee);
        }

        else if (userType.equalsIgnoreCase("VIP")){
            Attendee VIP = new Attendee(name, username);
            attendees.put(VIP.getUsername(), VIP);
            makeVIP(VIP.username);
        }

        else if (userType.equalsIgnoreCase("speaker")) {
            Speaker speaker = new Speaker(name, username);
            this.speakers.put(speaker.getUsername(), speaker);
        }
    }

    /**
     * Authenticates and sets a new password for a given User.
     * @param username: A String representing a username.
     * @param oldPassword: A string representing the old password of the User’s account.
     * @param newPassword: A string representing the new password of the User’s account.
     */
    public void setPassword(String username, String oldPassword, String newPassword) throws UserNotFoundException,
            PasswordNotMatchingException {
        getUser(username).setPassword(oldPassword, newPassword);
    }

    // friends

    /**
     * Adds a friend to the User's friends HashSet.
     * @param username A String representing a username.
     * @param newFriend A User to be added to the friends HashSet.
     * @return Boolean true iff the User is successfully added to the friends HashSet.
     */
    public boolean addFriend(String username, User newFriend) throws UserNotFoundException {
        User person = this.getUser(username);
        return person.addFriend(newFriend);
    }
    /**
     * Overloaded method: Adds a friend to the User's friends HashSet.
     * @param username: A String representing a username.
     * @param friendUsername: A String representing a username.
     * @return Boolean true iff username exists and the given new friend is added to the list of friends of the User.
     */
    public boolean addFriend(String username, String friendUsername) throws UserNotFoundException {
        User friend = this.getUser(friendUsername);
        return addFriend(username, friend);
    }

    /**
     * Removes a friend from the User's friends HashSet.
     * @param username: A String representing a username.
     * @param oldFriend: A String representing a username.
     * @return Boolean true iff username exists and the given new friend is removed from the list of friends of the User.
     */
    public boolean removeFriend(String username, String oldFriend) throws UserNotFoundException {
        User texter = this.getUser(username);
        if (texter == null){
            return false;
        }
        return texter.removeFriend(oldFriend);
    }

    /**
     * Gets a list of events that a given organizer has created.
     * @param username : A String representing a username.
     * @return An ArrayList representing unique event IDs as ints.
     */
    public ArrayList<Integer> getCreatedEvents(String username) throws UserNotFoundException {
        if (organizers.containsKey(username)){
            Organizer temp = getOrganizer(username);
            if (temp == null){
                return null;
            }
            return temp.getCreatedEvents();
            }
        else{
            Admin temp = getAdmin(username);
            if (temp == null){
                return null;
            }
            return temp.getCreatedEvents();
        }
    }

    /**
     * Adds a created event to the Organizer's createdEvent HashSet.
     * @param username: A String representing an Organizer username.
     * @param event: An int representing the unique ID for an Event.
     * @param speakerName: A String representing a name of the speaker to be added.
     */
    public void createEvent(String username, int event, String speakerName) {
        Organizer organizer = getOrganizer(username);
        getSpeaker(speakerName).addTalk(event);
        organizer.createEvent(event);

        //This method's functionality is commented out because it produces bugs that we could not solve in the given
        //moment. As a result, it will be impossible to see what events Speakers are speaking at from the User Profile
        //page, as well as it will be impossible to see what events a Organizer made. This was found last minute, and
        //would crash the code, hence why we were unable to fix this.
    }

    /**
     * Removes a created event from the Organizer's createdEvent HashSet.
     * @param username: A String representing an Organizer username.
     * @param event: An int representing the unique ID for an Event.
     * @param speakerName: A String representing a name of the speaker to be added.
     */
    public void deleteEvent(String username, int event, String speakerName) throws UserNotFoundException {
        Organizer organizer = getOrganizer(username);
        for (Attendee a : attendees.values()) {
            a.removeEvent(event);
        }
        for (Organizer o : organizers.values()) {
            o.removeEvent(event);
        }
        try {
            speakers.get(speakerName).deleteTalk(event);
        } catch (NullPointerException e) {
            throw new UserNotFoundException();
        }
        organizer.deleteEvent(event);
        organizer.deleteSpeaker(speakerName);
    }

    /**
     * Adds an event to the given User's list of events.
     * @param event An int representing the unique ID for an Event.
     * @param username A String representing a username.
     * @return Boolean true iff event is successfully added to the User's list of events.
     */
    public boolean addEventToUserList(int event, String username) {
        try {
            Attendee attendee = getAttendee(username);
            return attendee.addEvent(event);
        } catch (UserNotFoundException ignored) {
        }
        try {
            Organizer organizer = getOrganizer(username);
            return organizer.addEvent(event);
        } catch (UserNotFoundException ignored) {
            return false;
        }
    }

    /**
     * Removes an event to the given User's list of events.
     * @param event An int representing the unique ID for an Event.
     * @param username A String representing a username.
     */
    public void removeEventFromUserList(int event, String username) {
        try {
            Attendee attendee = getAttendee(username);
            attendee.removeEvent(event);
            return;
        } catch (UserNotFoundException ignored) {}
        try {
            Organizer organizer = getOrganizer(username);
            organizer.removeEvent(event);
        } catch (UserNotFoundException ignored) {
        }
    }

    /**
     * Represents the name and username of the given User as a String.
     * @param currentUsername A String representing a username.
     * @return A String representing the User's information.
     */
    public String userString(String currentUsername) {
        return this.getUser(currentUsername).toString();
    }

    /**
     * Reverts the status of the Attendee from VIP status.
     * @param username the username String of the User.
     * @return Returns true iff instance variable isVIP has been successfully set to false.
     */
    public boolean removeVIP(String username){
        Attendee attendee = getAttendee(username);
        return attendee.removeVIP();
    }

    /**
     * Gets a list of Events that the Speaker will be talking at.
     * @param username A String representing the username of the Speaker.
     * @return An ArrayList representing unique event Ids as ints.
     */
    public ArrayList<Integer> getTalks(String username) throws UserNotFoundException {
        Speaker temp = getSpeaker(username);
        if (temp == null){
            return null;
        }
        return temp.getTalks();
    }

    /**
     * Adds a Speaker to the speakersCreated HashSet of the given User.
     * @param currentUsername A String representing the username of the given User.
     * @param speakerName A String representing the username of the Speaker to be added.
     */
    public void addCreatedSpeaker(String currentUsername, String speakerName) {
        if (this.organizers.containsKey(currentUsername)){
            Organizer org = this.organizers.get(currentUsername);
            org.addSpeaker(speakerName);
        }
        else if (this.admins.containsKey(currentUsername)){
            Admin adm = this.admins.get(currentUsername);
            adm.addSpeaker(speakerName);
        }
    }



}
