package ScheduleGenerator;

import Scraper.Course;

import java.util.ArrayList;
import java.util.List;


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
        // Generate all possible permutations of the courses
        System.out.println("Generating combinations...");
        ArrayList<ArrayList<Integer>> combinations = getPermutations(courses.size());
        int combinationsSize = combinations.size();
        int increment = combinationsSize / 10;
        System.out.println(combinationsSize + " combinations generated.");
        System.out.println();

        System.out.println("Generating schedules...");
        int maxCourses = 0;
        int counter = 0;
        ArrayList<Schedule> schedules = new ArrayList<>();
        for (ArrayList<Integer> combination : combinations) {
            counter++;
            if (counter % increment == 0) {
                System.out.println((counter / increment) * 10 + "% complete.");
            }
            Schedule schedule = new Schedule();
            // Use combinations as pointers to the courses list
            for (int i : combination) {
                schedule.addCourse(courses.get(i));
            }
            if (schedule.getNumCourses() > maxCourses) {
                maxCourses = schedule.getNumCourses();
            }
            // Remove the schedule if it has fewer courses than the current max as we go
            if (schedule.getNumCourses() < maxCourses) {
                continue;
            }
            schedules.add(schedule);
        }
        // Removing schedules with fewer courses than the final max
        for (Schedule schedule : schedules) {
            if (schedule.getNumCourses() < maxCourses) {
                schedules.remove(schedule);
            }
        }
        System.out.println();
        return schedules;
    }


    /**
     * Generates all permutations of integers from 1 to size
     * @param size
     * @return
     */
    public static ArrayList<ArrayList<Integer>> getPermutations(int size) {
        ArrayList<Integer> nums = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            nums.add(i);
        }
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        backtrack(result, new ArrayList<>(), nums);
        return result;
    }
    private static void backtrack(List<ArrayList<Integer>> result, ArrayList<Integer> temp, ArrayList<Integer> nums) {
        if (temp.size() == nums.size()) {
            result.add(new ArrayList<>(temp)); // Add a copy of the current permutation
            return;
        }

        for (int i = 0; i < nums.size(); i++) {
            if (temp.contains(nums.get(i))) continue; // Skip already used elements
            temp.add(nums.get(i));
            backtrack(result, temp, nums);
            temp.remove(temp.size() - 1); // Backtrack
        }
    }
    
}
