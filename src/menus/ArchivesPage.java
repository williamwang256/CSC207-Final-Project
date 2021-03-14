package menus;

import messagesfeature.MessageSystem;
import mainsystem.SystemBuilder;

import java.util.ArrayList;
import java.util.Arrays;

/** Represents a text interface for the archives page.
 * @author Caroline McKenzie, Anastasia Young, Yijia Zhou
 */
public class ArchivesPage extends MenuPresenter {

    MessageSystem ms;

    /**
     * Constructor for the ArchivesPage
     * @param systemBuilder the system builder
     */
    public ArchivesPage(SystemBuilder systemBuilder){
        this.ms = systemBuilder.getMessageSystem();
    }

    /**
     * Runs the ArchivesPage
     * @return the appropriate menu page
     */
    @Override
    public String run(){
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Unarchive message.", "Exit."));
        String result = getMenuOption("", options);
        if(result.equals("1")){ //user wants to unarchive message
            Integer convoId = Integer.parseInt(askToEnter("convo id"));
            Integer messageId = Integer.parseInt(askToEnter("message id of message to unarchive"));
            ms.unarchiveMessage(messageId, convoId);
        }
        return "ViewConversationPage";
    }
}
