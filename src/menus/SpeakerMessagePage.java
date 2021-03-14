package menus;

import messagesfeature.CannotMessageUserException;
import messagesfeature.ConvoAlreadyExistsException;
import messagesfeature.MessageSystem;
import messagesfeature.UsersNotUniqueException;
import usersfeature.UserNotFoundException;
import mainsystem.SystemBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Represents a text interface for the speaker message page.
 * @author Caroline McKenzie, Anastasia Young, Yijia Zhou
 */
public class SpeakerMessagePage extends MenuPresenter {

    private final MessageSystem ms;

    /**
     * Constructor for the speaker messaging page
     * @param systemBuilder the system builder
     */
    public SpeakerMessagePage(SystemBuilder systemBuilder){this.ms = systemBuilder.getMessageSystem();}

    private void createConvo(String convoType){
        List<String> users;
        if(convoType.equals("message all attendees")){
            Integer eID = Integer.parseInt(askToEnter("the ID of your event"));
            users = ms.getAttendeesAtEvent(eID);
            try{
                users = ms.checkUsers(users);
            }catch(UserNotFoundException | UsersNotUniqueException e){
                print(e.toString());
                return;
            }
        }else{
            users = splitStringInput(askToEnter("username of speaker you wish to message"));
        }
        try{
            ms.checkForOrganizer(users);
            users = ms.checkUsers(users);
        }catch(CannotMessageUserException e){
            print(e.toString());
            return;
        }catch(UserNotFoundException e){
            print(e.toString());
            return;
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
     * Runs the SpeakerMessagePage
     * @return the appropriate messaging page
     */
    @Override
    public String run(){
        ArrayList<String> options = new ArrayList<>(Arrays.asList(
                "Message all attendees of your event",
                "Message specific attendee",
                "Return"));
        String action = getMenuOption("This is the speaker conversation creation page", options);
        switch(action){
            case "1":
                createConvo("message all attendees");
                return "MessageMenuPage";
            case "2":
                createConvo("message one attendee");
                return "MessageMenuPage";
            case "3":
                return "MessageMenuPage";
            default:
                return "SpeakerMessagePage";
        }

    }
}
