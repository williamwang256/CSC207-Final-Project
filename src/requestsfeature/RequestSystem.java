package requestsfeature;

import usersfeature.UserHandler;
import java.util.*;

/** Represents a controller specific to the request code.
 * @author William Wang, Caroline McKenzie
 */
public class RequestSystem {

    private final UserHandler userHandler;
    private final RequestHandler requestHandler;

    /**
     * Constructs an instance of RequestSystem.
     * @param data The data for requests to initialize with, as a string.
     * @param userHandler The user handler in the system.
     * @param requestHandler The request handler in the system.
     */
    public RequestSystem(String data, UserHandler userHandler, RequestHandler requestHandler) {
        this.userHandler = userHandler;
        this.requestHandler = requestHandler;
        this.requestReader(data);
    }

    /**
     * Gets the current user name and type.
     * @return A list of strings, the first one being the username and the second the user type.
     */
    public List<String> getUserNameAndType(){
        return new ArrayList<>(Arrays.asList(userHandler.getCurrentUsername(), userHandler.getCurrentUserType()));
    }

    /**
     * Resolves a given request.
     * @param user The user who's request to resolve.
     * @param eventId The id of the event the request is for.
     * @return true if it was resolved correctly, false otherwise.
     */
    public boolean resolveRequest(String user, int eventId) {
        return requestHandler.resolveRequest(user, eventId);
    }

    /**
     * Sends (creates) a new request.
     * @param user The user who is creating the request.
     * @param eventId The event the user is creating the request for.
     * @param item The item/description of the request.
     */
    public void sendRequest(String user, Integer eventId, String item){
        requestHandler.newUserRequest(eventId, user, item);
    }

    /**
     * Gets the information of all User Requests.
     * @return Returns a list of maps which represent the information associated with an all User Requests.
     */
    public List<Map<String, String>> getRequestsInfo(){
        return requestHandler.getRequestsInfo();
    }

    /**
     * Initializes and creates the requests that were saved in the system.
     * @param input A string representing data about requests.
     */
    protected void requestReader(String input) {
        if (input != null && input.length() > 0) {
            String[] inputSplit = input.split("##");
            for (String request : inputSplit) {
                String[] info = request.split("%%");
                if (info.length != 1) {
                    requestHandler.newUserRequest(Integer.parseInt(info[1]), info[2], info[0], info[3]);
                }
            }
        }
    }
}
