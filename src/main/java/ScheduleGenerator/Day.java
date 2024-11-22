package ScheduleGenerator;
import java.util.ArrayList;

public class Day {

    ArrayList<Event> events;

    // Constructor
    public Day() {
        this.events = new ArrayList<>();
    }

    // Adds an event to the day, returns false if there is a time conflict
    public boolean addEvent(Event event) {
        // Check if the event overlaps with any existing events
        for (Event existingEvent : events) {
            if (event.getStartTime() < existingEvent.getEndTime() && event.getEndTime() > existingEvent.getStartTime()) {
                return false;
            }
        }

        // If no overlap, add the event to the list
        events.add(event);
        return true;
    }

    // Removes an event from the day
    public void removeEvent(Event event) {
        events.remove(event);
    }

    public void arrangeEvents() {
        events.sort((e1, e2) -> e1.getStartTime() - e2.getStartTime());
    }

    // Getters
    public ArrayList<Event> getEvents() { return events; }

    // toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Event event : events) {
            sb.append(event.getName()).append(" (").append(event.getStartTime()).append(" - ").append(event.getEndTime()).append(")\n");
        }
        return sb.toString();
    }
    
}