# CSC207-Final-Project

** To run the program, run the main method inside RunSystem.java **

Login / Sign up system:
- When you first run the program, you must signup if you wish to be an Organizer or an Attendee.
- Once you have signed up, you can subsequently sign in as either an Attendee, Speaker or Organizer to access the
  Main Menu.
- In the Main Menu you have options to access four sub-menus:
    1. View Profile
    2. Message
    3. Events
    4. Contact

Message System:
- When you arrive at the message menu, you can either view a conversation or begin a conversation
- If you choose to view a conversation, you are shown the convo ids of all conversations that you are a part of.
- When you enter one of the ids, you can see the history of the entire conversation and you can either send a
  message to it or reply to a message in it.
- If you choose to begin a new conversation, you are presented with different options based on your user type
  (i.e. attendee vs organizer vs speaker).
- Attendees can message any number of users that are not organizers.
- Organizers can message some or all attendees and speakers.
- Speakers can message any attendees or all attendees that attend their events.

eventsfeature.Event System:
- If you choose to view events, you can choose to view information for one specific existing event, all existing events,
  or just the events you’re currently signed up for.
- If you choose to sign up or leave an event, you can then input the room number and event id for the event in question.
- If you’re an Organizer, you can also create and cancel events in addition to the previous options.
- If you choose to create, you can further choose to create a new room, event, or Speaker.
- To cancel an event, you must input an existing room number and event id.
- By file reading and writing to the events text file, we can save and read event information in the form of strings.
