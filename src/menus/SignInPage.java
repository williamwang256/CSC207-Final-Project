package menus;


import usersfeature.UserSystem;
import mainsystem.SystemBuilder;

/**
 * responsible for running the SignInPage
 */
public class SignInPage extends MenuPresenter {
    private final UserSystem userSystem;

    public SignInPage(SystemBuilder systemBuilder){
        this.userSystem = systemBuilder.getUserSystem();
    }

    /**
     * Runs the SignInPage.
     * @return The appropriate menuing page after authenticating the user at the keyboard.
     */
    @Override
    public String run() {
        for(int i = 0; i< 5; i++) {
            print("\nenter \"exit\" to go to the Login Page\n");
            String username = askToEnter("username");
            if (username.equalsIgnoreCase("exit")){
                return "StartUpPage";
            }
            String password = askToEnter("password");
            if (authenticate(username, password)){
                setUsername(username);
                String currentUsername = userSystem.getCurrentUsername();
                String currentName = userSystem.getName(currentUsername);
                printSuccess("You have signed in successfully, " + currentName + "!");
                return "MainMenuPage";
            }
            print(getInvalidCredentials());
        }
        print(String.format("You have attempted to sign in %d times.", 5));
        return "StartUpPage";
    }

    private String getInvalidCredentials() {return "Invalid Login credentials. \nPlease Try Again!"; }

    private boolean authenticate(String username, String password){
        return userSystem.authenticate(username, password);
    }

    private void setUsername(String username){
        userSystem.setCurrentUsername(username);
    }
}