package menus;

import mainsystem.SystemBuilder;
import usersfeature.Attendee;
import usersfeature.UserSystem;

import java.util.ArrayList;
import java.util.Arrays;

public class MainMenuPage extends MenuPresenter {

    UserSystem userSystem;
    public MainMenuPage(SystemBuilder systemBuilder){
        this.userSystem = systemBuilder.getUserSystem();
    }

    @Override
    public String run() {

        ArrayList<String> prompts;
        prompts = new ArrayList<>(Arrays.asList(
                    "View Profile",
                    "Create Page",
                    "Messages Page",
                    "Events Page",
                    "Contacts Page",
                    "Requests Page",
                    "LogOut",
                    "quit"));

        String option = getMenuOption("This is the Main Menu.",
                prompts);

        switch (option){
            case "1":
                return "UserProfilePage";
            case "2":
                return "CreatePage";
            case "3":
                // messages page with usertype passed in
                return "MessageMenuPage";
            case "4":
                // events page with usertype passed in
                return "EventMenuPage";
            case "5":
                return "ContactsPage";
            case "6":
                return "RequestMenuPage";
            case "7":
                // back to the sign in, sign up page.
                return "StartUpPage";
            case "8":
                return "QuitPage";
            default:
                return "MainMenuPage";
        }
    }

}

