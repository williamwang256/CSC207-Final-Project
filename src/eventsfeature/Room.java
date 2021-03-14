package eventsfeature;

import java.util.*;

/** An entity class representing a Room (abstract).
 * @author Kevin Cecco, Daniel Chan, William Wang
 */
public abstract class Room {

    private final int roomNum;
    private final int capacity;
    private final Set<Integer> eventIds;
    private final Set<String> features;

    /**
     * Constructs an instance of Room.
     * @param roomNum  The unique room number associated with this Room, as an int.
     * @param capacity The capacity of this Room, as an int.
     * @param features The features of the room, as a Set.
     */
    protected Room(int roomNum, int capacity, Set<String> features) {
        this.roomNum = roomNum;
        this.capacity = capacity;
        this.eventIds = new HashSet<>(); // list of event ids as ints
        this.features = features; // list of features. this is determined by subclasses
    }

    /**
     * Gets the "code" of the room. The code is composed of two letters which represent the room type, followed by one
     * or more digits, which represents the room number.
     * Since different types of rooms have different codes, this method is made abstract.
     * @return The code of the room, as a string.
     */
    protected abstract String getRoomCode();

    /**
     * A method to check if the given requirements are possible in this room. Since all rooms are different,
     * this method is made abstract. All non-abstract subclasses of room should implement this method.
     * @param requirements Given requirements.
     * @return Whether or not the requirements are present.
     */
    protected abstract boolean checkRequirements(Set<String> requirements, int capacity);

    /**
     * Gets the capacity of the room.
     * @return The capacity, as an int.
     */
    protected int getCapacity() {
        return capacity;
    }

    /**
     * Gets the room number of this Room.
     * @return The room number of this room, as an int.
     */
    protected int getRoomNum() {
        return roomNum;
    }

    /**
     * Gets the event ids of this Room.
     * @return The event ids of this rom, as a set.
     */
    protected Set<Integer> getEventIds() {
        return eventIds;
    }

    /**
     * Gets the room features for this Room.
     * @return The features specific to the room, as a set.
     */
    protected Set<String> getFeatures() {
        return features;
    }

    /**
     * Adds an Event to this Room.
     * @param eventId Event id of the Event added to this Room.
     */
    protected void addEventId(int eventId) {
        if (eventIds.size() < capacity) {
            eventIds.add(eventId);
        }
        else {
            throw new InvalidEventFieldsException("room " + roomNum + "is full");
        }
    }

    /**
     * Removes an Event from this Room.
     * @param eventId Event id of the Event removed from this Room.
     */
    protected void removeEventId(int eventId) {
        eventIds.remove(eventId);
    }

    /**
     * Checks if this Room contains a certain Event.
     * @param eventId Event id of the Event checked
     * @return True if this Room has the Event, false otherwise.
     */
    protected boolean hasEventId(int eventId) {
        return eventIds.contains(eventId);
    }

    /**
     * Gets the event Ids of the Room, used specifically for file writing.
     * @return A chain of event Ids separated by commas.
     */
    protected String getEventidsString(){
        StringBuilder s = new StringBuilder();
        for (Integer i : eventIds){
            s.append(i.toString()).append(",");
        }
        if (s.length()>= 1)
            s = new StringBuilder(s.substring(0, s.length() - 1));
        return s.toString();
    }
}
