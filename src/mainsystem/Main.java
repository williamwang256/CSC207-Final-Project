package mainsystem;

import menus.*;

import java.util.HashMap;

/**
 * Responsible for systematic execution of Menu pages.
 */
public class Main {
    // This hashmap takes a string and maps that to a MenuPresenter object.
    HashMap<String, MenuPresenter> menus;

    // Stores the current menu that the user is on.
    MenuPresenter currentMenu;

    /**
     * Constructs an instance of Main.
     */
    public Main(){
        this.menus = new HashMap<>();
        SystemBuilder systemBuilder = new SystemBuilder();
        menus.put("StartUpPage", new StartUpPage(systemBuilder));
        menus.put("SignUpPage", new SignUpPage(systemBuilder));
        menus.put("SignInPage", new SignInPage(systemBuilder));
        menus.put("CreatePage", new CreatePage(systemBuilder));
        menus.put("UserProfilePage", new UserProfilePage(systemBuilder));
        menus.put("ContactsPage", new ContactsPage(systemBuilder));
        menus.put("MainMenuPage", new MainMenuPage(systemBuilder));
        menus.put("QuitPage", new QuitPage(systemBuilder));
        menus.put("EventMenuPage", new EventMenuPage(systemBuilder));
        menus.put("EventViewPage", new EventViewPage(systemBuilder));
        menus.put("MessageMenuPage", new MessageMenuPage(systemBuilder));
        menus.put("OrganizerMessagePage", new OrganizerMessagePage(systemBuilder));
        menus.put("SpeakerMessagePage", new SpeakerMessagePage(systemBuilder));
        menus.put("ArchivesPage", new ArchivesPage(systemBuilder));
        menus.put("ViewConversationPage", new ViewConversationPage(systemBuilder));
        menus.put("RequestMenuPage", new RequestMenuPage(systemBuilder));

        // Starting menu goes here. This would change to the StartUpMenu once it is made.
        currentMenu = menus.get("StartUpPage");
    }

    /**
     * Runs the program
     */
    public void runProgram() {
        while (true) {
            String tempMenu = currentMenu.run();
            if (tempMenu.equalsIgnoreCase("goodbye"))
                System.exit(0);
            this.currentMenu = menus.get(tempMenu);
        }
    }
}
