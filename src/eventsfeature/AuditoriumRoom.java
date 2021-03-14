package eventsfeature;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an Auditorium version of a Room. An Auditorium has a set number of seats, (i.e. the capacity of an event
 * cannot be more than the room's capacity. The room's features include chairs, projectors, and computers.
 * @author Kevin Cecco, Daniel Chan, William Wang
 */
public class AuditoriumRoom extends Room {

    /**
     * Constructs an instance of AuditoriumRoom.
     * @param roomNum The room number of this AuditoriumRoom.
     * @param capacity The capacity of this AuditoriumRoom.
     */
    protected AuditoriumRoom(int roomNum, int capacity) {
        super(roomNum, capacity, new HashSet<>(Arrays.asList("rows of seating", "chairs", "projectors", "computers")));
    }

    /**
     * Gets the code for this AuditoriumRoom. AuditoriumRooms are identified by "AU" in their code.
     * @return The code for this Room.
     */
    @Override
    protected String getRoomCode(){
        return "AU"+getRoomNum();
    }

    /**
     * Checks whether this AuditoriumRoom can hold an event, based on a set of requirements and a capacity.
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
        // auditorium rooms cannot take on any more than the capacity
        return capacity <= getCapacity();
    }

}
