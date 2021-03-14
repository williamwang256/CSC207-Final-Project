package menus;

import eventsfeature.EventNotFoundException;
import eventsfeature.EventSystem;
import eventsfeature.InvalidEventFieldsException;
import eventsfeature.RoomNotFoundException;
import usersfeature.UserNotFoundException;
import mainsystem.SystemBuilder;

import java.util.*;

/** Represents a text interface for viewing Event information.
 * @author Kevin Cecco, Daniel Chan, William Wang
 */
public class EventViewPage extends MenuPresenter {

    private final EventSystem es;

    /**
     * Constructs an instance of EventViewPage.
     * @param sb The SystemBuilder in the program.
     */
    public EventViewPage(SystemBuilder sb) {
        this.es = sb.getEventSystem();
    }

    /**
     * Runs the EventViewPage.
     * @return The appropriate menu page, as a String.
     */
    @Override
    public String run() {
        try {
            String userName = es.getUserNameAndType().get(0);
            String userType = es.getUserNameAndType().get(1);

            ArrayList<String> options;
            if (userType.equals("attendee") || userType.equals("VIP"))
                options = new ArrayList<>(Arrays.asList(
                        "specific event",
                        "all events",
                        "within range",
                        "on specific date",
                        "with speakers",
                        "my events"));
            else if (userType.equals("organizer"))
                options = new ArrayList<>(Arrays.asList(
                        "specific event",
                        "all events",
                        "within range",
                        "on specific date",
                        "with speakers",
                        "my events",
                        "attendees"));
            else
                options = new ArrayList<>(Arrays.asList(
                        "specific event",
                        "all events",
                        "within range",
                        "on specific date",
                        "with speakers",
                        "my events"));

            switch (getMenuOption("This is the " + userType + " event view menu.", options)) {
                case "1":
                    printInfo(formatEventsList(new ArrayList<>(Collections.singleton
                            (es.getEventInfo(getEventFromUser())))));
                    return "EventMenuPage";
                case "2":
                    printInfo(formatEventsList(es.getSpecificEventData("all", "")));
                    return "EventMenuPage";
                case "3":
                    printInfo(formatEventsList(es.getSpecificEventData("eventRange", getEventRangeFromUser())));
                    return "EventMenuPage";
                case "4":
                    printInfo(formatEventsList(es.getSpecificEventData("day", getDateFromUser())));
                    return "EventMenuPage";
                case "5":
                    printInfo(formatEventsList(es.getSpecificEventData("speakers", getSpeakersFromUser())));
                    return "EventMenuPage";
                case "6":
                    printInfo(formatEventsList(es.getSpecificEventData("user", userName)));
                    return "EventMenuPage";
                case "7":
                    printInfo(formatAttendeesList(es.getAllAttendeeUsernames(getEventFromUser())));
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
     * Get a string representing an event.
     * @param infos a list of information to format.
     * @return A formatted string containing all the information about the event.
     */
    public String formatEventsList(List<Map<String, String>> infos) {
        StringBuilder output = new StringBuilder();
        try {
            for (Map<String, String> i : infos) {
                String description =
                                  "\nEvent id: "+i.get("eventId")
                                + "\nName: "+i.get("name")
                                + "\nTime: "+i.get("time")
                                + "\nRoom: "+i.get("roomNum")
                                + "\nSpeakers: "+i.get("speakers")
                                + "\nNo. of attendees: "+i.get("numAttendees")
                                + "\nFull? "+i.get("isFull")
                                + "\nRequirements: "+i.get("requirements")
                                + "\nVIP only? "+i.get("isVIP");
                output.append(description).append("\n");
            }
        } catch (NullPointerException e) {
            return "error printing event";
        }
        if (output.toString().isEmpty()) { output.append("no events"); }
        return output.toString();
    }

    /**
     * Get a string representation of all the usernames signed up for a given event, from the Presenter.
     * @param infos the information to format.
     * @return A formatted string containing all the attendee usernames signed up for the event.
     */
    public String formatAttendeesList(List<String> infos) {
        StringBuilder output = new StringBuilder();
        if (infos.isEmpty())
            return "event has no attendees.";
        for (String id : infos)
            output.append("\nuser: ").append(id);
        return output.append("\n").toString();
    }

    /**
     * Gets a room number and event from the user and returns it back to the controller method to be used.
     * @return The room number and event id. Must always be a list of two Integers.
     * @throws InvalidEventFieldsException If the user gives invalid information.
     */
    private int getEventFromUser() throws InvalidEventFieldsException {
        try {
            return Integer.parseInt(askToEnter("event id"));
        } catch (NumberFormatException e) {
            throw new InvalidEventFieldsException("must be a number");
        }
    }

    /**
     * Gets a range of Event ids and returns it back to the controller method to be used.
     * @return The range of events, formatted as "int int", as a String.
     */
    private String getEventRangeFromUser() {
        return askToEnter("events between two events, separated by a comma, no spaces " +
                "(<starting event id>,<ending event id>)");
    }

    /**
     * Gets a date from the user and returns it back to the controller method to be used.
     * @return The date, formatted as dd-MM-yyyy, as a String.
     */
    private String getDateFromUser() {
        return askToEnter("date of events (dd/MM/yyyy)");
    }

    /**
     * Gets a speaker from the user and returns it back to the controller method to be used.
     * @return The speakerId.
     * @throws InvalidEventFieldsException If the user inputs anything but a String.
     */
    private String getSpeakersFromUser() throws InvalidEventFieldsException {
        try{
            return askToEnter("speaker usernames, separated by commas, no spaces");
        } catch (InputMismatchException e){
            throw new InvalidEventFieldsException("the speakers must be as Strings");
        }
    }
}
