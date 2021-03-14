package menus;
import usersfeature.*;
import mainsystem.SystemBuilder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * CreatePage that allows users different options to create Users etc.
 */
public class CreatePage extends MenuPresenter {

    UserSystem userSystem;

    /**
     * CreatePage that allows users different options to create Users etc.
     */
    public CreatePage(SystemBuilder systemBuilder){this.userSystem = systemBuilder.getUserSystem();}

    /**
     * Runs the CreatePage.
     * @return The appropriate menuing page.
     */
    @Override
    public String run() {
        String currentUsername = this.userSystem.getCurrentUsername();
        String currentUserType = this.userSystem.getUserType(currentUsername);

        if (currentUserType.equals("attendee") || currentUserType.equals("VIP") || currentUserType.equals("speaker")) {
            print("Sorry, you are not worthy enough to access this page!");
            print(addSpace(1));
            askToEnter("any key to go back");
            return ("MainMenuPage");
        } else if (currentUserType.equals("admin")) {
            ArrayList<String> prompts = new ArrayList<>(Arrays.asList(
                    "Create speaker",
                    "Create attendee",
                    "Create VIP",
                    "Give VIP status",
                    "Remove VIP status",
                    "Main Menu"));

            String option = getMenuOption("This is the Create Page.",
                    prompts);
            String userType;
            switch (option) {
                case "1": //speaker
                    userType = "speaker";
                    print("\nType and enter \"exit\" to go back to the startup page\n");
                    String speakerName = askToEnter("speaker name");
                    String speakerUsername = this.getUserName(speakerName, userType);
                    this.setPassword(speakerUsername);
                    this.userSystem.addCreatedSpeaker(currentUsername, speakerUsername);
                    printSuccess("You have created a speaker by the name of " + speakerName + "!");

                    print(this.addSpace(1));
                    askToEnter("Enter any key to go back");
                    return "CreatePage";

                case "2": //attendee
                    userType = "attendee";
                    print("\nType and enter \"exit\" to go back to the startup page\n");
                    String attendeeName = askToEnter("attendee name");
                    String attendeeUsername = this.getUserName(attendeeName, userType);
                    this.setPassword(attendeeUsername);
                    printSuccess("You have created an attendee by the name of " + attendeeName + "!");

                    print(this.addSpace(1));
                    askToEnter("Enter any key to go back");
                    return "CreatePage";

                case "3": //VIP
                    userType = "VIP";
                    print("\nType and enter \"exit\" to go back to the startup page\n");
                    String vipName = askToEnter("VIP name");
                    String vipUsername = this.getUserName(vipName, userType);
                    this.setPassword(vipUsername);
                    printSuccess("You have created a VIP by the name of " + vipName + "!");

                    print(this.addSpace(1));
                    askToEnter("Enter any key to go back");
                    return "CreatePage";

                case "4":
                    print("\nType and enter \"exit\" to go back to the startup page\n");
                    String username = askToEnter("username of the person you wish to make a VIP.");
                    makeVIP(username);
                    print(this.addSpace(1));
                    askToEnter("Enter any key to go back");
                    return "CreatePage";
                case "5":
                    print("\nType and enter \"exit\" to go back to the startup page\n");
                    String userName = askToEnter("username of the person of whom you wish to remove VIP status .");
                    removeVIP(userName);
                    print(this.addSpace(1));
                    askToEnter("Enter any key to go back");
                    return "CreatePage";

                default:
                    return "MainMenuPage";
            }

        } else {
            ArrayList<String> prompts = new ArrayList<>(Arrays.asList(
                    "Create speaker",
                    "Create attendee",
                    "Main Menu"));

            String option = getMenuOption("This is the Create Page.",
                    prompts);
            String userType;
            switch (option) {
                case "1": //speaker
                    userType = "speaker";
                    print("\nType and enter \"exit\" to go back to the startup page\n");
                    String speakerName = askToEnter("speaker name");
                    String speakerUsername = this.getUserName(speakerName, userType);
                    this.setPassword(speakerUsername);
                    this.userSystem.addCreatedSpeaker(currentUsername, speakerUsername);
                    printSuccess("You have created a speaker by the name of " + speakerName + "!");

                    print(this.addSpace(1));
                    askToEnter("Enter any key to go back");
                    return "CreatePage";

                case "2": //attendee
                    userType = "attendee";
                    print("\nType and enter \"exit\" to go back to the startup page\n");
                    String attendeeName = askToEnter("attendee name");
                    String attendeeUsername = this.getUserName(attendeeName, userType);
                    this.setPassword(attendeeUsername);
                    printSuccess("You have created an attendee by the name of " + attendeeName + "!");

                    print(this.addSpace(1));
                    askToEnter("Enter any key to go back");
                    return "CreatePage";

                default:
                    return "MainMenuPage";

            }

        }
    }
    private String getUserName(String name, String usertype){
        String username = askToEnter("the desired username. Psst...the username and name must not match!");
        try{
            userSystem.createUser(name, username, usertype);
            return username;
        }catch (UsernameEqualsNameException e){
            print("The username matches the name. Please try again!");
            return getUserName(name, usertype);
        }catch (UsernameAlreadyExistsException e){
            print("Dang! This username already exist. Please try a different one.");
            return getUserName(name, usertype);
        }
    }

    private void setPassword(String username){
        String password = askToEnter("the password");
        try {
            userSystem.setPassword(username, null, password);
        }catch (PasswordNotMatchingException e){
            print("Password authentication failed. Please try again!");
            setPassword(username);
        }
    }

    private void makeVIP(String username){
        try{
            if (userSystem.makeVIP(username)){
                printSuccess("This user has been given VIP status!");
            }
            else{printError("This user is already a VIP!");}
        } catch (UserNotFoundException e){
            printError("No VIP by this username was found!");
        }
    }

    private void removeVIP(String username){
        try{
            if (userSystem.removeVIP(username)){
                printSuccess("This user is no longer a VIP");
            }
            else{throw new UserNotFoundException();}
        } catch (UserNotFoundException e){
            printError("No VIP by this username was found!");
        }
    }
}
