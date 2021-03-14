package usersfeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Represents an abstract UserHandler..
 * @author Manav Shah, Muhammad Sohaib Saqib
 */
public class UserInformation {

    protected final HashMap<String, Attendee> attendees;
    protected final HashMap<String, Organizer> organizers;
    protected final HashMap<String, Speaker> speakers;
    protected final HashMap<String, Admin> admins;

    private String currentUsername;

    /**
     * Constructs an instance of UserInformation.
     */
    public UserInformation() {
        this.attendees = new HashMap<>();
        this.organizers = new HashMap<>();
        this.speakers = new HashMap<>();
        this.admins = new HashMap<>();
    }

    /**
     * Constructs an instance of UserInformation.
     * @param data A String representing the stored data.
     */
    public UserInformation(String data){
        this.attendees = new HashMap<>();
        this.organizers = new HashMap<>();
        this.speakers = new HashMap<>();
        this.admins = new HashMap<>();
        if (data != null && data.length() > 0) {
            ArrayList<String> lines = new ArrayList<>(Arrays.asList(data.split("\n")));
            for (String line :
                    lines) {
                createUser(line);
            }
        }
    }

    /**
     * Checks if given username is taken by any other user or not and creates a User object, given the name, username and usertype.
     * @param line a string containing info from the txt file.
     */
    public void createUser(String line){
        if (line.contains("organizer")){
            Organizer org = new Organizer(line);
            organizers.put(org.getUsername(), org);
        }
        else if (line.contains("admin")){
            Admin adm = new Admin(line);
            admins.put(adm.getUsername(), adm);
        }
        else if (line.contains("attendee")){
            Attendee attendee = new Attendee(line);
            attendees.put(attendee.getUsername(), attendee);
        }
        else if (line.contains("VIP")){
            Attendee VIP = new Attendee(line);
            makeVIP(VIP.getUsername());
            attendees.put(VIP.getUsername(), VIP);
        }
        else if (line.contains("speaker")){
            Speaker speaker = new Speaker(line);
            this.speakers.put(speaker.getUsername(), speaker);
        }
    }

    /**
     * Encodes the data of the UserInformation.
     * @return A String representing the stored data of the UserInformation.
     */
    public String ObjectEncoder(){
        StringBuilder sb = new StringBuilder();
        for (Attendee attendee:
                this.attendees.values()) {
            sb.append(attendee.objectEncoder()).append("-attendee").append("\n");
        }

        for (Organizer og:
                this.organizers.values()) {
            sb.append(og.objectEncoder()).append("-organizer").append("\n");
        }

        for (Speaker sp:
                this.speakers.values()) {
            sb.append(sp.objectEncoder()).append("-speaker").append("\n");
        }

        for (Admin ad:
                this.admins.values()){
            sb.append(ad.objectEncoder()).append("-admin").append("\n");
        }
        return sb.toString();
    }

    /**
     * Gets the Attendee object with the given username.
     * @param username A String representing a username.
     * @return An Attendee object with the given username.
     */
    public Attendee getAttendee(String username) throws UserNotFoundException {
        Attendee attendee = this.attendees.get(username); // returns null if username does not exist
        if (attendee == null){
            throw new UserNotFoundException();
        }
        else {
            return attendee;
        }
    }

    /**
     * Gets the Organizer object with the given username.
     * @param username A String representing a username.
     * @return An Organizer object with the given username.
     */
    public Organizer getOrganizer(String username) throws UserNotFoundException {
        Organizer org = this.organizers.get(username); // returns null if username does not exist
        if (org == null){
            throw new UserNotFoundException();
        }
        else {
            return org;
        }
    }

    /**
     * Gets the Admin object with the given username.
     * @param username A String representing a username.
     * @return An Admin object with the given username.
     */
    public Admin getAdmin(String username) throws UserNotFoundException {
        Admin adm = this.admins.get(username); // returns null if username does not exist
        if (adm == null){
            throw new UserNotFoundException();
        }
        else {
            return adm;
        }
    }

    /**
     * Gets the Speaker object with the given username.
     * @param username A String representing a username.
     * @return An Speaker object with the given username.
     */
    public Speaker getSpeaker(String username) throws UserNotFoundException {
        Speaker speaker = this.speakers.get(username); // returns null if username does not exist
        if (speaker == null){
            throw new UserNotFoundException();
        }
        else {
            return speaker;
        }
    }

    /**
     * Gets the User object with the given username.
     * @param username A String representing a username.
     * @return An User object with the given username.
     */
    public User getUser(String username) throws UserNotFoundException {
        if (attendees.containsKey(username)){
            return attendees.get(username);
        }
        else if (organizers.containsKey(username))
        {
            return organizers.get(username);
        }
        else if (speakers.containsKey(username)){
            return speakers.get(username);
        }
        else if(admins.containsKey(username)){
            return admins.get(username);
        }
        else{
            throw new UserNotFoundException();
        }
    }

