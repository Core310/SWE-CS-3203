import java.sql.*;
import java.util.*;

class ClassSchedule {
    private String courseName;
    private String instructor;
    private String meetingTimes;
    private String meetingDays;
    private String location;

    public ClassSchedule(String courseName, String instructor, String meetingTimes, String meetingDays, String location) {
        this.courseName = courseName;
        this.instructor = instructor;
        this.meetingTimes = meetingTimes;
        this.meetingDays = meetingDays;
        this.location = location;
    }

    public boolean conflictsWith(ClassSchedule other) {
        // Check for overlapping days
        for (char day : this.meetingDays.toCharArray()) {
            if (other.meetingDays.indexOf(day) != -1) { // Same day exists
                if (this.meetingTimes.equals("Times not available") || other.meetingTimes.equals("Times not available")) {
                    continue;
                }

                String[] thisTimes = this.meetingTimes.replace(" am", "").replace(" pm", "").split("-");
                String[] otherTimes = other.meetingTimes.replace(" am", "").replace(" pm", "").split("-");
                try {
                    Time thisStart = parseTime(thisTimes[0]);
                    Time thisEnd = parseTime(thisTimes[1]);
                    Time otherStart = parseTime(otherTimes[0]);
                    Time otherEnd = parseTime(otherTimes[1]);

                    if (!(thisEnd.before(otherStart) || thisStart.after(otherEnd))) {
                        return true; // Conflict found
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid time format: " + e.getMessage());
                }
            }
        }
        return false;
    }

    private Time parseTime(String timeStr) {
        boolean isPM = timeStr.toLowerCase().contains("pm");
        String[] parts = timeStr.trim().replace(" am", "").replace(" pm", "").split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;

        if (isPM && hours != 12) hours += 12;
        if (!isPM && hours == 12) hours = 0;

        return Time.valueOf(String.format("%02d:%02d:00", hours, minutes));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ClassSchedule other = (ClassSchedule) obj;
        return courseName.equals(other.courseName) &&
               instructor.equals(other.instructor) &&
               meetingTimes.equals(other.meetingTimes) &&
               meetingDays.equals(other.meetingDays) &&
               location.equals(other.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseName, instructor, meetingTimes, meetingDays, location);
    }

    @Override
    public String toString() {
        return String.format("%s: %s (%s) with %s at %s",
                courseName, meetingTimes, meetingDays, instructor, location);
    }
}

public class ScheduleGenerator {
    private static final String DB_URL = "jdbc:mysql://35.222.162.189:3306/classes?useSSL=false";
    private static final String DB_USER = "masiko";
    private static final String DB_PASSWORD = "";

    public static List<List<ClassSchedule>> generateSchedules(String semester, List<String> desiredCourses) throws SQLException {
        List<List<ClassSchedule>> schedules = new ArrayList<>();
        Set<List<ClassSchedule>> potentialSchedules = new HashSet<>();
        Map<String, List<ClassSchedule>> courseOptions = new HashMap<>();

        String tableName = semester.replaceAll("\\s+", " ");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, tableName, null);

            if (!tables.next()) {
                throw new SQLException("Table for the specified semester does not exist: " + tableName);
            }

            for (String courseName : desiredCourses) {
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT DISTINCT * FROM " + tableName + " WHERE `course name` LIKE ? AND `seats left` > 0");
                stmt.setString(1, "%" + courseName + "%");
                ResultSet rs = stmt.executeQuery();

                List<ClassSchedule> options = new ArrayList<>();
                while (rs.next()) {
                    ClassSchedule schedule = new ClassSchedule(
                            rs.getString("course name"),
                            rs.getString("instructor"),
                            rs.getString("meeting times"),
                            rs.getString("meeting days"),
                            rs.getString("location")
                    );
                    if (!options.contains(schedule)) {
                        options.add(schedule);
                    }
                }
                courseOptions.put(courseName, options);
            }
        }

        generateSchedulesRecursive(new ArrayList<>(), desiredCourses, 0, courseOptions, potentialSchedules);

        for (List<ClassSchedule> schedule : potentialSchedules) {
            if (isValidSchedule(schedule)) {
                schedules.add(schedule);
            }
        }

        return schedules;
    }

    private static void generateSchedulesRecursive(List<ClassSchedule> currentSchedule, List<String> desiredCourses,
                                                   int index, Map<String, List<ClassSchedule>> courseOptions,
                                                   Set<List<ClassSchedule>> potentialSchedules) {
        if (index == desiredCourses.size()) {
            potentialSchedules.add(new ArrayList<>(currentSchedule));
            return;
        }

        String currentCourse = desiredCourses.get(index);
        List<ClassSchedule> options = courseOptions.getOrDefault(currentCourse, new ArrayList<>());

        if (options.isEmpty()) {
            currentSchedule.add(new ClassSchedule(currentCourse, "Unknown Instructor", "Times not available", "N/A", "N/A"));
            generateSchedulesRecursive(currentSchedule, desiredCourses, index + 1, courseOptions, potentialSchedules);
            currentSchedule.remove(currentSchedule.size() - 1);
        } else {
            for (ClassSchedule option : options) {
                currentSchedule.add(option);
                generateSchedulesRecursive(currentSchedule, desiredCourses, index + 1, courseOptions, potentialSchedules);
                currentSchedule.remove(currentSchedule.size() - 1);
            }
        }
    }

    private static boolean isValidSchedule(List<ClassSchedule> schedule) {
        for (int i = 0; i < schedule.size(); i++) {
            for (int j = i + 1; j < schedule.size(); j++) {
                if (schedule.get(i).conflictsWith(schedule.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter the semester (e.g., 'Summer 2025'):");
            String semester = scanner.nextLine().trim();

            System.out.println("Enter the courses you want (comma-separated):");
            String[] courseNames = scanner.nextLine().split(",");
            List<String> desiredCourses = new ArrayList<>();
            for (String courseName : courseNames) {
                desiredCourses.add(courseName.trim());
            }

            List<List<ClassSchedule>> schedules = generateSchedules(semester, desiredCourses);
            if (schedules.isEmpty()) {
                System.out.println("No valid schedules found for " + semester + ".");
            } else {
                System.out.println("Generated schedules for " + semester + ":");
                for (int i = 0; i < schedules.size(); i++) {
                    System.out.println("Schedule " + (i + 1) + ":");
                    for (ClassSchedule cs : schedules.get(i)) {
                        System.out.println("  " + cs);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
