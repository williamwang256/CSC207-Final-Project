package eventsfeature;

import java.time.*;
import java.util.*;

/** An entity class representing an Event.
 * @author Kevin Cecco, Daniel Chan, William Wang
 */
public class Event {

    private final int uid;
    private String name;
    private LocalDateTime startTime;
    private int duration; //in hours
    private final List<String> eventGoers;
    private String speakerNames; //usernames separated by commas
    private boolean isFull = false;
    private int capacity;
    private final Set<String> requirements;
    private boolean isVIP;

    /**
     * Constructs an instance of Event.
     * @param name The name of the Event, as a String.
     * @param startTime The starting time of the Event, as a LocalDateTime.
     * @param duration The duration of the Event, as an int.
     * @param uid The unique ID associated with such Event, as an int.
     * @param speakerNames The unique usernames of the Speakers of such Event separated by commas, as a String.
     * @param capacity The capacity of Attendees the Event can hold, as an int.
     * @param VIP The VIP status of the Event, as a boolean.
     */
    protected Event(String name, LocalDateTime startTime, int duration, int uid, String speakerNames, int capacity,
                    Set<String> requirements, boolean VIP) {
        this.uid = uid;
        this.name = name;
        this.startTime = startTime;
        this.duration = duration;
        this.eventGoers = new ArrayList<>();
        this.speakerNames = speakerNames;
        this.capacity = capacity;
        this.requirements = requirements;
        this.isVIP = VIP;
    }

    /**
     * Getter for the unique ID for an Event.
     * @return Returns the unique ID int for the Event.
     */
    protected int getUid() {
        return this.uid;
    }

    /**
     * Getter for the name of the Event.
     * @return Returns the name of the Event in the form of a String.
     */
    protected String getName() {
        return this.name;
    }

    /**
     * Getter for the start time of the Event.
     * @return Returns the starting time of the Event, as a LocalDateTime.
     */
    protected LocalDateTime getStartTime() {
        return this.startTime;
    }

    /**
     * Getter for the unique usernames of the Speakers at the Event.
     * @return Returns the unique IDs of the Speakers for such Event, as an String of ints separated by commas.
     */
    protected String getSpeakerNames() {
        return this.speakerNames;
    }

    /**
     * Getter for the duration of the Event.
     * @return Returns the duration of the Event, as a double.
     */
    protected int getDuration() {
        return this.duration;
    }

    /**
     * Getter for the capacity of the Event.
     * @return Returns the capacity of the Event, as an int.
     */
    protected int getCapacity() {
        return this.capacity;
    }

    /**
     * Getter for the list of individuals going to the Event.
     * @return Returns a list of Strings of names of individuals attending the Event.
     */
    protected List<String> getEventGoers() {
        return eventGoers;
    }

    /**
     * Getter for the list of requirements this Event has.
     * @return Returns a list of Strings of the requirements for this event.
     */
    protected Set<String> getRequirements() {
        return requirements;
    }

    /**
     * Gets a string representation of the requirements this Event has.
     * @return A string of requirements for this event.
     */
    protected String getRequirementsString() {
        StringBuilder s = new StringBuilder();
        for (String e : requirements){
            s.append(e).append(",");
        }
        if (s.length() > 0)
            s = new StringBuilder(s.substring(0, s.length() - 1));
        return s.toString();
    }

    /**
     * Gets the VIP boolean associated with this Event.
     * @return True if this is a VIP Event, False otherwise.
     */
    protected String getVIP(){
        if (isVIP)
            return "true";
        return "false";
    }

    /**
     * Setter for the name, start time, duration, speakers and capacity of the Event.
     * Note: if the new capacity is less than the number of eventGoers of this Event,
     * remove the most recent eventGoers and make the Event full.
     * @param newName Represents the new name.
     * @param newStartTime Represents the new start time.
     * @param newDuration Represents the new duration.
     * @param newSpeakers Represents the new Speaker usernames.
     * @param newCapacity Represents the new capacity of the Event.
     * @param newVIP Represents the new VIP status of the Event.
     */
    protected void updateEventData(String newName, LocalDateTime newStartTime, int newDuration, String newSpeakers,
                                   int newCapacity, boolean newVIP) {
        this.name = newName;
        this.startTime = newStartTime;
        this.duration = newDuration;
        this.speakerNames = newSpeakers;
        this.capacity = newCapacity;
        if (newCapacity < eventGoers.size()) {
            eventGoers.subList(newCapacity, eventGoers.size()).clear();
            isFull = true;
        }
        this.isVIP = newVIP; // If newVIP is true, existing non-VIP attendees aren't removed.
    }

    /**
     * Adds an individual to the list of individuals attending the Event.
     *
     * There are two criterion that must be met in order for the individual to be added to the Event. The first is that
     * they must not already be in the list of individuals attending the Event. The second is that the Event itself
     * must not be full. If the user is added to the Event, the capacity of the Event is updated accordingly.
     *
     * @param eventGoerName The name of the individual trying to attend the Event.
     */
    protected void addEventGoer(String eventGoerName) {
        if (!hasEventGoer(eventGoerName) && !isFull) {
            eventGoers.add(eventGoerName);
            if (eventGoers.size() == capacity) {
                isFull = true;
            }
        }
    }

    /**
     * Removes an individual from the list of those attending the Event.
     *
     * The user will be removed from the Event if they are within the list included. If they are not in such list,
     * nothing happens and the method will return false. The boolean for the capacity of the Event is then updated
     * accordingly.
     * @param eventGoerName The name of the individual trying to be removed from the Event.
     * @return True if the EventGoer was removed, False otherwise.
     */
    protected boolean removeEventGoer(String eventGoerName) {
         if (hasEventGoer(eventGoerName)) {
             eventGoers.remove(eventGoerName);
             if (eventGoers.size() < capacity) {
                 isFull = false;
             }
             return true;
         } else {
             return false;
         }
     }

    /**
     * Returns if a specific individual is going to attend an Event.
     * @param eventGoerName The name of the individual.
     * @return True if the individual is attending the Event, false otherwise.
     */
    protected boolean hasEventGoer(String eventGoerName) {
        return eventGoers.contains(eventGoerName);
    }

    /**
     * Returns if the Event is full.
     * @return True if the Event is full, false if it is not.
     */
    protected boolean isFull() {
        return isFull;
    }

    /**
     * Returns how many attendees are signed up for this Event.
     * @return Size of the evert goers list.
     */
    protected int numAttendees() {
        return eventGoers.size();
    }

    /**
     * Checks if a time period overlaps with this Event
     * @param time Time the time period begins at, as a LocalDateTime.
     * @param duration Duration the time period lasts for, in hours as an int.
     * @return Returns true if there is no time conflict, false otherwise.
     */
    protected boolean hasTimeConflict(LocalDateTime time, int duration) {
        if (this.startTime.isEqual(time)) {
            return true;
        } else {
            return time.isAfter(this.startTime.minusHours(duration)) &&
                    time.isBefore(this.startTime.plusHours(this.duration));
        }
    }

}