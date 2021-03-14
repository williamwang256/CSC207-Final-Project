package usersfeature;

import java.util.*;

/** Represents an Organizer.
 * @author Manav Shah, Muhammad Sohaib Saqib
 */
public class Organizer extends Attendee {

    private HashSet<Integer> createdEvents;
    private HashSet<String> speakersCreated;

    /**
     * Constructs an instance of Organizer.
     * @param name A string representing a name.
     * @param username A string representing a name.
     */
    public Organizer(String name, String username) {
        super(name, username);
        this.createdEvents = new HashSet<>();
        this.speakersCreated = new HashSet<>();
    }

    /**
     * Constructs an instance of Organizer.
     * @param data A string representing the stored data.
     */
    public Organizer(String data){
        super(data);
        this.createdEvents = new HashSet<>();
        this.speakersCreated = new HashSet<>();
        this.organizerReader(data);
    }

    /**
     * Encodes the data of the Organizer.
     * @return A String representing the stored data of the Organizer.
     */
    public String objectEncoder(){
        StringBuilder file = new StringBuilder(super.objectEncoder());
        String createdEvents;
        if (!this.createdEvents.isEmpty()){
            StringBuilder f = new StringBuilder();
            for (int event: this.createdEvents){
                String s = "##" + event;
                f.append(s);
            }
            createdEvents = f.substring(2);
        } else {
            createdEvents = "";
        }
        file.append("-").append(createdEvents);
        String speakersCreated;
        if (!this.speakersCreated.isEmpty()) {
            StringBuilder h = new StringBuilder();
            for (String name : this.speakersCreated) {
                String s = "," + name;
                h.append(s);
            }
            speakersCreated = h.substring(1);
        } else {
            speakersCreated = "";
        }
        file.append("-").append(speakersCreated);
        return file.toString();
    }

    /**
     * Decodes the data of the Organizer.
     * @param data A string representing the stored data.
     */
    public void organizerReader(String data) {
        int startingIndex = 7;
        if (data != null && data.length() > 0) {
            ArrayList<String> lists = new ArrayList<>(Arrays.asList(data.split("-")));
            ArrayList<String> events = new ArrayList<>(Arrays.asList(lists.get(startingIndex).split("##")));
            if (!events.isEmpty() && !events.get(0).equals("")) {
                for (String event: events){
                    this.createdEvents.add(Integer.parseInt(event));
                }
            } else {
                this.createdEvents = new HashSet<>();
            }
            ArrayList<String> speakers = new ArrayList<>(Arrays.asList(lists.get(startingIndex + 1).split(",")));
            if(!speakers.isEmpty() && !speakers.get(0).equals("")) {
                this.speakersCreated = new HashSet<>(speakers);
            }else{
                this.speakersCreated = new HashSet<>();
            }
        }
    }

    /**
     * Represents the name and username of the Organizer as a String.
     * @return A String representing the Organizer's information.
     */
    public String toString() {
        return "Organizer Name: " + this.name + ", Username: " + this.username;
    }

    /**
     * Gets a list of events that the Organizer has created.
     * @return An ArrayList representing  unique event IDs as ints.
     */
    public ArrayList<Integer> getCreatedEvents() {
        return new ArrayList<>(this.createdEvents);
    }


    /**
     * Adds an event created by the Organizer to the created HashSet.
     * @param event An int representing the unique ID for an Event.
     * @return Boolean true iff event is successfully added to the createdEvents HashSet.
     */
    public boolean createEvent(int event) {
        // how to 'request' event class to create event?
        if (this.createdEvents.contains(event)) {
            return false;
        } else {
            this.createdEvents.add(event);
            return true;
        }
    }

    /**
     * Removes an event created by the Organizer from the created HashSet.
     * @param event An int representing the unique ID for an Event.
     * @return Boolean true iff event is successfully removed from the createdEvents HashSet.
     */
    public boolean deleteEvent(int event) {
        if (this.createdEvents.contains(event)) {
            this.createdEvents.remove(event);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds a Speaker to the speakersCreated HashSet.
     * @param username A String representing the username of the Speaker.
     * @return Boolean true iff the Speaker is successfully added to the speakersCreated HashSet.
     */
    public boolean addSpeaker(String username) {
        if (this.speakersCreated.contains(username)) {
            return false;
        } else {
            this.speakersCreated.add(username);
            return true;
        }
    }

    /**
     * Deletes a Speaker from the speakersCreated HashSet.
     * @param username A String representing the username of the Speaker.
     * @throws UserNotFoundException thrown when the speaker wasn't found.
     */
    public void deleteSpeaker(String username) throws UserNotFoundException{
        if (this.speakersCreated.contains(username)) {
            this.speakersCreated.remove(username);
        } else{
            throw new UserNotFoundException();
        }
    }

}
