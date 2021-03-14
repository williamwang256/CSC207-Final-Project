package eventsfeature;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an ConventionCentre version of a Room. A ConventionCentre has a maximum room capacity, however events held
 * in this room may not exceed 10 times this capacity. This is to allow Conventions to be held, where not every attendee
 * is expected to show up at the same time. The room's features include tables, booths, and projectors.
 * @author Kevin Cecco, Daniel Chan, William Wang
 */
public class ConventionCentreRoom extends Room {

    /**
     * Constructs an instance of ConventionCentre.
     * @param roomNum The room number of this ConventionCentre.
     * @param capacity The capacity of this ConventionCentre.
     */
    protected ConventionCentreRoom(int roomNum, int capacity) {
        super(roomNum, capacity, new HashSet<>(Arrays.asList("tables", "booths", "projectors")));
    }

    /**
     * Gets the code for this ConventionCentre. ConventionCentres are identified by "CC" in their code.
     * @return The code for this Room.
     */
    @Override
    protected String getRoomCode(){
        return "CC"+getRoomNum();
    }

    /**
     * Checks whether this ConventionCentre can hold an event, based on a set of requirements and a capacity.
     * @param requirements Given requirements.
     * @param capacity The capacity of the event.
     * @return Whether or not all the requirements are in the room's list, and the capacity is no more than the room can
     * hold at once.
     */
    @Override
    protected boolean checkRequirements(Set<String> requirements, int capacity) {
        for (String s : requirements) {
            if (!getFeatures().contains(s))
                return false;
        }
        // convention centre rooms can take on events with more people that can fit in at once.
        return capacity <= 10*getCapacity();
    }

}
