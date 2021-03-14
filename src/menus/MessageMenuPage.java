package menus;

import messagesfeature.CannotMessageUserException;
import messagesfeature.ConvoAlreadyExistsException;
import messagesfeature.MessageSystem;
import messagesfeature.UsersNotUniqueException;
import usersfeature.*;
import mainsystem.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Represents a text interface for the message menu page.
 * @author Caroline McKenzie, Anastasia Young, Yijia Zhou
 */
public class MessageMenuPage extends MenuPresenter {

    private String userType;
    private MessageSystem ms;
    private UserSystem us;

    /**
     * Constructor for MessageMenuPage
     * @param systemBuilder a system builder
     */
    public MessageMenuPage(SystemBuilder systemBuilder){
        this.ms = systemBuilder.getMessageSystem();
        this.us = systemBuilder.getUserSystem();
    }

    /**
     * Runs the MessageMenuPage
     * @return the appropriate menu page
     */
    @Override
    public String run(){
        ms.updateAllUsers();
        this.userType = ms.getUserType();
        ArrayList<String> options = new ArrayList<>(Arrays.asList(
                "View conversation.",
                "Create new conversation.",
                "Exit."));
        switch(getMenuOption("This is the message main menu.", options)){
            case "1":   //view conversation
                return "ViewConversationPage";
            case "2":   //create conversation
                if(userType.equals("attendee")){
                    String usernames = askToEnter("username(s) of user(s) you wish to message, separated by spaces");
                    List<String> users = splitStringInput(usernames);
                    try{
                        users = ms.checkUsers(users);
                    }catch(UsersNotUniqueException | UserNotFoundException e){
                        print(e.toString());
                        return "MessageMenuPage";
                    }
                    try{
                        ms.checkForOrganizer(users);
                    }catch(CannotMessageUserException e){
                        print(e.toString());
                        return "MessageMenuPage";
                    }
                    String content = askToEnter("message");
                    try{
                        ms.sendNewConvo(users, content);
                        print("convo sent");
                    }catch(ConvoAlreadyExistsException e){
                        print(e.toString());
                    }
                    return "MessageMenuPage";
                }else if(userType.equals("organizer")){
                    return "OrganizerMessagePage";
                }else{
                    return "SpeakerMessagePage";
                }

            case "3":   //exit
                return "MainMenuPage";
            default:    //invalid input
                return "MessageMenuPage";
        }
    }
}
