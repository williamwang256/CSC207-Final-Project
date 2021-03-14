package eventsfeature;

import java.util.Arrays;

/**
 * A room factory for constructing different types of rooms.
 * @author Kevin Cecco, Daniel Chan, William Wang
 */
public class RoomFactory {

    private static final String[] ROOM_TYPES = {"auditorium", "convention", "classroom"};

    /**
     * The method to be called to generate a room object.
     * @param roomType The type of room to make.
     * @param roomNum The room number of the new room.
     * @param capacity The capacity of the new room.
     * @return The new room object.
     */
    protected Room getNewRoom(String roomType, int roomNum, int capacity) {
        if (roomType.equalsIgnoreCase("auditorium"))
            return getAuditoriumRoom(roomNum, capacity);
        else if (roomType.equalsIgnoreCase("convention"))
            return getConventionRoom(roomNum, capacity);
        else if (roomType.equalsIgnoreCase("classroom"))
            return getClassroom(roomNum, capacity);
        throw new InvalidEventFieldsException("unrecognized room type");
    }

    /**
     * A getter method to let others know what rooms can be created.
     * @return A string representation of all available room types.
     */
    protected String getRoomTypes() {
        return Arrays.toString(ROOM_TYPES);
    }

    /**
     * Creates an instance of AuditoriumRoom.
     * @param roomNum The room number, as an integer.
     * @param capacity The capacity, as an integer.
     * @return An instance of AuditoriumRoom, as a Room.
     */
    private Room getAuditoriumRoom(int roomNum, int capacity) {
        return new AuditoriumRoom(roomNum, capacity);
    }

    /**
     * Creates an instance of ConventionCentreRoom.
     * @param roomNum The room number, as an integer.
     * @param capacity The capacity, as an integer.
     * @return An instance of ConventionCentreRoom, as a Room.
     */
    private Room getConventionRoom(int roomNum, int capacity) {
        return new ConventionCentreRoom(roomNum, capacity);
    }

    /**
     * Creates an instance of Classroom.
     * @param roomNum The room number, as an integer.
     * @param capacity The capacity, as an integer.
     * @return An instance of Classroom, as a Room.
     */
    private Room getClassroom(int roomNum, int capacity) {
        return new Classroom(roomNum, capacity);
    }

}
