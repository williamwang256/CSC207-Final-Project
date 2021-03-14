package eventsfeature;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an Classroom version of a Room. A Classroom has a set number of seats, (i.e. the capacity of an event
 * cannot be more than the room's capacity. The room's features include tables, chairs, projectors, and whiteboards.
 * @author Kevin Cecco, Daniel Chan, William Wang
 */
public class Classroom extends Room {

    /**
     * Constructs an instance of Classroom. A Classroom cannot have a capacity greater than 50, so if the input is
     * greater than 50, make a classroom with capacity equal to 50 (i.e. largest classroom possible).
     * @param roomNum The room number of this Classroom.
     * @param capacity The capacity of this Classroom.
     */
    protected Classroom(int roomNum, int capacity) {
        super(roomNum, Math.min(capacity, 50), new HashSet<>(Arrays.asList("tables", "chairs", "projectors", "whiteboards")));
    }

    /**
     * Gets the code for this Classroom. Classrooms are identified by "CL" in their code.
     * @return The code for this Room.
     */
    @Override
    protected String getRoomCode(){
        return "CL"+getRoomNum();
    }

    /**
     * Checks whether this Classroom can hold an event, based on a set of requirements and a capacity.
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
        // classrooms cannot take on any more than the capacity
        return capacity <= getCapacity();
    }

}
