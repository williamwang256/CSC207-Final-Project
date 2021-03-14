package usersfeature;

/** Represents an Admin.
 * @author Manav Shah, Muhammad Sohaib Saqib
 */
public class Admin extends Organizer {


    /**
     * Constructs an instance of Admin.
     *
     * @param name     A string representing a name.
     * @param username A string representing a username.
     */
    public Admin(String name, String username) {
        super(name, username);
    }

    /**
     * Constructs an instance of Admin.
     *
     * @param data A string representing the stored data.
     */
    public Admin(String data) {
        super(data);
    }

    /**
     * Represents the name and username of the Admin as a String.
     *
     * @return A String representing the Admin's information.
     */
    public String toString() {
        return "Admin Name: " + this.name + ", Username: " + this.username;
    }
}
