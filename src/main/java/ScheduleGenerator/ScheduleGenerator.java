package ScheduleGenerator;

import Scraper.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;


/**
 *  Generates schedules based on the courses provided.
 *  The logic for this needs to be redone; generating all permutations of courses is not feasible.
 *  We should systematically check for conflicts, and only generate as many schedules as is needed to compensate for that.
 */
public class ScheduleGenerator {
    /**
     * Generates best schedules based on the courses provided
     * @param courses
     * @return
     */
    public static ArrayList<Schedule> generateSchedules(ArrayList<Course> courses) {
        // First check if there are any conflicts by adding all courses to a schedule
        Schedule schedule = new Schedule();
        for (Course course : courses) {
            schedule.addCourse(course);
        }
        // If there were no conflicts, return the schedule
        if (schedule.getNumCourses() == courses.size()) {
            schedule.arrangeEvents();
            ArrayList<Schedule> schedules = new ArrayList<>();
            schedules.add(schedule);
            return schedules;
        }

        // Find the conflicts, and add each pair of conflicting courses to a list
        System.out.println("Checking for conflicts...");
        ArrayList<Course[]> conflicts = new ArrayList<>();
        Schedule temp;
        for (int i = 0; i < courses.size(); i++) {
            Course c1 = courses.get(i);
            temp = new Schedule();
            temp.addCourse(c1);
            for (int j = 0; j < courses.size(); j++) {
                Course c2 = courses.get(j);
                if (c1 == c2) { continue; }
                // Get meeting info for each course
                MeetingInfo meetingInfo1 = c1.getMeetingInfos().get(0);
                MeetingInfo meetingInfo2 = c2.getMeetingInfos().get(0);
                String days1 = meetingInfo1.getDays();
                String days2 = meetingInfo2.getDays();
                String times1 = meetingInfo1.getTimes();
                String times2 = meetingInfo2.getTimes();

                // If times overlap and days are the same, there is a conflict
                if (times1.equals(times2)) {
                    for (int k = 0; k < days1.length(); k++) {
                        if (days2.contains(Character.toString(days1.charAt(k)))) {
                            conflicts.add(new Course[]{c1, c2});
                            break;
                        }
                    }
                }
            }
        }
        System.out.println(conflicts.size() + " conflict(s) found.");
        System.out.println();

        // Remove duplicates from the conflicts list
        System.out.println("Removing duplicates...");
        for (Iterator<Course[]> iterator = conflicts.iterator(); iterator.hasNext(); ) {
            Course[] conflict = iterator.next();
            Course c1 = conflict[0];
            Course c2 = conflict[1];
        
            for (Course[] otherConflict : conflicts) {
                if (otherConflict[0] == c2 && otherConflict[1] == c1) {
                    iterator.remove();
                    break;
                }
            }
        }
        System.out.println(conflicts.size() + " conflict(s) found.");
        System.out.println();

        // For each pair of conflicting courses, generate a schedule with only one of the courses
        System.out.println("Generating schedules...");
        ArrayList<Schedule> schedules = new ArrayList<>();
        for (Course[] conflict : conflicts) {
            for (Course skip : conflict) {
                schedule = new Schedule();
                for (Course course : courses) {
                    if (course != skip) {
                        schedule.addCourse(course);
                    }
                }
                schedule.arrangeEvents();
                schedules.add(schedule);
            }
        }
        System.out.println(schedules.size() + " schedules generated.");
        System.out.println();
        return schedules;
    }
    
}
