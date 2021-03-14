package menus;

import eventsfeature.EventNotFoundException;
import eventsfeature.EventSystem;
import eventsfeature.InvalidEventFieldsException;
import eventsfeature.RoomNotFoundException;
import usersfeature.UserNotFoundException;
import mainsystem.SystemBuilder;

import java.util.*;

/** Represents a text interface for interacting with Events.
 * @author Kevin Cecco, Daniel Chan, William Wang
 */
public class EventMenuPage extends MenuPresenter {

    private final EventSystem es;

    /**
     * Constructor for the EventMenuPage.
     * @param sb The string builder.
     */
    public EventMenuPage(SystemBuilder sb) {
        this.es = sb.getEventSystem();
    }

    /**
     * Runs the EventMenuPage.
     * @return A string representing the next menu to be run.
     */
    @Override
    public String run() {
        try {
            String userName = es.getUserNameAndType().get(0);
            String userType = es.getUserNameAndType().get(1);
            ArrayList<String> options;
            if (userType.equals("attendee") || (userType.equals("VIP")))
                options = new ArrayList<>(Arrays.asList(
                        "view",
                        "main menu",
                        "quit",
                        "sign up",
                        "leave event"));
            else if (userType.equals("organizer") || (userType.equals("admin")))
                options = new ArrayList<>(Arrays.asList(
                        "view",
                        "main menu",
                        "quit",
                        "sign up",
                        "leave event",
                        "create room",
                        "create event",
                        "cancel event",
                        "update event"));
            else
                options = new ArrayList<>(Arrays.asList(
                        "view",
                        "main menu",
                        "quit"));

            switch (getMenuOption("This is the " + userType + " events menu.", options)) {
                case "1":
                    return "EventViewPage";
                case "2":
                    return "MainMenuPage";
                case "3":
                    return "QuitPage";
                case "4":
                    int joinId = getEventFromUser();
                    boolean success = es.attendEvent(joinId, userName);
                    if (success)
                        printSuccess("joined event id #" + joinId);
                    else
                        printError("this event is VIP only");
                    return "EventMenuPage";
                case "5":
                    int leaveId = getEventFromUser();
                    es.leaveEvent(leaveId, userName);
                    printSuccess("left event id #" + leaveId);
                    return "EventMenuPage";
                case "6":
                    Map<String, Integer> roomInfo = getRoomFromUser();
                    es.addRoom(roomInfo.get("roomNum"), roomInfo.get("capacity"),
                            getRoomTypeFromUser(es.getRoomTypes()));
                    printSuccess("create room #" + roomInfo.get("roomNum"));
                    return "EventMenuPage";
                case "7":
                    List<String> data = getEventDataFromUser();
                    Set<String> requirements = getRequirementsFromUser(es.getAvailableFeatures());
                    int roomNum = getEventRoomFromUser(
                            es.getSuggestedRoomNumbers(requirements, Integer.parseInt(data.get(4))));
                    String speakerName = data.get(0);
                    data.add(Integer.toString(roomNum));
                    int eventId = es.createEvent(speakerName, userName, data, requirements);
                    printSuccess("create event id #" + eventId);
                    return "EventMenuPage";
                case "8":
                    int cancelId = getEventFromUser();
                    es.removeEvent(userName, cancelId);
                    printSuccess("cancel event id #" + cancelId);
                    return "EventMenuPage";
                case "9":
                    int updateId = getEventFromUser();
                    List<String> updatedEventData = getEventDataFromUser();
                    es.updateEventData(userName, updateId, updatedEventData);
                    printSuccess("info updated for event id #" + updateId);
                    return "EventMenuPage";
                default:
                    return "EventMenuPage";
            }
        } catch (UserNotFoundException
                | EventNotFoundException | RoomNotFoundException
                | InvalidEventFieldsException e) {
            printError(e.toString());
            return "EventMenuPage";
        } catch (NumberFormatException e){
            printError("Must be a number");
            return "EventMenuPage";
        }
    }

    /**
     * Gets an event id from the user and returns it back to the controller method to be used.
     * @return The event id. Must always be an int.
     * @throws InvalidEventFieldsException If the user doesn't give an integer.
     */
    private int getEventFromUser() throws InvalidEventFieldsException {
        try {
            return Integer.parseInt(askToEnter("event id"));
        } catch (NumberFormatException e) {
            throw new InvalidEventFieldsException("must be a number");
        }
    }

    /**
     * Gets a room type from the user.
     * @param availableTypes A string to tell the user which types are available.
     * @return A string representing the type the user chose.
     * @throws InvalidEventFieldsException If the user doesn't give a valid room type.
     */
    private String getRoomTypeFromUser(String availableTypes) {
        return (askToEnter("room type. Available are: "+availableTypes));
    }

    /**
     * Gets user input for a room and capacity.
     * @return The room and capacity.
     * @throws InvalidEventFieldsException If the user enters a non-integer.
     */
    private Map<String, Integer> getRoomFromUser() throws InvalidEventFieldsException {
        Map<String, Integer> data = new HashMap<>();
        try {
            data.put("roomNum", Integer.parseInt(askToEnter("room number")));
            data.put("capacity", Integer.parseInt(askToEnter("capacity")));
            return data;
        } catch (NumberFormatException e) {
            throw new InvalidEventFieldsException("must be a number");
        }
    }

    /**
     * Gets all appropriate data for an Event from the user and returns it back to the controller method.
     * @return Returns an array list of strings of the Event data.
     */
    private List<String> getEventDataFromUser() {
        ArrayList<String> data = new ArrayList<>();
        String[] stuffToEnter = {
                "speaker usernames (separated by commas, no spaces)",
                "event name (cannot contain special characters)",
                "date [yyyy-MM-dd HH:mm]",
                "duration (hours, as an integer)",
                "capacity (as an integer)",
                "VIP status ('true' if restricted for VIPs, 'false' otherwise)",
        };
        for (String e : stuffToEnter) {
            data.add(askToEnter(e));
        }
        return data;
    }

    /**
     * Gets a list of requirements for an event, from the user.
     * @param availableFeatures A string to tell the user what features are available.
     * @return A set of strings as to which features the user chose.
     */
    private Set<String> getRequirementsFromUser(String availableFeatures) {
        Set<String> data = new HashSet<>();
        print("Enter features for this event/room. Enter \"done\" to stop.\n");
        print("This is what you can choose from: " + availableFeatures + "\n");
        while (true) {
            String s = askToEnter("requirement");
            if (s.equalsIgnoreCase("done") || s.equalsIgnoreCase(""))
                return data;
            else if (!availableFeatures.contains(s.toLowerCase()))
                print("Sorry, that item is not available.\n");
            else
                data.add(s.toLowerCase());
        }
    }

    /**
     * Gets a room for an event to take place in, from the user.
     * @param suggestedRooms A set of integers representing suggested room numbers.
     * @return An integer representing the room number the user chose.
     */
    private int getEventRoomFromUser(Set<Integer> suggestedRooms) {
        try {
            return Integer.parseInt(askToEnter("Add this event to a room.\nSuggested rooms: " +
                    suggestedRooms.toString()));
        } catch (NumberFormatException e) {
            throw new InvalidEventFieldsException("must be a number");
        }
    }
}
