package menus;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract class for the mini presenter classes. These classes represent individual menus.
 */

public abstract class MenuPresenter {

    private Scanner scanner;

    /**
     * Constructs an instance of MenuPresenter.
     */
    public MenuPresenter(){
        this.scanner = new Scanner(System.in).useDelimiter("\n");
    }

    /**
     * Executes the menu code.
     * Each menu will implement this method based on what is to be done in that menu, so it is made ABSTRACT.
     * @return The string of the next menu to call, after this menu has executed.
     */
    public abstract String run();

    /**
     * Creates a formatted string with a list of options for the user.
     * @param options The options available to the user.
     * @return The formatted string.
     */
    protected String formatMenuOptions(String msg, List<String> options) {
        StringBuilder sb = new StringBuilder("\n"+ msg + "\nSelect an option to continue.\n");
        for (int i=1; i<=options.size(); i++)
            sb.append("[").append(i).append("] ").append(options.get(i-1)).append("\n");
        sb.setLength(sb.length()-1);
        sb.append("\n--> ");
        return sb.toString();
    }

    /**
     * Prints a statement.
     * @param s The string to print.
     */
    protected void print(String s){
        System.out.print(s);
    }

    /**
     * Print a success message in green colour.
     * @param msg String that represents the success message.
     */
    protected void printSuccess(String msg) {
        System.out.println("\u001B[32m" + "SUCCESS: " + msg + "\u001B[0m");
    }

    /**
     * Print an error message in blue colour.
     * @param info String that represents the info to print
     */
    protected void printInfo(String info) {
        System.out.println("\u001B[34m" + info + "\u001B[0m");
    }

    /**
     * Print an error message in red colour.
     * @param errorMessage String that represents an error message.
     */
    protected void printError(String errorMessage) {
        System.err.println("UNSUCCESSFUL: " +errorMessage+"!\n");
    }

    /**
     * Get a string input from the user.
     * @return The string the user inputted after performing some checks.
     */
    protected String stringOptions() {
        String s =  scanner.next();
        Pattern pattern = Pattern.compile("[^#%]*"); // this regex rejects any string with the # or % characters.
        Matcher matcher = pattern.matcher(s);
        if (!matcher.matches()) {
            print("Invalid Input");
            print("Please Try Again!");
            return stringOptions();
        }
        return s;
    }

    /**
     * Prompt the user to input a string and get a string input from the user.
     * @param msg String that is printed as the prompt.
     * @return The string that the user inputted.
     */
    protected String askToEnter(String msg){
        print("Enter " + msg + ": ");

        String s =  stringOptions();
        if (s.equalsIgnoreCase("exit")){
            return "exit";
        }
        else{
            return s;
        }
    }

    /**
     * Get the option input from the user. loops till the user gets it right.
     * @return The string of the menu option. null if the input is invalid
     */
    protected String getMenuOption(String msg, List<String> options) {
        print(formatMenuOptions(msg, options));
        int rangeStartInclusive = 1;
        int rangeEndInclusive = options.size();
        String temp = stringOptions();
        try {
            int i = Integer.parseInt(temp);
            if (i >= rangeStartInclusive && i <= rangeEndInclusive) {
                return temp;
            } else{
                printError("This menu option does not exist! Please try again.");
                return getMenuOption(msg, options);
            }
        } catch(NumberFormatException e){
            if (temp.equalsIgnoreCase("exit")){
                return "exit";
            } else{
                printError("This menu option does not exist! Please try again.");
                return getMenuOption(msg, options);
            }
        }
    }

    /**
     * Used to add space between lines
     * @param numSpaces int containing number of lines of space
     * @return a string that adds the number of lines of space needed.
     */
    protected String addSpace(int numSpaces){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < Math.max(numSpaces, 0); i++){
            builder.append(" \n");
        }
        return builder.toString();
    }

    /**
     * Splits the inputted string into a list of strings.
     * @param input A string that contains multiple words.
     * @return An arrayList of each word in the string.
     */
    public ArrayList<String> splitStringInput(String input){
        String[] temp = input.split(" "); // splits the input into array
        ArrayList<String> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, temp);
        return arrayList;
    }

}
