import java.util.ArrayList;

import ScheduleGenerator.*;
import Scraper.*;

public class Driver {
    public static void main(String[] args) {
        // List of CRNs to lookup, could also use Subject and Course Number
        String[] CRNs = {"13594", "13657", "13625", "14139", "14142", "14147", "33500", "34574", "38345", "38346", "38522"};

        // Initializing Scraper
        // Defaults to Fall 2024
        Scraper scraper = new Scraper();

        // Storing courses in a list
        ArrayList<Course> courses = new ArrayList<>();
        int counter = 1;
        for (String CRN : CRNs) {
            System.out.println("Fetching course (" + counter + "/" + CRNs.length + ")...");
            ++counter;
            Course course = scraper.getCourse(CRN);
            courses.add(course);
        }
        System.out.println();

        // Generating and printing schedules
        ArrayList<Schedule> schedules = ScheduleGenerator.generateSchedules(courses);
        for (Schedule schedule : schedules) {
            schedule.arrangeEvents();
            System.out.println(schedule);
        }

        // Closing the scraper
        scraper.close();
    }
}
