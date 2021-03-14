package eventsfeature;

import usersfeature.*;
import java.util.*;

/** Represents a controller specific to the events code. Takes in input from the user, and calls event use case methods.
 * @author Kevin Cecco, Daniel Chan, William Wang
 */
public class EventSystem {

    private final EventHandler eventHandler;
    private final UserHandler userHandler;

    /**
     * Constructs and initializes an instance of EventSystem.
     * @param eventData The data for events to initialize with, as a string.
     * @param roomData The data for rooms to initialize with, as a string.
     * @param userHandler The user handler in the system.
     * @param eventHandler The event handler in the system.
     */
    public EventSystem(String eventData, String roomData, UserHandler userHandler, EventHandler eventHandler) {
        this.eventHandler = eventHandler;
        this.userHandler = userHandler;
        this.roomReader(roomData);
        this.eventReader(eventData);
    }

    /**
     * Gets all available features in the system. This is determined by the types of rooms that exist.
     * @return A string representing of all available features in the system.
     */
    public String getAvailableFeatures() {
        return eventHandler.getAvailableFeatures().toString();
    }

    /**
     * Gets all available room types in the system.
     * @return A string representing all available room types in the system.
     */
    public String getRoomTypes() {
        return eventHandler.getRoomTypes();
    }

    /**
     * Initializes and creates the rooms that were saved in the system.
     * @param input A string representing data about rooms.
     */
    protected void roomReader(String input){
       if (input!= null && input.length() > 0){
           String[] inputSplit = input.split("\\r?\\n");
           for (String rooms : inputSplit){
               String [] info = rooms.split("%%");
               int roomNum = Integer.parseInt(info[0]);
               String [] eventIds = info[1].split(",");
               int capacity = Integer.parseInt(info[2]);
               String room = info[3].substring(0,2);
               String roomType;
               if (room.equals("CC")){
                   roomType = "convention";
               } else if (room.equals("CL")){
                   roomType = "classroom";
               } else {
                   roomType = "auditorium";
               }
               try{
                   addRoom(roomNum, capacity, roomType);
                   for (String e : eventIds){
                       eventHandler.addEventsToRoom(roomNum, Integer.parseInt(e));
                   }
               } catch (InvalidEventFieldsException | NumberFormatException ignored){}
           }
       }
    }

    /**
     * Method calls the Persistence class to read from the file data associated with Events and Rooms.
     * @param input The string of data from the file.
     */
    protected void eventReader(String input){
        if (input != null && input.length()>0) {
            String[] inputSplit = input.split("\\r?\\n");
            for (String events : inputSplit) {
                String [] specs = events.split("%%");
                int eventId = Integer.parseInt(specs[0]);
                String name = specs[1];
                String dateTime = specs[2].replace("T", " ");
                int duration = Integer.parseInt(specs[3]);
                String [] attendees = specs[4].split(",");
                String speakers = specs[5];
                String [] features = specs[6].split(",");
                int capacity = Integer.parseInt(specs[7]);
                boolean vip = Boolean.parseBoolean(specs[8]);
                Set<String> feat = new HashSet<>();
                Collections.addAll(feat, features);
                try {
                    eventHandler.createEventForFile(eventId , name, dateTime, duration,
                            speakers, feat, capacity, vip);
                    for (String person : attendees) {
                        if (!person.equals("")) {
                            attendEvent(Integer.parseInt(specs[0]), person);
                        }
                    }
                } catch (RoomNotFoundException | EventNotFoundException | InvalidEventFieldsException ignored) {}
            }
        }
    }

    /**
     * Adds a room to the handler.
     * @param roomNum The number of the room to add.
     * @param capacity The capacity of the room to add.
     * @param type The room type.
     * @throws InvalidEventFieldsException If the room we are trying to add already exists.
     */
    public void addRoom(int roomNum, int capacity, String type) throws InvalidEventFieldsException {
        eventHandler.createRoom(roomNum, capacity, type);
    }

    /**
     * Adds the current user to the event given by the eventId.
     * @param eventId The id of the event to attend.
     * @param username The username of the user that wants to attend.
     * @throws EventNotFoundException If the selected event does not exist.
     * @throws InvalidEventFieldsException If the user is already signed up.
     * @throws UserNotFoundException If the user is not found.
     */
    public boolean attendEvent(int eventId, String username) throws
            EventNotFoundException, InvalidEventFieldsException, UserNotFoundException {
        if (eventHandler.getEventInfo(eventId).get("isVIP").equals("true")
                && userHandler.getUserType(username).equals("attendee")) {
            return false;
        } else {
            eventHandler.addAttendeeToEvent(username, eventId);
            String[] speakers = eventHandler.getSpeakerNames(eventId).split(",");
            for (String speakerUserName : speakers)
                if (!"".equals(speakerUserName)) {
                    userHandler.addFriend(speakerUserName, username);
                }
            userHandler.addEventToUserList((eventId), username);
            return true;
        }
    }

    /**
     * Gets information from user about their username, roomNum, and eventId of event they want to leave
     * @param eventId The id of the event to leave.
     * @param username The username of the user that wants to attend.
     * @throws EventNotFoundException If the selected event does not exist.
     * @throws InvalidEventFieldsException If the event field(s) are incorrect.
     */
    public void leaveEvent(int eventId, String username) throws EventNotFoundException, InvalidEventFieldsException{
        eventHandler.removeAttendeeFromEvent(username, eventId);
        userHandler.removeEventFromUserList(eventId, username);
    }

