package usersfeature;

import java.util.*;

/** Represents a Speaker.
 * @author Manav Shah, Muhammad Sohaib Saqib
 */
public class Speaker extends User{

    HashSet<Integer> talks;

    /**
     * Constructs an instance of Speaker.
     * @param name A string representing a name..
     * @param username A string representing a username.
     */
    public Speaker(String name, String username){
        super(name, username);
        this.talks = new HashSet<>();
    }

    /**
     * Constructs an instance of speaker.
     * @param data A string representing the stored data.
     */
    public Speaker(String data){
        super(data);
        this.talks = new HashSet<>();
        this.speakerReader(data);
    }

    /**
     * Encodes the data of the Speaker.
     * @return A String representing the stored data of the Speaker.
     */
    public String objectEncoder() {
        StringBuilder file = new StringBuilder(super.objectEncoder());

        String talks;
        if(!this.talks.isEmpty()) {
            StringBuilder f = new StringBuilder();
            for (int t : this.talks) {
                String s = "##" + t;
                f.append(s);
            }
            talks = f.substring(2);
        }else{
            talks = "";
        }

        file.append("-").append(talks);
        return file.toString();
    }

    /**
     * Decodes the data of the Speaker.
     * @param data A string representing the stored data.
     */
    public void speakerReader(String data) {
        if (data != null && data.length() > 0) {
            ArrayList<String> lists = new ArrayList<>(Arrays.asList(data.split("-")));
            ArrayList<String> events = new ArrayList<>(Arrays.asList(lists.get(5).split("##")));
            if(!events.isEmpty() && !events.get(0).equals("")) {
                for (String event: events){
                    this.talks.add(Integer.parseInt(event));
                }
            }
            else{
                this.talks = new HashSet<>();
            }
        }
    }


    /**
     * Represents the name and username of the Speaker as a String.
     * @return A String representing the Speaker's information.
     */
    public String toString(){
        return "Speaker Name: " + this.name + ", Username: " + this.username;
    }

    /**
     * Gets a list of Events that the Speaker will be talking at.
     * @return An ArrayList representing unique event Ids as ints.
     */
    public ArrayList<Integer> getTalks() {
        return new ArrayList<>(this.talks);
    }


    /**
     * Deletes an Event that the Speaker will be talking at from the talks HashSet.
     * @param event An int representing the unique ID for an Event.
     */
    public void deleteTalk(int event){
        if (!talks.contains(event)){
            return;
        }
        this.talks.remove(event);
    }

    /**
     * adds an Event that the Speaker will be talking at
     * @param event An int representing the unique ID for an Event.
     */
    public void addTalk(int event){
        this.talks.add(event);
    }

}
