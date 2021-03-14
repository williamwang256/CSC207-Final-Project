package requestsfeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Use case class that handles user requests
 * @author Caroline McKenzie, William Wang
 */
public class RequestHandler {

    private final List<UserRequest> requests;

    /**
     * Constructs an instance of RequestHandler
     */
    public RequestHandler() {
        this.requests = new ArrayList<>();
    }

    /**
     * Gets the information of all User Requests within the RequestHandler.
     * @return Returns a list of maps which represent the information associated with an all User Requests.
     */
    protected List<Map<String, String>> getRequestsInfo() {
        List<Map<String, String>> out = new ArrayList<>();
        for (UserRequest ur : requests) {
            Map<String, String> mp = new HashMap<>();
            mp.put("event", Integer.toString(ur.getEventId()));
            mp.put("user", ur.getUsername());
            mp.put("request", ur.getItem());
            mp.put("status", ur.getStatus());
            out.add(mp);
        }
        return out;
    }

    /**
     * Marks a request as being resolved given the event and user associated with the request.
     * @param user the username of the user who submitted the request.
     * @param eventId the id of the event associated with the request.
     * @return boolean true iff request successfully marked as resolved.
     */
    protected boolean resolveRequest(String user, int eventId) {
        boolean resolved = false;
        for (UserRequest ur : requests) {
            if (ur.getEventId() == eventId && ur.getUsername().equalsIgnoreCase(user)) {
                ur.setResolved();
                resolved = true;
            }
        }
        return resolved;
    }

    /**
     * Creates a new instance of UserRequest
     * @param eventId the id of the event associated with the request.
     * @param username the username of the user who submitted the request.
     * @param item a string representing the request itself.
     */
    protected void newUserRequest(Integer eventId, String username, String item) {
        UserRequest userRequest = new UserRequest(eventId, username, item);
        requests.add(userRequest);
    }

    /**
     * Creates a new instance of UserRequest specifically for file reading.
     * @param eventId the id of the event associated with the request.
     * @param username the username of the user who submitted the request.
     * @param item a string representing the request itself.
     * @param status a string representing the current status of the request.
     */
    protected void newUserRequest(Integer eventId, String username, String item, String status) {
        UserRequest userRequest = new UserRequest(eventId, username, item, status);
        requests.add(userRequest);
    }

    /**
     * Encapsulates all Request information into a String.
     * @return Returns the information of all Requests in this Request Handler's list, as one String.
     */
    public String requestWriter(){
        StringBuilder all_requests_ever = new StringBuilder();
        List<Map<String, String>> hm_requests = getRequestsInfo();
        for(Map<String, String> hmap: hm_requests){
            for (String value : hmap.values()){
                all_requests_ever.append(value);
                all_requests_ever.append("%%");
            }
            all_requests_ever.append("##");
        }
        return all_requests_ever.toString();
    }
}
