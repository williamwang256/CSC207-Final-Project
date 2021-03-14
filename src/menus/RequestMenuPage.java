package menus;

import eventsfeature.EventSystem;
import requestsfeature.RequestSystem;
import mainsystem.SystemBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** Represents a text interface for interacting with Requests.
 * @author Caroline McKenzie, William Wang
 */
public class RequestMenuPage extends MenuPresenter {

    private final RequestSystem rs;
    private final EventSystem es;

    /**
     * Creates an instance of RequestMenuPage.
     * @param systemBuilder The SystemBuilder in the program.
     */
    public RequestMenuPage(SystemBuilder systemBuilder){
        this.rs = systemBuilder.getRequestSystem();
        this.es = systemBuilder.getEventSystem();
    }

    /**
     * Get a formatted string representing an all requests.
     * @return The formatted string.
     */
    private String allRequestInfo(){
        StringBuilder sb = new StringBuilder();
        try {
            List<Map<String, String>> all_info = rs.getRequestsInfo();
            sb.append("List of all current user requests:\n");
            sb.append("\n");
            for(Map<String, String> hmap:all_info){
                sb.append("Request: ");
                sb.append(hmap.get("request"));
                sb.append("\n");
                sb.append("Current status: ");
                sb.append(hmap.get("status"));
                sb.append("\n");
                sb.append("Request made by: ");
                sb.append(hmap.get("user"));
                sb.append(" for event ");
                sb.append(hmap.get("event"));
                sb.append("\n");
                sb.append("\n");
            }
            String s = sb.toString();
            if (s.equals("List of all current user requests:\n")){
                s = "There are currently no requests";
            }
            return s;
        } catch(NullPointerException e){
            return "There are currently no requests.";
        }
    }

    /**
     * Get a formatted string representing an event.
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
                                + "\nRequirements: "+i.get("requirements");
                output.append(description);
            }
        } catch (NullPointerException e) {
            return "error printing event";
        }
        if (output.toString().isEmpty()) { output.append("no events"); }
        return output.toString();
    }

    /**
     * Runs the RequestMenuPage.
     * @return The appropriate menu page, as a String.
     */
    @Override
    public String run(){
        String userName = rs.getUserNameAndType().get(0);
        String userType = rs.getUserNameAndType().get(1);
        ArrayList<String> options;
        String option = "exit";
        try {
            if (userType.equals("attendee") || userType.equals("speaker") || userType.equals("VIP")) {
                options = new ArrayList<>(Arrays.asList("submit a request", "main menu", "quit"));

                switch (getMenuOption("This is the " + userType + " requests menu.", options)) {
                    case "1":
                        // print events to choose from.
                        printInfo("Events you have signed up for: \n");
                        printInfo(formatEventsList(es.getSpecificEventData("user", userName)));
                        es.getSpecificEventData("user", userName);
                        print("Enter exit to go to main menu or \n");
                        option = askToEnter("the id of the event you would like to submit a request for");
                        Integer event_id = Integer.parseInt(option);
                        String item = askToEnter("your request");
                        rs.sendRequest(userName, event_id, item);
                        printSuccess("Request sent. You will be informed when your request has been approved");
                        return "RequestMenuPage";
                    case "2":
                        return "MainMenuPage";
                    case "3":
                        return "QuitPage";
                    default:
                        return "RequestMenuPage";
                }
            } else {
                printInfo(allRequestInfo());
                options = new ArrayList<>(Arrays.asList("mark a request as resolved", "main menu", "quit"));
                switch (getMenuOption("This is the " + userType + " requests menu.", options)) {
                    case "1":
                        String username = askToEnter(
                                "the username of the user you would like to resolve the request of.");
                        option = askToEnter("the event id related to the request you'd like to resolve.");
                        int event_id = Integer.parseInt(option);
                        if (rs.resolveRequest(username, event_id)) {
                            printSuccess("Request marked as resolved.");
                        } else {
                            printError("Error with processing your request. " +
                                    "Please ensure you have entered the username and event id correctly");
                        }
                        return "RequestMenuPage";
                    case "2":
                        return "MainMenuPage";
                    case "3":
                        return "QuitPage";
                    default:
                        return "RequestMenuPage";
                }
            }
        } catch (NumberFormatException e) {
            if (option.equalsIgnoreCase("exit")){
                return "MainMenuPage";
            }
            printError("must be a number");
            return "RequestMenuPage";
        }
    }
}
