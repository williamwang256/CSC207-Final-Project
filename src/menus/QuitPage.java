package menus;

import mainsystem.SystemBuilder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Page responsible for quitting the program and saving all the changes.
 */
public class QuitPage extends MenuPresenter {

    SystemBuilder systemBuilder;
    public QuitPage(SystemBuilder systemBuilder){
       this.systemBuilder = systemBuilder;
    }

    /**
     * Runs the QuitPage.
     * @return The appropriate menuing page.
     */
    @Override
    public String run() {
        ArrayList<String> options = new ArrayList<>(Arrays.asList(
                "Yes",
                "No"
        ));
        String option = this.getMenuOption("Are you sure you want to quit?", options);
        if ("1".equals(option)) {
            systemBuilder.saveAll();
            return "Goodbye";
        }
        print("Returning to Main Menu...");
        return "MainMenuPage";
    }
}
