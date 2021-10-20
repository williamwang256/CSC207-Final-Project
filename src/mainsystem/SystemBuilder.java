package mainsystem;

import eventsfeature.EventHandler;
import eventsfeature.EventSystem;
import requestsfeature.RequestSystem;
import requestsfeature.RequestHandler;
import messagesfeature.MessageHandler;
import messagesfeature.MessageSystem;
import usersfeature.UserHandler;
import usersfeature.UserSystem;

import java.io.File;

/**
 * A builder class responsible for building the Controllers .
 */
public class SystemBuilder {

    private final MessageHandler messageHandler;
    private final UserHandler userHandler;
    private final EventHandler eventHandler;
    private final RequestHandler requestHandler;

    private final MessageSystem messageSystem;
    private final UserSystem userSystem;
    private final EventSystem eventSystem;
    private final RequestSystem requestSystem;
    private final Persistence persistence;

    private static final String userPath = "filedata" +  File.separator + "userstext.txt";
    private static final String eventsPath = "filedata" +  File.separator + "eventstext.txt";
    private static final String messagesPath = "filedata" + File.separator + "messagestext.txt";
    private static final String roomsPath = "filedata" + File.separator + "roomstext.txt";
    private static final String requestsPath = "filedata" + File.separator + "requeststext.txt";

    /**
     * Responsible for instantiating all relevant Use Cases, Controllers etc.
     */
    public SystemBuilder(){
        this.persistence = new Persistence();
        this.messageHandler = new MessageHandler();
        this.requestHandler = new RequestHandler();
        this.eventHandler = new EventHandler();
        this.userHandler = new UserHandler(persistence.readStringFromPath(userPath));

        this.messageSystem = new MessageSystem(persistence.readStringFromPath(messagesPath),
                userHandler,
                eventHandler,
                messageHandler);
        this.eventSystem = new EventSystem(persistence.readStringFromPath(eventsPath),
                persistence.readStringFromPath(roomsPath),
                userHandler,
                eventHandler);

        this.userSystem = new UserSystem(userHandler);
        this.requestSystem = new RequestSystem(persistence.readStringFromPath(requestsPath),userHandler, requestHandler);
    }

    /**
     * Saves all changes made during session.
     */
    public void saveAll(){
        persistence.saveDataToPath(userPath, userHandler.ObjectEncoder());
        persistence.saveDataToPath(roomsPath, eventHandler.roomWriter());
        persistence.saveDataToPath(eventsPath, eventHandler.eventWriter());
        persistence.saveDataToPath(messagesPath, messageHandler.fileWriter());
        persistence.saveDataToPath(requestsPath, requestHandler.requestWriter());
    }

    /**
     * returns the stored MessageSystem object.
     * @return MessageSystem object
     */
    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    /**
     * returns the stored EventSystem object.
     * @return EventSystem object
     */
    public EventSystem getEventSystem() {
        return eventSystem;
    }

    /**
     * returns a the stored UserSystem object.
     * @return UserSystem object
     */
    public UserSystem getUserSystem() {
        return userSystem;
    }

    /**
     * returns a the stored RequestSystem object.
     * @return RequestSystem object
     */
    public RequestSystem getRequestSystem() {
        return requestSystem;
    }

}
