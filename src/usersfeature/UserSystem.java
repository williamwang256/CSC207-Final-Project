package usersfeature;

import java.util.ArrayList;

/**
 * Represents a UserSystem, a controller for all classes within the UsersFeature package.
 * @author Manav Shah, Muhammad Sohaib Saqib
 */
public class UserSystem{

    private final UserHandler userHandler;

    /**
     * Constructs an instance of UserSystem.
     * @param userHandler A UserHandler object that is stored by the UserSystem.
     */
    public UserSystem(UserHandler userHandler){
        this.userHandler = userHandler;
    }

    /**
     * Gets the username of the current User logged into the system.
     * @return A String representing the username of the current User.
     */
    public String getCurrentUsername() {
        return this.userHandler.getCurrentUsername();
    }

    /**
     * Gets the usertype of the current User logged into the system.
     * @return A String representing the usertype of the current User.
     */
    public String getCurrentUserType() {
        return this.userHandler.getCurrentUserType();
    }

    /**
     * Sets the name of the current User logged into the system.
     * @param username A String representing the current username.
     */
    public void setCurrentUsername(String username){
        this.userHandler.setCurrentUsername(username);
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
        this.userHandler.createUser(name, username, userType);
    }

    /**
     * Authenticates and sets a new password for a given User.
     * @param username: A String representing a username.
     * @param oldPassword: A string representing the old password of the User’s account.
     * @param newPassword: A string representing the new password of the User’s account.
     */
    public void setPassword(String username, String oldPassword, String newPassword)
            throws PasswordNotMatchingException {
        this.userHandler.setPassword(username, oldPassword, newPassword);
    }

    /**
     * Verifies whether the password entered by the User matches the saved password of the User.
     * @param password A string representing the password entered by the User to sign in.
     * @return Boolean true iff the decoded password matches the hashedPassword.
     */
    public boolean authenticate(String username, String password) {
        return userHandler.authenticate(username, password);
    }

    /**
     * Gets the usertype of a User object.
     * @param username A String representing a username.
     * @return A String representing the user type.
     */
    public String getUserType(String username) {
        return userHandler.getUserType(username);
    }

    /**
     * Represents the name and username of the given User as a String.
     * @param currentUsername A String representing a username.
     * @return A String representing the User's information.
     */
    public String getUserInformation(String currentUsername){
        return userHandler.userString(currentUsername);
    }

    /**
     * Gets a list of friends of the given User.
     * @param username A String representing a username.
     * @return A Collection Object of String elements.
     */
    public ArrayList<String> getFriends(String username) {
        return userHandler.getFriends(username);
    }

    /**
     * Gets a list of events that a given organizer has created.
     * @param username : A String representing a username.
     * @return An ArrayList representing unique event IDs as ints.
     */
    public ArrayList<Integer> getCreatedEvents(String username){
        return userHandler.getCreatedEvents(username);
    }

    /**
     * Gets a list of Events that the Speaker will be talking at.
     * @param username A String representing the username of the Speaker.
     * @return An ArrayList representing unique event Ids as ints.
     */
    public ArrayList<Integer> getTalks(String username) {
        return userHandler.getTalks(username);
    }

    /**
     * Overloaded method: Adds a friend to the User's friends HashSet.
     * @param username: A String representing a username.
     * @param newFriend: A String representing a username.
     * @return Boolean true iff username exists and the given new friend is added to the list of friends of the User.
     */
    public boolean addFriend(String username, String newFriend) throws UserNotFoundException {
        return userHandler.addFriend(username, newFriend);
    }

    /**
     * Removes a friend from the User's friends HashSet.
     * @param username: A String representing a username.
     * @param oldFriend: A String representing a username.
     * @return Boolean true iff username exists and the given new friend is removed from the list of friends of the User.
     */
    public boolean removeFriend(String username, String oldFriend) {
        return userHandler.removeFriend(username, oldFriend);
    }

    /**
     * Converts the status of the Attendee to VIP status.
     * @param username the String representing a username.
     * @return Boolean true iff instance variable isVIP has been successfully set to true.
     */
    public boolean makeVIP(String username){
        return userHandler.makeVIP(username);
    }

    /**
     * Reverts the status of the Attendee from VIP status.
     * @param username the username String of the User.
     * @return Returns true iff instance variable isVIP has been successfully set to false.
     */
    public boolean removeVIP(String username) {
        return userHandler.removeVIP(username);
    }

    /**
     * Gets the name of the current User logged into the system.
     * @param currentUsername A String representing the current username.
     * @return A String representing the name of the current User.
     */
    public String getName(String currentUsername) {
        return userHandler.getName(currentUsername);
    }

    /**
     * Adds a Speaker to the speakersCreated HashSet of the given User.
     * @param currentUsername A String representing the username of the given User.
     * @param speakerName A String representing the username of the Speaker to be added.
     */
    public void addCreatedSpeaker(String currentUsername, String speakerName) {
        this.userHandler.addCreatedSpeaker(currentUsername, speakerName);
    }


}
