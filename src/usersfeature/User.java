package usersfeature;

import java.util.*;

/** Represents a User.
 * @author Manav Shah, Muhammad Sohaib Saqib
 */
public abstract class User {
    protected String name;
    protected String username;
    private String hashedPassword;

    private HashSet<String> friends;

    private HashSet<Integer> convoIds;

    /**
     * Constructs an instance of User.
     * @param name A string representing a name.
     * @param username A string representing a username.
     */
    public User(String name, String username){
        this.name = name;
        this.username = username;
        this.hashedPassword = null;

        this.convoIds = new HashSet<>();
        this.friends = new HashSet<>();
    }

    /**
     * Empty Constructor
     */
    public User(String data){
        userReader(data);
    }

    /**
     * Encodes the data of the User.
     * @return A String representing the stored data of the User.
     */
    public String objectEncoder(){
        String friends;
        StringBuilder f = new StringBuilder();
        if (!this.friends.isEmpty()) {
            for (String friend : this.friends) {
                String s = "," + friend;
                f.append(s);
            }
            friends = f.substring(1);
        }
        else{
            friends = "";
        }

        String convoIDs;
        StringBuilder c = new StringBuilder();
        if (!this.convoIds.isEmpty()) {
            for (int con : this.convoIds) {
                String s = "," + con;
                c.append(s);
            }
            convoIDs = c.substring(1);
        }
        else{
            convoIDs = "";
        }
        return this.name + "-" + this.username + "-" + this.hashedPassword + "-" + friends + "-" + convoIDs;
    }

    /**
     * Decodes the data of the User.
     * @param data A string representing the stored data.
     */
    public void userReader(String data) {
        if (data != null && data.length() > 0) {
            ArrayList<String> lists = new ArrayList<>(Arrays.asList(data.split("-")));

            this.name = lists.get(0);
            this.username = lists.get(1);
            this.hashedPassword = lists.get(2);
            ArrayList<String> dost = new ArrayList<>(Arrays.asList(lists.get(3).split(",")));
            if (!dost.isEmpty()  && !dost.get(0).equals("")){
                this.friends = new HashSet<>(dost);
            }
            else{
                this.friends = new HashSet<>();
            }

            ArrayList<String> convoIds = new ArrayList<>(Arrays.asList(lists.get(4).split(",")));
            this.convoIds = new HashSet<>();
            for (String num : convoIds) {
                if (!num.equals("")) {
                    this.convoIds.add(Integer.parseInt(num));
                }
            }
        }
    }

    /**
     * Represents the name and username of the User as a String.
     * @return A String representing the User's information.
     */
    public abstract String toString();


    // Login functionality
    /**
     * Sets the password for the User's account.
     * @param oldPassword A string representing the old password of the User’s account.
     * @param password A string representing the new password of the User’s account.
     */
    public void setPassword(String oldPassword, String password){
        if (authenticateUser(oldPassword)){
            this.hashedPassword = encodedPassword(password);
        }
        else{
            throw new PasswordNotMatchingException();}
    }

    /**
     * Verifies whether the password entered by the User matches the saved hashedPassword of authentication purposes.
     * @param password A string representing the password entered by the User to sign in.
     * @return Boolean true iff the decoded password matches the hashedPassword.
     */
    public boolean authenticateUser(String password){
        if (this.hashedPassword == null){
            return true;
        }
        return this.hashedPassword.equals(encodedPassword(password));
    }

    /**
     * Encodes the given stringPassword into an encoded form.
     * @param stringPassword A string representing the entered password to be encoded.
     * @return A string representing the encoded password.
     */
    public String encodedPassword(String stringPassword){
        return stringPassword; // temporary.
    }

    /**
     * Gets the username of the User.
     * @return Returns the username of the User, as a String.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the name of the User.
     * @return A String representing the name of the User.
     */
    public String getName(){return this.name;}


    // friends
    /**
     * Verifies whether a given User is a friend of the User.
     * @param username A string representing the name of the friend.
     * @return Boolean true iff the friend's username is a key in the friends HashSet for the User.
     */
    public boolean isFriend(String username){
        return this.friends.contains(username);
    }

    /**
     * Gets a list of Users that are friends with the User.
     * @return A string representing an ArrayList of String elements.
     */
    public ArrayList<String> getFriends(){
        return new ArrayList<>(friends);
    }


    /**
     * Adds a friend to the User's friends HashSet.
     * @param friend A User to be added to the friends HashSet.
     * @return Boolean true iff the User is successfully added to the friends HashSet.
     */
    public boolean addFriend(User friend){
        if (isFriend(friend.getUsername())){
            return false;
        }
        this.friends.add(friend.getUsername());
        return true;
    }

    /**
     * Removes a friend from the friends HashSet.
     * @param username A String representing the username of a User.
     * @return Boolean true iff the User is successfully removed from the friends HashSet.
     */
    public boolean removeFriend(String username){
        if (isFriend(username)){
            this.friends.remove(username);
            return true;
        }
        return false;
    }

}
