package menus;

import messagesfeature.ConvoAlreadyExistsException;
import messagesfeature.MessageSystem;
import messagesfeature.UsersNotUniqueException;
import usersfeature.UserNotFoundException;
import mainsystem.SystemBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Represents a text interface for the organizer message page.
 * @author Caroline McKenzie, Anastasia Young, Yijia Zhou
 */
public class OrganizerMessagePage extends MenuPresenter {

    private MessageSystem ms;

    /**
     * Constructor for OrganizerMessagePage
     * @param systemBuilder the system builder
     */
    public OrganizerMessagePage(SystemBuilder systemBuilder){
        this.ms = systemBuilder.getMessageSystem();
    }

    private void createConvo(String convoType){
        List<String> users;
        if(convoType.equals("generic convo")){
            String usernames = askToEnter("username(s) of user(s) you wish to message, separated by spaces");
            users = splitStringInput(usernames);
            try{
                users = ms.checkUsers(users);
            }catch(UserNotFoundException | UsersNotUniqueException e){
                print(e.toString());
                return;
            }
        }else if(convoType.equals("all attendees")){
            users = ms.checkUsers(ms.getAllAttendees());

        }else{
            users = ms.checkUsers(ms.getAllSpeakers());
        }
        String content = askToEnter("message");
        try{
            ms.sendNewConvo(users, content);
            print("convo made!");
        }catch(ConvoAlreadyExistsException e){
            print(e.toString());
        }
    }

    /**
     * Runs the OrganizerViewPage
     * @return the appropriate menu page
     */
    @Override
    public String run(){
        ArrayList<String> options = new ArrayList<>(Arrays.asList(
                "Create a conversation",
                "Message all attendees",
                "Message all speakers",
                "Return"));
        String action = getMenuOption("This is the organizer conversation creation page", options);
        switch(action){
            case "1":
                createConvo("generic convo");
                return "MessageMenuPage";
            case "2":
                createConvo("all attendees");
                return "MessageMenuPage";
            case "3":
                createConvo("all speakers");
                return "MessageMenuPage";
            case "4":
                return "MessageMenuPage";
            default:
                return "OrganizerMessagePage";
        }
    }
}
