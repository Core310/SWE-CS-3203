package ScheduleGenerator;

import Scraper.Course;
import Scraper.MeetingInfo;

/**
 * Represents a schedule for a student
 * Contains a Day object for each day of the week, as well as an "other" day for asynchronous courses
 */
public class Schedule {
    private Day monday;
    private Day tuesday;
    private Day wednesday;
    private Day thursday;
    private Day friday;
    private Day other;
    private int numCourses; // Used to find best schedule (i.e. the one with the most courses)

    /**
     * Constructor for Schedule
     */
    public Schedule() {
        this.monday = new Day();
        this.tuesday = new Day();
        this.wednesday = new Day();
        this.thursday = new Day();
        this.friday = new Day();
        this.other = new Day();
        this.numCourses = 0;
    }

    /**
     * Adds a course to the schedule
     * @param course
     * @return true if the course was successfully added, false if there was a conflict
     */
    public boolean addCourse(Course course) {
        Event event = new Event(course);

        // If the course is asynchronous online, add it to the async day
        if (event.getStartTime() == -1) {
            other.addEvent(event);
            return true;
        }

        // Get the days the course meets
        MeetingInfo meetingInfo = course.getMeetingInfos().get(0);
        String days = meetingInfo.getDays();

        // Add the event to the schedule for each day it meets
        boolean conflict = false;
        for (int i = 0; i < days.length(); i++) {
            char day = days.charAt(i);
            switch (day) {
                case 'M':
                    if (!monday.addEvent(event)) {
                        conflict = true;
                    }
                    break;
                case 'T':
                    if (!tuesday.addEvent(event)) {
                        conflict = true;
                    }
                    break;
                case 'W':
                    if (!wednesday.addEvent(event)) {
                        conflict = true;
                    }
                    break;
                case 'R':
                    if (!thursday.addEvent(event)) {
                        conflict = true;
                    }
                    break;
                case 'F':
                    if (!friday.addEvent(event)) {
                        conflict = true;
                    }
                    break;
                default:
                    System.out.println("Invalid day: " + day);
                    return false;
            }

            // If there is a conflict, remove the event from all days it was added to
            if (conflict) {
                monday.removeEvent(event);
                tuesday.removeEvent(event);
                wednesday.removeEvent(event);
                thursday.removeEvent(event);
                friday.removeEvent(event);
                return false;
            }
        }
        numCourses++;
        return true;
    }

    /**
     * Arranges the events in each day in order of start time
     */
    public void arrangeEvents() {
        monday.arrangeEvents();
        tuesday.arrangeEvents();
        wednesday.arrangeEvents();
        thursday.arrangeEvents();
        friday.arrangeEvents();
        other.arrangeEvents();
    }


    // Getters
    public Day getMonday() { return monday; }
    public Day getTuesday() { return tuesday; }
    public Day getWednesday() { return wednesday; }
    public Day getThursday() { return thursday; }
    public Day getFriday() { return friday; }
    public Day getAsync() { return other; }
    public int getNumCourses() { return numCourses; }

    // toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Monday:\n").append(monday).append("\n");
        sb.append("Tuesday:\n").append(tuesday).append("\n");
        sb.append("Wednesday:\n").append(wednesday).append("\n");
        sb.append("Thursday:\n").append(thursday).append("\n");
        sb.append("Friday:\n").append(friday).append("\n");
        sb.append("Other:\n").append(other).append("\n");
        return sb.toString();
    }
}
