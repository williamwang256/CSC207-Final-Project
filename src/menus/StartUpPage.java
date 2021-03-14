package menus;

import usersfeature.UserSystem;
import mainsystem.SystemBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class StartUpPage extends MenuPresenter {
    SystemBuilder systemBuilder;
    UserSystem userSystem;

    public StartUpPage(SystemBuilder systemBuilder){
        this.systemBuilder = systemBuilder;
        this.userSystem = systemBuilder.getUserSystem();
    }

    @Override
    public String run() {
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Sign in", "Sign up", "Quit"));

        String curr_user = userSystem.getCurrentUsername();
        String welcomeMessage;
        if (curr_user != null){
            welcomeMessage = String.format("Welcome %s! Please enter one of the following options", curr_user);
        }
        else{
            welcomeMessage = "Welcome! Please enter one of the following options";
        }

        String option = getMenuOption(welcomeMessage,
                options);

        switch (option){
            case "1":
                return "SignInPage";
            case "2":
                return "SignUpPage";
            case "3":
            case "exit":
                return "QuitPage";
            default:
                //print(userPresenter.getInvalidInput());
                return "StartUpPage";
        }
    }
}
