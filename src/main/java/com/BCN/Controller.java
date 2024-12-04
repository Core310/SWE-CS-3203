package com.BCN;

import ScheduleGenerator.Schedule;
import ScheduleGenerator.ScheduleGenerator;
import Scraper.Scraper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Scraper.*;

import java.util.ArrayList;


@RestController
@RequestMapping("/")
public class Controller {
    /**
     * @param requestedItem String format item searching for
     * @return json format in java map style.
     */
    @GetMapping("/singleItem/{requestedItem}")
    public ResponseEntity<Object> requestItem(@PathVariable String requestedItem) throws JsonProcessingException {
        String[] CRNs = requestedItem.split(",");

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

        // Generating and printing schedules
        ArrayList<Schedule> schedules = ScheduleGenerator.generateSchedules(courses);
        for (Schedule schedule : schedules) {
            schedule.arrangeEvents();
            System.out.println(schedule);
        }

        return ResponseEntity.ok(schedules.toString());
    }
    }

