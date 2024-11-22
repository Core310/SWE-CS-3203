package ScheduleGenerator;
import Scraper.Course;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event on a given day, has a name, start time, and end time
 */
public class Event {
    private String name;
    private int startTime;
    private int endTime;

    // Manual constructor
    public Event(String name, int startTime, int endTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Constructor via Course object
    public Event(Course course) {
        this.name = course.getSubject() + " " + course.getCourseNumber() + " | " + course.getSectionTitle();
        // If days are not available, set the start and end times to -1
        if (course.getMeetingInfos().get(0).getDays().equals("Days not available")) {
            this.startTime = -1;
            this.endTime = -1;
            return;
        }

        int[] times = convertTimes(course.getMeetingInfos().get(0).getTimes());
        this.startTime = times[0];
        this.endTime = times[1];
    }

    // Getters
    public String getName() { return name; }
    public int getStartTime() { return startTime; }
    public int getEndTime() { return endTime; }

    // Helper method to convert time strings to integer format
    public int[] convertTimes(String input) {
        int[] result = new int[2];
        String[] times = input.split(" - ");

        // Define the time format (e.g., "h:mm a" for 12-hour clock with am/pm)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        int i = 0;
        for (String time : times) {
            // Parse the input time string into a LocalTime object
            LocalTime localTime = LocalTime.parse(time.toUpperCase(), formatter);

            // Convert the LocalTime to an integer in HHMM format
            int hour = localTime.getHour();
            int minute = localTime.getMinute();

            result[i] = hour * 100 + minute;
            i++;
        }
        return result;
    }
}
