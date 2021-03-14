package eventsfeature;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** EventHandler serves as a Use Case for Events and Rooms.
 * @author Kevin Cecco, Daniel Chan, William Wang
 */

public class EventHandler {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
    private final RoomFactory roomFactory;
    private final HashMap<Integer, Event> eventList;
    private final HashMap<Integer, Room> roomList;
    private int eventIdTracker;

    /**
     * Constructs an instance of EventHandler.
     */
    public EventHandler() {
        eventList = new HashMap<>();
        roomList = new HashMap<>();
        eventIdTracker = 0;
        roomFactory = new RoomFactory();
    }

    /**
     * Gets a string representation of all the room types in the system. This is governed by the room factory.
     * @return A string representation of all the room types in the system
     */
    protected String getRoomTypes() {
        return roomFactory.getRoomTypes();
    }

    /**
     * Create Event method specifically for the file reader upon loading.
     * @param id The ID of the Event to be made, as an int.
     * @param name The name of the Event to be made, as a String.
     * @param time The start time of the Event to be made, as a LocalDateTime.
     * @param duration The duration of the Event to be made, as an int.
     * @param speakerNames The usernames of the Speaker of the Event to be made separated by commas, as an String.
     * @param capacity The capacity of the Event to be made.
     */
    protected void createEventForFile(int id, String name, String time, int duration, String speakerNames,
                                      Set<String> features, int capacity, boolean vip) {
        LocalDateTime dateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(DATE_FORMAT));
        eventIdTracker++;
        eventList.put(id, new Event(name, dateTime, duration, id, speakerNames, capacity, features, vip));
    }

    /**
     * Adds an Event to a room, as desired by the file reader.
     * @param roomNum The Room number, as an int.
     * @param eventId The Event ID, as an int.
     */
    protected void addEventsToRoom(int roomNum, int eventId) {
        roomList.get(roomNum).addEventId(eventId);
    }

    /**
     * Method for creating an Event to be included in the EventHandler schedule. Performs a series of checks to ensure
     * the event information given meets the criteria.
     * @param data The data to be used to create the event.
     * @param requirements A list of requirements for this Room.
     * @return The id of the event that was just created.
     * @throws InvalidEventFieldsException For an invalid Event field.
     */
    protected int createEvent(List<String> data, Set<String> requirements) throws InvalidEventFieldsException{
        String speakerNames, name, time;
        int duration, capacity, roomNum;
        boolean VIP;
        try {
            checkEventData(data);
            speakerNames = data.get(0);
            name = data.get(1);
            time = data.get(2);
            duration = Integer.parseInt(data.get(3));
            capacity = Integer.parseInt(data.get(4));
            VIP = Boolean.parseBoolean(data.get(5));
            roomNum = Integer.parseInt(data.get(6));
            LocalDateTime dateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(DATE_FORMAT));
            if (!roomList.containsKey(roomNum))
                throw new RoomNotFoundException();
            for (int eventIds : roomList.get(roomNum).getEventIds()) {
                if (eventList.get(eventIds).hasTimeConflict(dateTime, duration))
                    throw new InvalidEventFieldsException("room unavailable");
            }
            if (!roomList.get(roomNum).checkRequirements(requirements, capacity))
                throw new InvalidEventFieldsException("this room is incompatible with the event");
            int id = eventIdTracker;
            eventIdTracker++;
            eventList.put(id, new Event(name, dateTime, duration, id, speakerNames, capacity, requirements, VIP));
            roomList.get(roomNum).addEventId(id);
            return id;
        } catch (NumberFormatException e) {
            throw new InvalidEventFieldsException("must be a number");
        } catch (java.time.format.DateTimeParseException e) {
            throw new InvalidEventFieldsException("invalid date format");
        }
    }

    /**
     * Creates a room in the system.
     * @param roomNum  The room number of the new room.
     * @param capacity The capacity of the new room.
     * @param roomType The type of thw new room.
     * @throws InvalidEventFieldsException If the room already exists or has a capacity below 1.
     */
    protected void createRoom(int roomNum, int capacity, String roomType) throws InvalidEventFieldsException {
        if (roomList.containsKey(roomNum))
            throw new InvalidEventFieldsException("room already exists");
        if (capacity <= 0)
            throw new InvalidEventFieldsException("minimum capacity is 1");
        roomList.put(roomNum, roomFactory.getNewRoom(roomType, roomNum, capacity));
    }

    /**
     * Removes an Event from the event list based hash map.
     * @param eventId The unique ID of the Event to be removed, as an int.
     * @throws EventNotFoundException If the event does not exist in the schedule.
     */
    protected void removeEvent(int eventId) throws EventNotFoundException {
        if (!eventList.containsKey(eventId))
            throw new EventNotFoundException();
        eventList.remove(eventId);
        for (Room room : roomList.values()) {
            for (int id : room.getEventIds()) {
                if (id == eventId) {
                    room.removeEventId(eventId);
                    break;
                }
            }
        }
    }

    /**
     * Adds an attendee to an Event in the EventHandler schedule, if any.
     * @param attendeeName The name of the attendee to be added.
     * @param eventId The unique ID for the Event in question.
     * @throws EventNotFoundException If the event does not exist in the schedule.
     * @throws InvalidEventFieldsException If the Event is full or the user is already signed up.
     */
    protected void addAttendeeToEvent(String attendeeName, int eventId) throws EventNotFoundException,
            InvalidEventFieldsException {
        if (!eventList.containsKey(eventId))
            throw new EventNotFoundException();
        Event e = eventList.get(eventId);
        if (e.isFull())
            throw new InvalidEventFieldsException("this event is already full");
        if (e.hasEventGoer(attendeeName))
            throw new InvalidEventFieldsException("you are already signed up for this event");
        e.addEventGoer(attendeeName);
    }

    /**
     * Removes an attendee from an Event in EventHandler schedule, if any.
     * @param attendeeName The name of the attendee to be removed.
     * @param eventId The unique ID for the Event in question.
     * @throws EventNotFoundException If the event does not exist in the schedule.
     * @throws InvalidEventFieldsException If the user is not in this event.
     */
    protected void removeAttendeeFromEvent(String attendeeName, int eventId) throws EventNotFoundException,
            InvalidEventFieldsException {
        if (eventList.containsKey(eventId)) {
            Event e = eventList.get(eventId);
            if (!e.removeEventGoer(attendeeName)) {
                throw new InvalidEventFieldsException("user is not in this event");
            }
        } else {
            throw new EventNotFoundException();
        }
    }

    /**
     * Returns the usernames of the speakers speaking at the given event.
     * @param eventid The event in question.
     * @return The usernames of the speakers speaking at the event separated by commas, as a String.
     * @throws EventNotFoundException If the event does not exist.
     */
    protected String getSpeakerNames(int eventid) throws EventNotFoundException {
        try {
            return eventList.get(eventid).getSpeakerNames();
        } catch (NullPointerException e) {
            throw new EventNotFoundException();
        }
    }

    /**
     * Getter for the names of attendees for an Event within the EventHandler schedule.
     * @param eventId The unique ID of the Event in question.
     * @return Returns the list of attendees for the Event in Schedule or an empty list if no such Event exists.
     * @throws EventNotFoundException If the event does not exist in the schedule.
     */
    public List<String> getAttendeeNames(int eventId) throws EventNotFoundException {
        if (eventList.containsKey(eventId))
            return eventList.get(eventId).getEventGoers();
        else
            throw new EventNotFoundException();
    }

    /**
     * Getter for all the Event ids within the EventHandler schedule of a certain criteria.
     * @param criteria Criteria of Events to look for (ie. "user" if searching Events with a certain user)
     * @param details Details of criteria (id. if you would like to search by user, this would be the username)
     * @return Event ids that follow the criteria, as a List of ints.
     * @throws InvalidEventFieldsException If a detail isn't valid to its criteria.
     * @throws EventNotFoundException If the Event doesn't exist.
     */
    protected List<Integer> getSpecificEventIds(String criteria, String details) throws InvalidEventFieldsException,
            EventNotFoundException{
        ArrayList<Integer> ids = new ArrayList<>();
        switch (criteria) {
            case "all":
                // criteria: "all", details don't matter
                for (Event e : eventList.values())
                    ids.add(e.getUid());
                break;
            case "user":
                // criteria: "user", details: username of user
                for (Event e : eventList.values())
                    if (e.getEventGoers().contains(details)) {
                        ids.add(e.getUid());
                    }
                break;
            case "speakers":
                // criteria: "speakers", details: speaker usernames separated by commas, no spaces, as a String
                List<String> speakers;
                speakers = Arrays.asList(details.split(","));
                for (Event e : eventList.values()) {
                    for (String speaker : speakers) {
                        if (e.getSpeakerNames().contains(speaker)) {
                            ids.add(e.getUid());
                        }
                    }
                }
                break;
            case "day":
                // criteria: "day", details: date formatted as dd/MM/yyyy, as a String
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate d = LocalDate.parse(details, formatter);
                    for (Event e : eventList.values()) {
                        LocalDateTime eventDateTime = eventList.get(e.getUid()).getStartTime();
                        if (eventDateTime.getYear() == d.getYear() &&
                                eventDateTime.getMonth() == d.getMonth() &&
                                eventDateTime.getDayOfYear() == d.getDayOfYear()) {
                            ids.add(e.getUid());
                        }
                    }
                } catch (DateTimeException e) {
                    throw new InvalidEventFieldsException("day not in format (dd/MM/yyyy)");
                }
                break;
            case "eventRange":
                // criteria: "eventRange", details: starting event id and ending event id, separated by a comma, no spaces as a String
                try {
                    List<String> range = Arrays.asList(details.split(","));
                    int lower = Integer.parseInt(range.get(0));
                    int upper = Integer.parseInt(range.get(1));
                    if (!eventList.containsKey(lower) || !eventList.containsKey(upper)) {
                        throw new EventNotFoundException();
                    }
                    ids.add(lower);
                    ids.add(upper);
                    for (Event e : eventList.values()) {
                        if (eventList.get(e.getUid()).getStartTime().isBefore(eventList.get(upper).getStartTime()) &&
                                eventList.get(e.getUid()).getStartTime().isAfter(eventList.get(lower).getStartTime())) {
                            ids.add(e.getUid());
                        }
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    throw new InvalidEventFieldsException("event range not in format (starting id,ending id)");
                }
                break;
        }
        return ids;
    }

    /**
     * Gets the room code (room type abbreviation + room number) of a given event.
     * @param eventId The event we want the room number of.
     * @return The room code of the given event.
     * @throws EventNotFoundException If the event does not exist.
     */
    private String getRoomCodeOfEvent(int eventId) throws EventNotFoundException {
        for (Room r : roomList.values()) {
            if (r.hasEventId(eventId)) {
                return r.getRoomCode();
            }
        }
        throw new EventNotFoundException();
    }

    /**
     * Add up all the available features from all the rooms, and return a set of everything.
     * @return A set of all available features in the system.
     */
    protected Set<String> getAvailableFeatures() {
        Set<String> features = new HashSet<>();
        for (Room r : roomList.values()) {
            features.addAll(r.getFeatures());
        }
        return features;
    }

    /**
     * Gets the information associated with a specific Event within the EventHandler schedule.
     * @param eventId The unique ID for the Event in question.
     * @return Returns a map of Strings which represent the information associated with an Event in schedule.
     * @throws EventNotFoundException If the event does not exist in the schedule.
     */
    protected Map<String, String> getEventInfo(int eventId) throws EventNotFoundException {
        HashMap<String, String> info = new HashMap<>();
        if (!eventList.containsKey(eventId))
            throw new EventNotFoundException();
        info.put("name", eventList.get(eventId).getName());
        info.put("time", eventList.get(eventId).getStartTime().toString());
        info.put("roomNum", getRoomCodeOfEvent(eventId));
        info.put("speakers", eventList.get(eventId).getSpeakerNames());
        info.put("numAttendees", Integer.toString(eventList.get(eventId).numAttendees()));
        info.put("isFull", Boolean.toString(eventList.get(eventId).isFull()));
        info.put("eventId", Integer.toString(eventList.get(eventId).getUid()));
        info.put("requirements", eventList.get(eventId).getRequirements().toString());
        info.put("isVIP", eventList.get(eventId).getVIP());
        return info;
    }

    /**
     * Gets rooms that have certain requirements and is greater or equal that a certain capacity.
     * @param requirements Requirements, as a Set of Strings.
     * @param capacity Capacity, as an int.
     * @return A set of integers representing the recommended rooms for a given event.
     */
    protected Set<Integer> getSuggestedRoomNumbers(Set<String> requirements, int capacity) {
        Set<Integer> rooms = new HashSet<>(roomList.keySet());
        for (int r : roomList.keySet()) {
            if (!roomList.get(r).checkRequirements(requirements, capacity)) {
                rooms.remove(r);
            }
        }
        return rooms;
    }

    /**
     * Updates an Event given an event id and a list of data.
     * @param eventId Event id of the Event to update.
     * @param updatedData New data for the Event, as a List of Strings.
     * @throws EventNotFoundException If the event does not exist.
     * @throws InvalidEventFieldsException If the event fields entered are invalid.
     */
    protected void updateEventData(int eventId, List<String> updatedData) throws EventNotFoundException, InvalidEventFieldsException {
        if (!eventList.containsKey(eventId))
            throw new EventNotFoundException();
        checkEventData(updatedData);
        String speakerNames = updatedData.get(0);
        String name = updatedData.get(1);
        String time = updatedData.get(2);
        int duration = Integer.parseInt(updatedData.get(3));
        int capacity = Integer.parseInt(updatedData.get(4));
        boolean vip = Boolean.parseBoolean(updatedData.get(5));
        LocalDateTime dateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(DATE_FORMAT));
        eventList.get(eventId).updateEventData(name, dateTime, duration, speakerNames, capacity, vip);
    }

    /**
     * Check that each piece of the Event data is a valid and logical parameter.
     * @param data Each part of an event's information, as a List of Strings.
     * @throws InvalidEventFieldsException If a piece of data is invalid.
     */
    private void checkEventData(List<String> data) throws InvalidEventFieldsException{
        try {
            String speakerNames = data.get(0);
            String name = data.get(1);
            String time = data.get(2);
            int duration = Integer.parseInt(data.get(3));
            int capacity = Integer.parseInt(data.get(4));
            LocalDateTime dateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(DATE_FORMAT));
            Pattern pattern = Pattern.compile("[\\w|\\s_.]*");
            Matcher matcher = pattern.matcher(name);
            if (!matcher.matches())
                throw new InvalidEventFieldsException("name cannot contain special characters");
            if (dateTime.isBefore(LocalDateTime.now()))
                throw new InvalidEventFieldsException("event cannot start in the past");
            if (duration <= 0 || capacity <= 0)
                throw new InvalidEventFieldsException("zeros and negatives not allowed");
            for (String speakerName : speakerNames.split(",")) {
                for (Event event : eventList.values()) {
                    if (event.getSpeakerNames().equals(speakerName)) {
                        if (event.hasTimeConflict(dateTime, duration)) {
                            throw new InvalidEventFieldsException("speaker " + speakerName + " unavailable");
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            throw new InvalidEventFieldsException("must be a number");
        } catch (java.time.format.DateTimeParseException e) {
            throw new InvalidEventFieldsException("invalid date format");
        }
    }

    /**
     * Takes the list of attendees for an Event and creates a string of all of their usernames used for output file.
     * @param eventId The unique ID of this Event.
     * @return Returns a string of all the attendees.
     * @throws EventNotFoundException If the Event doesn't exist.
     */
    protected String attendeeNamesToString(int eventId) throws EventNotFoundException{
        StringBuilder s = new StringBuilder();
        if (eventList.containsKey(eventId)) {
            for (String person : eventList.get(eventId).getEventGoers())
                s.append(person).append(",");
            if (!eventList.get(eventId).getEventGoers().isEmpty())
                s = new StringBuilder(s.substring(0, s.length() - 1));
            return s.toString();
        } else
            throw new EventNotFoundException();
    }

    /**
     * Encapsulates all Room information into a String
     * @return Returns the information of all Rooms in this Event Handler's roomList, as one String.
     */
    public String roomWriter() {
        StringBuilder output = new StringBuilder();
        for (Map.Entry<Integer, Room> data : roomList.entrySet()) {
            output.append(data.getKey()).append("%%").append(data.getValue().getEventidsString()).
                    append("%%").append(data.getValue().getCapacity()).append("%%").append(data.getValue().
                    getRoomCode()).append("%%").append("\n");
        }
        return output.toString();
    }

    /**
     * Encapsulates all Event information into a String.
     * @return Returns the information of all Events in this Event Handler's eventList, as one String.
     */
    public String eventWriter() {
        StringBuilder output = new StringBuilder();
        for (Map.Entry<Integer, Event> data : eventList.entrySet()) {
            output.append(data.getKey()).append("%%").append(data.getValue().getName()).append("%%").
                    append(data.getValue().getStartTime()).append("%%").append(data.getValue().getDuration()).
                    append("%%").append(attendeeNamesToString(data.getKey())).append("%%").
                    append(data.getValue().getSpeakerNames()).append("%%").append(data.getValue().
                    getRequirementsString()).append("%%").append(data.getValue().getCapacity()).
                    append("%%").append(data.getValue().getVIP()).append("%%").append("\n");
        }
        return output.toString();
    }
}