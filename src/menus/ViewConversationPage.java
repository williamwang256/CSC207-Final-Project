package menus;

import messagesfeature.ConvoNotFoundException;
import messagesfeature.MessageSystem;
import mainsystem.SystemBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Represents a text interface for the view conversation page.
 * @author Caroline McKenzie, Anastasia Young, Yijia Zhou
 */
public class ViewConversationPage extends MenuPresenter {

    private final MessageSystem ms;

    /**
     * Constructor for view conversation page
     * @param systemBuilder a system builder
     */
    public ViewConversationPage(SystemBuilder systemBuilder){
        this.ms = systemBuilder.getMessageSystem();
    }

    private String printSingleMessage(Integer messageId){
        StringBuilder output = new StringBuilder();
        List<String> messageInfo;
        messageInfo = ms.getMessageInfo(messageId);
        output.append("ID: ").append(messageId).append(" | ");
        output.append("Sender: ").append(messageInfo.get(1)).append(" | ");
        output.append("Time: ").append(messageInfo.get(3)).append("\n");

        int pID = Integer.parseInt(messageInfo.get(2));
        if(pID != 0){
            output.append(">> Replying to: ");
            output.append(ms.getMessageInfo(pID).get(0)).append("\n");
        }

        output.append(">> ").append(messageInfo.get(0)).append("\n");

        return output.toString();
    }

    private String formatUsers(String list_name, List<String> users){
        StringBuilder output = new StringBuilder();
        output.append(list_name);
        for (String u: users){
            if (users.indexOf(u) == users.size()-1){
                output.append(u).append("\n");
            }else{
                output.append(u).append(", ");
            }
        }
        output.append("\n");
        return output.toString();

    }

    private String convoInfo(int convoId, List<Integer> messageIds, boolean archive){
        StringBuilder output = new StringBuilder();
        List<String> users = ms.getUsers(convoId);
        output.append(formatUsers("GROUP MEMBERS: ", users));
        for(int i: messageIds){
            output.append(printSingleMessage(i)).append("\n");
        }
        if(!archive) {
            List<String> users_read = ms.getReadUsers(convoId);
            output.append(formatUsers("READ BY: ", users_read));
        }

        return output.toString();
    }

    private String displayAllConvos(List<Integer> allConversations){
        List<Integer> readConversations = ms.getUserReadConversations();
        StringBuilder convoIDString = new StringBuilder("Conversation ID(s): \n");
        ArrayList<Integer> convoID = new ArrayList<>(allConversations);
        if (convoID.isEmpty()){
            return "You do not have any conversations.";
        }
        for (Integer id: convoID){
            convoIDString.append(id);
            if(readConversations.contains(id)){
                convoIDString.append(" - Read");
            } else{
                convoIDString.append(" - Unread");
            }
            convoIDString.append("\n");

        }
        return convoIDString.toString();
    }

    private Integer askForConvoId(){
        List<Integer> allConversations = ms.getUserConversations();
        String result = displayAllConvos(allConversations);
        print(result);
        if(result.equals("You do not have any conversations.")){
            return -1;
        }
        Integer convoId = Integer.parseInt(askToEnter("the conversation you would like to view"));
        if(!allConversations.contains(convoId)){ //user enters convo that is not theirs
            throw new ConvoNotFoundException();
        }
        else{
            return convoId;
        }
    }

    /**
     * Runs the ViewConversationPage
     * @return the appropriate menu page
     */
    @Override
    public String run(){
        //i did it this way to avoid another cs.getUserConversations() call
        //just to clarify this displays all conversation information as well as returning convo id.
        Integer convoId;
        try{
            convoId = askForConvoId();
        }catch(ConvoNotFoundException e){
            print(e.toString());
            return "ViewConversationPage";
        }
        if(convoId == -1){
            return "MessageMenuPage";
        }
        ms.setConvoRead(convoId);
        List<Integer> messageIds = ms.getConvoMessages(convoId);
        print(convoInfo(convoId, messageIds, false));
        ArrayList<String> options = new ArrayList<>(Arrays.asList(
                "Send message",
                "Reply message",
                "View archived messages",
                "Archive message",
                "Set conversation as unread",
                "Delete message",
                "Return"));
        String action = getMenuOption("This is the conversation options page", options);
        String content;
        int m_id;
        switch(action){
            case "1":   //send message
                content = askToEnter("message");
                ms.sendMessage(convoId, content);
                printSuccess("Message sent.");
                break;
            case "2":   //reply to message
                int parentId = 0;
                while(parentId == 0) {
                    parentId = Integer.parseInt(askToEnter("id of message you wish to reply to"));
                }
                content = askToEnter("message");
                ms.reply(convoId, content, parentId);
                printSuccess("Reply sent.");
                break;
            case "3":   //view archived messages
                List<Integer> archiveIds = ms.getConvoArchive(convoId);
                print(convoInfo(convoId, archiveIds, true));
                return "ArchivesPage";
            case "4":   //archive message
                m_id = Integer.parseInt(askToEnter("id of the message you want to archive"));
                ms.archiveMessage(m_id);
                printSuccess("Message " + m_id + " archived.");
                break;
            case "5":   //set conversation as unread
                ms.setConvoUnread(convoId);
                printSuccess("Conversation marked as unread.");
                break;
            case "6":   //delete message
                m_id = Integer.parseInt(askToEnter("id of the message you want to delete"));
                ms.deleteMessage(m_id);
                printSuccess("Message " + m_id + " deleted.");
                break;
            case "7":   //return
                break;
            default:
                return "OrganizerMessagePage";
        }
        return "MessageMenuPage";
    }

}
