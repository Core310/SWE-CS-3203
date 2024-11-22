import java.util.ArrayList;

import ScheduleGenerator.*;
import Scraper.*;

public class Driver {
    public static void main(String[] args) {
        // List of CRNs to lookup, could also use Subject and Course Number
        // SWEG, PPL, DBMS, Comp Org, Linear
        String[] CRNs = {"38346", "42906", "14142", "38345", "17886"};

        // Initializing Scraper
        // Defaults to Fall 2024
        Scraper scraper = new Scraper();

        // Storing courses in a list
        ArrayList<Course> courses = new ArrayList<>();
        for (String CRN : CRNs) {
            Course course = scraper.getCourse(CRN);
            courses.add(course);
        }

        // Adding courses to a schedule
        Schedule schedule = new Schedule();
        for (Course course : courses) {
            schedule.addCourse(course);
        }

        // Printing the schedule
        schedule.arrangeEvents();
        System.out.println(schedule);

        // Closing the scraper
        scraper.close();
    }
}
