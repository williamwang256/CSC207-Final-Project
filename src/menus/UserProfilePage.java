package menus;

import eventsfeature.EventSystem;
import usersfeature.UserSystem;
import mainsystem.SystemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Responsible for displaying the user's information
 */
public class UserProfilePage extends MenuPresenter {

    UserSystem userSystem;
    EventSystem eventSystem;

    public UserProfilePage(SystemBuilder systemBuilder){
        this.userSystem = systemBuilder.getUserSystem();
        this.eventSystem = systemBuilder.getEventSystem();
    }

    /**
     * Runs the ContactsPage.
     * @return The appropriate menuing page.
     */
    @Override
    public String run(){
        String currentUsername = this.userSystem.getCurrentUsername();
        String userType = this.userSystem.getUserType(currentUsername);
        printInfo(this.userSystem.getUserInformation(currentUsername));
        print(this.addSpace(1));

        if (userType.equalsIgnoreCase("attendee") || userType.equalsIgnoreCase("VIP")) {
            if (userType.equalsIgnoreCase("VIP")){
                print("Congratulations, you are a VIP! \n");
            }
            else {
                print("You are not a VIP yet!\n");
            }
            print(this.addSpace(1));

            print("Your contacts:\n");
            printInfo(formatAttendeeFriends(currentUsername));
            print(this.addSpace(1));

            print("Your registered events:\n");
            printEventData(currentUsername);

        } else if (userType.equalsIgnoreCase("organizer") || userType.equalsIgnoreCase("admin")){
            print("Your Contacts:\n");
            printInfo(formatAttendeeFriends(currentUsername));
            print(this.addSpace(1));

            print("Your registered events:\n");
            printEventData(currentUsername);
            print(this.addSpace(1));

            print("Your created events:\n");
            String events = getAllEvents(currentUsername, "organizer");
            printInfo(events);

        }else if(userType.equalsIgnoreCase("speaker")){
            // display talks
            print("The events you'll be talking in:\n");
            String events = getAllEvents(currentUsername, "speaker");
            printInfo(events);
        }
        print(addSpace(2));
        askToEnter("any key to go back");
        return("MainMenuPage");
    }

    private String formatAttendeeFriends(String username) {
        ArrayList<String> friendList = userSystem.getFriends(username);
        StringBuilder friendString = new StringBuilder();
        if(friendList.isEmpty()){
            return "\nNo contacts were found.\n";
        }
        friendString.append("Your Contact List:");
        for (String u : friendList) {
            String s = u + ", ";
            friendString.append(s);
        }
        return friendString.substring(0, friendString.length() - 2);
    }

    private String getAllEvents(String username, String userType){
        List<String> eventList;
        if (userType.equalsIgnoreCase("speaker")) {
            eventList = eventSystem.getAllEventNameList(userSystem.getTalks(username));
        }
        else{
            eventList = eventSystem.getAllEventNameList(userSystem.getCreatedEvents(username));
        }

        if (eventList.isEmpty()){
            return "You haven't registered for any events.";
        }
        else{
            StringBuilder event = new StringBuilder();
            for (String name: eventList){
                event.append(" -- ").append(name);
            }
            return event.substring(3);
        }
    }

//    private String getEventData(String username){
//        List<Map<String, String>> eventData =
//        if (eventData.isEmpty()){
//            return "You have not registered for any events.";
//        }
//        else {
//            return eventData.toString();
//        }
//    }

    private void printEventData(String username){
        List<Map<String, String>> data = this.eventSystem.getSpecificEventData("user", username);
        if(data.isEmpty()){
            printInfo("No events were found.");
            return;
        }
        for (Map<String, String> event:
             data) {
            printInfo("Event ID: " + event.get("eventId"));
            printInfo("Event Name: " + event.get("name"));
            printInfo("Room number: " + event.get("roomNum"));
            printInfo("Speaker(s): " + event.get("speakers"));
            printInfo("Time: " + event.get("time"));
            printInfo("------------");
        }
    }

}
