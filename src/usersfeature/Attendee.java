package usersfeature;
import java.util.*;

/** Represents an Attendee.
 * @author Manav Shah, Muhammad Sohaib Saqib
 */
public class Attendee extends User {

    private HashSet<Integer> attending;
    private boolean isVIP = false;

    /**
     * Constructs an instance of Attendee.
     * @param name A string representing a name.
     * @param username A string representing a username.
     */
    public Attendee(String name, String username) {
        super(name, username);
        this.attending = new HashSet<>();
    }

    /**
     * Constructs an instance of attendee.
     * @param data A string representing the stored data.
     */
    public Attendee(String data){
        super(data);
        this.attending = new HashSet<>();
        this.attendeeReader(data);
    }

    /**
     * Encodes the data of the Attendee.
     * @return A String representing the stored data of the Attendee.
     */
    public String objectEncoder() {
        StringBuilder file = new StringBuilder(super.objectEncoder());
        String attending;
        if (!this.attending.isEmpty()) {
            StringBuilder f = new StringBuilder();
            for (Integer t : this.attending) {
                String s = "##" + t;
                f.append(s);
            }
            attending = f.substring(2);
        } else {
            attending = "";
        }
        file.append("-").append(attending);
        file.append("-").append(isVIP?"true":"false");
        return file.toString();
    }

    /**
     * Decodes the data of the Attendee.
     * @param data A string representing the stored data.
     */
    public void attendeeReader(String data) {
        if (data != null && data.length() > 0) {
            ArrayList<String> lists = new ArrayList<>(Arrays.asList(data.split("-")));

            ArrayList<String> attendingEvents = new ArrayList<>(Arrays.asList(lists.get(5).split("##")));
            if (!attendingEvents.isEmpty() && !attendingEvents.get(0).equals("")) {
                for (String event: attendingEvents){
                    this.attending.add(Integer.parseInt(event));
                }
            }
            else{
                this.attending = new HashSet<>();
            }
            this.isVIP = lists.get(6).equalsIgnoreCase("true");
        }
    }

    /**
     * Represents the name and username of the Attendee as a String.
     * @return A String representing the Attendee's information.
     */
    public String toString(){
        return "Attendee Name: " + this.name + ", Username: " + this.username;
    }

    /**
     * Converts the status of the Attendee to VIP.
     * @return Boolean true iff instance variable isVIP has been successfully set to true.
     */
    public boolean makeVIP(){
        if (!isVIP) {
            this.isVIP = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Reverts the status of the Attendee from VIP.
     * @return Boolean true iff instance variable isVIP has been successfully set to false.
     */
    public boolean removeVIP(){
        if (isVIP) {
            this.isVIP = false;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifies whether the Attendee has VIP status.
     * @return Boolean true iff instance variable isVIP is true.
     */
    public boolean isVIP(){
        return this.isVIP;
    }

    /**
     * Adds an event to the attending HashSet.
     * @param event An int representing the unique ID for an Event.
     * @return Boolean true iff event is successfully added to the attending HashSet.
     */
    public boolean addEvent(int event){
        if (this.attending.contains(event)){
            return false;
        }
        this.attending.add(event);
        return true;
    }

    /**
     * Removes an event to the attending HashSet.
     * @param event An int representing the unique ID for an Event.
     * @return Boolean true iff event is successfully removed from the attending HashSet.
     */
    public boolean removeEvent(int event){
        if (!this.attending.contains(event)){
            return false;
        }
        this.attending.remove(event);
        return true;
    }

}
