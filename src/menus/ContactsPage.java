package menus;

import usersfeature.UserNotFoundException;
import usersfeature.UserSystem;
import mainsystem.SystemBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class ContactsPage extends MenuPresenter {

    UserSystem userSystem;

    public ContactsPage(SystemBuilder systemBuilder){
        this.userSystem = systemBuilder.getUserSystem();
    }

    /**
     * Runs the ContactsPage.
     * @return The appropriate menuing page.
     */
    public String run(){
        String currentUsername = this.userSystem.getCurrentUsername();

        ArrayList<String> prompts = new ArrayList<>(Arrays.asList(
                "View Contacts",
                "Add Contacts",
                "Delete Contacts",
                "Main Menu"));

        String option = getMenuOption("This is the Contacts Page.",
                prompts);

        switch (option) {
            case "1":
                print(formatAttendeeFriends(currentUsername));
                print(this.addSpace(1));
                askToEnter("Enter any key to go back");
                return "ContactsPage";
            case "2":
                String newFriend = askToEnter("Username of new contact");
                try{
                    if (userSystem.addFriend(currentUsername, newFriend)){
                        printSuccess("You have added " + newFriend + " to your list of contacts!");
                    }
                    else printError("Looks like this username was already in your contact list");
                }catch (UserNotFoundException e){
                    print(e.toString());
                    printError("User with such username doesn't exist! Please Try Again!");
                }
                return "ContactsPage";
            case "3":
                print(formatAttendeeFriends(currentUsername));
                print(this.addSpace(1));
                print("Enter the username of whichever friend you wish to delete");
                print(this.addSpace(1));

                String oldFriend = askToEnter("Username of contact to delete");
                try{
                    if (userSystem.removeFriend(currentUsername, oldFriend)){
                        printSuccess("You have removed " + oldFriend + " from your list of friends!");
                    }
                    else printError("Please Try Again!");
                }catch (UserNotFoundException e){
                    print(e.toString());
                    printError("Please Try Again!");
                }
                return "ContactsPage";
            default:
                return "MainMenuPage";
        }

    }

    /**
     * returns the formatted string of contacts that the username has
     * @return String formatted contact list
     */
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


}