    /**
     * Splits up the user-entered info, and creates an event with the given information.
     * @param speakerNames The names of the Speakers for the event, as a string.
     * @param createdByUser The user who created the event.
     * @param data A list of strings the user inputted.
     * @param requirements The requirements for the event.
     * @return The event id of the event that was just created.
     * @throws RoomNotFoundException If the room requested does not exist.
     * @throws InvalidEventFieldsException If it was given invalid input.
     * @throws UserNotFoundException If the user is not found.
     */
    public int createEvent(String speakerNames, String createdByUser, List<String> data, Set<String> requirements)
            throws RoomNotFoundException, InvalidEventFieldsException, UserNotFoundException {
        String [] speakers = speakerNames.split(",");
        for (String speaker : speakers)
            if (!"".equals(speaker)) {
                userHandler.getSpeaker(speaker);
            }
        int createdEvent = eventHandler.createEvent(data, requirements);
        for (String speaker : speakers)
            if (!"".equals(speaker)) {
                userHandler.createEvent(createdByUser, createdEvent, speaker);
            }
        return createdEvent;
    }

    /**
     * Removes the event based on given roomNum and eventId.
     * @param doneByUser User who removed the event.
     * @param eventId The id of the event removed.
     * @throws EventNotFoundException If the selected event does not exist.
     * @throws UserNotFoundException If the user does not exist.
     */
    public void removeEvent(String doneByUser, int eventId) throws EventNotFoundException, UserNotFoundException {
        String[] speakers = eventHandler.getSpeakerNames(eventId).split(",");
        eventHandler.removeEvent(eventId);
        for (String speaker : speakers) {
            if (!"".equals(speaker)) {
                userHandler.deleteEvent(doneByUser, eventId, speaker);
            }
        }
    }

    /**
     * Updates an Event given the Event id and a list of data.
     * @param eventId Event id of the Event to update.
     * @param updatedData New Data for the Event, as a List of Strings.
     * @throws EventNotFoundException If the event is not found.
     * @throws InvalidEventFieldsException If the event field(s) are incorrect.
     */
    public void updateEventData(String doneByUser, int eventId, List<String> updatedData) throws EventNotFoundException,
            InvalidEventFieldsException {
        String [] speakers = updatedData.get(0).split(",");
        for (String speaker : speakers) {
            if (!"".equals(speaker)) {
                userHandler.getSpeaker(speaker);
            }
        }
        eventHandler.updateEventData(eventId, updatedData);
        for (String speaker : speakers) {
            if (!"".equals(speaker)) {
                userHandler.createEvent(doneByUser, eventId, speaker);
            }
        }
    }

    /**
     * Gets a list of usernames signed up for an event.
     * @param eventId The event id.
     * @return A list of usernames of attendees to the event.
     * @throws EventNotFoundException If the given event does not exist.
     */
    public List<String> getAllAttendeeUsernames(int eventId) throws EventNotFoundException {
        return eventHandler.getAttendeeNames(eventId);
    }

    /**
     * Gets a list of suggested rooms, based on a set of requirements and a capacity.
     * @param requirements A set of requirements.
     * @param capacity A integer representing the capacity that we need.
     * @return A set of suggested rooms.
     */
    public Set<Integer> getSuggestedRoomNumbers(Set<String> requirements, int capacity) {
        return eventHandler.getSuggestedRoomNumbers(requirements, capacity);
    }

    /**
     * Getter for data of a singular event.
     * @param eventId The id of the event to get data for.
     * @return A map of the data; where the key is the field, the value is the data itself.
     * @throws EventNotFoundException If the event is not found.
     */
    public Map<String, String> getEventInfo(int eventId) throws EventNotFoundException {
        return eventHandler.getEventInfo(eventId);
    }

    /**
     * Getter for data from Events of a certain criteria.
     * @param criteria Criteria of Events to look for (ie. "user" if searching Events with a certain user)
     * @param details Details of criteria (id. if you would like to search by user, this would be the username)
     * @return Event data of Events that follow the criteria, as a List of Maps, with their keys and values as Strings.
     * @throws InvalidEventFieldsException If the event field is invalid.
     * @throws EventNotFoundException If the event is not found.
     */
    public List<Map<String, String>> getSpecificEventData(String criteria, String details) throws
            InvalidEventFieldsException, EventNotFoundException{
        ArrayList<Map<String, String>> data = new ArrayList<>();
        for (int id : eventHandler.getSpecificEventIds(criteria, details)) {
            data.add(eventHandler.getEventInfo(id));
        }
        return data;
    }

    /**
     * Getter for a string of event names, given a list of event ids.
     * @param createdEvents A list of event ids, as integers.
     * @return A string with all the names of events.
     * @throws EventNotFoundException If the event is not found.
     */
    public List<String> getAllEventNameList(List<Integer> createdEvents) throws EventNotFoundException{
        List<String> out = new ArrayList<>();
        for (int id : createdEvents){
            out.add(eventHandler.getEventInfo(id).get("name"));
        }
        return out;
    }

    /**
     * Gets the current user's name and type from the user handler.
     * @return A list of strings, the first one being the username and the second the user type.
     */
    public List<String> getUserNameAndType() {
        return new ArrayList<>(Arrays.asList(userHandler.getCurrentUsername(), userHandler.getCurrentUserType()));
    }

}