    /**
     * Gets the usertype of a User object.
     * @param username A String representing a username.
     * @return A String representing the user type.
     */
    public String getUserType(String username)  throws UserNotFoundException {
        if (this.attendees.containsKey(username)) {
            if (this.getAttendee(username).isVIP()) {
                return "VIP";
            }
            else{
                return "attendee";
            }
        }
        else if (this.organizers.containsKey(username)){
            return "organizer";
        }
        else if (this.speakers.containsKey(username)){
            return "speaker";
        }
        else if(this.admins.containsKey(username)){
            return "admin";
        }
        else throw new UserNotFoundException();
    }

    /**
     * Gets a list of Attendees that have been created.
     * @return A Collection Object of Attendee elements.
     */
    public ArrayList<Attendee> getAllAttendees(){
        return (ArrayList<Attendee>) attendees.values();
    }

    /**
     * Gets a list of usernames of Attendees that have been created.
     * @return A Collection Object of String elements.
     */
    public ArrayList<String> getAllAttendeesUsernames(){
        return new ArrayList<>(attendees.keySet());
    }

    /**
     * Gets a list of Organizers that have been created.
     * @return A Collection Object of Organizer elements.
     */
    public ArrayList<Organizer> getAllOrganizers(){
        return (ArrayList<Organizer>) organizers.values();
    }

    /**
     * Gets a list of usernames of Organizers that have been created.
     * @return A Collection Object of String elements.
     */
    public ArrayList<String> getAllOrganizersUsernames(){
        return new ArrayList<>(organizers.keySet());
    }

    /**
     * Gets a list of Speakers that have been created.
     * @return A Collection Object of Speaker elements.
     */
    public ArrayList<Speaker> getAllSpeakers(){
        return (ArrayList<Speaker>) speakers.values();
    }

    /**
     * Gets a list of usernames of Speakers that have been created.
     * @return A Collection Object of String elements.
     */
    public ArrayList<String> getAllSpeakersUsernames(){
        return new ArrayList<>(speakers.keySet());
    }

    /**
     * Verifies whether the password entered by the User matches the saved password of the User.
     * @param password A string representing the password entered by the User to sign in.
     * @return Boolean true iff the decoded password matches the hashedPassword.
     */
    public boolean authenticate(String username, String password){
        try{
            return getUser(username).authenticateUser(password);
        }catch (UserNotFoundException e){
            return false;
        }
    }

    /**
     * Gets a list of usernames of Users that have been created.
     * @return A Collection Object of String elements.
     */
    public ArrayList<String> getAllUsernames() {
        ArrayList<String> allUsernames = new ArrayList<>();
        allUsernames.addAll(this.getAllAttendeesUsernames());
        allUsernames.addAll(this.getAllOrganizersUsernames());
        allUsernames.addAll(this.getAllSpeakersUsernames());
        return allUsernames;
    }

    /**
     * Gets a list of friends of the given User.
     * @param currentUsername A String representing a username.
     * @return A Collection Object of String elements.
     */
    public ArrayList<String> getFriends(String currentUsername) {
        return this.getUser(currentUsername).getFriends();
    }


    /**
     * Converts the status of the Attendee to VIP status.
     * @param username the String representing a username.
     * @return Boolean true iff instance variable isVIP has been successfully set to true.
     */
    public boolean makeVIP(String username) throws UserNotFoundException{
        if (attendees.containsKey(username)) {
            Attendee attendee = getAttendee(username);
            return attendee.makeVIP();
        } else if (organizers.containsKey(username)){
            Organizer organizer = getOrganizer(username);
            return organizer.makeVIP();
        } else if (admins.containsKey(username)){
            Admin admin = getAdmin(username);
            return admin.makeVIP();
        }else throw new UserNotFoundException();

    }

    /**
     * Gets the name of the current User logged into the system.
     * @param currentUsername A String representing the current username.
     * @return A String representing the name of the current User.
     */
    public String getName(String currentUsername) {
        return getUser(currentUsername).getName();
    }

    /**
     * Sets the name of the current User logged into the system.
     * @param username A String representing the current username.
     */
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    /**
     * Gets the username of the current User logged into the system.
     * @return A String representing the username of the current User.
     */
    public String getCurrentUsername(){return this.currentUsername;}

    /**
     * Gets the usertype of the current User logged into the system.
     * @return A String representing the usertype of the current User.
     */
    public String getCurrentUserType() {
        return this.getUserType(this.currentUsername);
    }
}

