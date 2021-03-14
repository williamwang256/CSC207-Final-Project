package menus;

import usersfeature.PasswordNotMatchingException;
import usersfeature.UserSystem;
import usersfeature.UsernameAlreadyExistsException;
import usersfeature.UsernameEqualsNameException;
import mainsystem.SystemBuilder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Responsible for signing up users.
 */
public class SignUpPage extends MenuPresenter {
    UserSystem userSystem;

    public SignUpPage(SystemBuilder systemBuilder){
        userSystem = systemBuilder.getUserSystem();
    }

    /**
     * Runs the SignUpPage.
     * @return The appropriate menuing page.
     */
    @Override
    public String run() {
        ArrayList<String> options = new ArrayList<>(Arrays.asList(
                "Looking to be an attendee?",
                "Looking to be a VIP attendee?",
                "Looking to be an organizer?",
                "Looking to be an admin?",
                "Exit"));

        String option = getMenuOption("",
                options);
        String userType;
        switch (option){
            case "1":
                userType = "attendee";
                break;
            case "2":
                userType = "VIP";
                break;
            case "3":
                userType = "organizer";
                break;
            case "4":
                userType = "admin";
                break;
            case "exit":
                return "QuitPage";
            default:
                //print(userPresenter.getInvalidInput());
                return "StartUpPage";
        }

        print("\nType and enter \"exit\" to go back to the startup page\n");
        String name = askToEnter("your name");
        if (name.equalsIgnoreCase("exit")){
            return "StartUpPage";
        }
        String username = this.getUserName(name, userType);
        if (username.equalsIgnoreCase("exit")){
            return "StartUpPage";
        }
        this.setPassword(username);
        userSystem.setCurrentUsername(username);
        printSuccess("You have signed up successfully, " + name + "!");
        return "StartUpPage";
    }

    private String getUserName(String name, String usertype){
        String username = askToEnter("your desired username. Psst...your username and name must not match!");
        try{
            userSystem.createUser(name, username, usertype);
            return username;
        }catch (UsernameEqualsNameException e){
            print("Your username matches your name. Please try again!");
            return getUserName(name, usertype);
        }catch (UsernameAlreadyExistsException e){
            print("Dang! This username already exist. Please try a different one.");
            return getUserName(name, usertype);
        }
    }

    private void setPassword(String username){
        String password = askToEnter("your password");
        try {
            userSystem.setPassword(username, null, password);
        }catch (PasswordNotMatchingException e){
            print("Password authentication failed. Please try again!");
            setPassword(username);
        }
    }
}