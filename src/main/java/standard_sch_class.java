/**
 * Create new std class object based off of school classes in classNav for readibility ease

 *   All values should be read in as STRINGS
 */
public class standard_sch_class {
    private String meetingDates;
    private String meetingTimes;
    private String meetingDays;
    private String location;
    private String crn;
    private String subject;
    private String course;
    private String section;
    private String sectionTitle;
    private String instructor;
    private String seats;
    private String waitlist;

    public standard_sch_class(String meetingDates, String meetingTimes, String meetingDays, String location, String crn, String subject, String course, String section, String sectionTitle, String instructor, String seats, String waitlist) {
        this.meetingDates = meetingDates;
        this.meetingTimes = meetingTimes;
        this.meetingDays = meetingDays;
        this.location = location;
        this.crn = crn;
        this.subject = subject;
        this.course = course;
        this.section = section;
        this.sectionTitle = sectionTitle;
        this.instructor = instructor;
        this.seats = seats;
        this.waitlist = waitlist;
    }

// Getters
public String getMeetingDates() {
    return meetingDates;
}

public String getMeetingTimes() {
    return meetingTimes;
}

public String getMeetingDays() {
    return meetingDays;
}

public String getLocation() {
    return location;
}

public String getCrn() {
    return crn;
}

public String getSubject() {
    return subject;
}

public String getCourse() {
    return course;
}

public String getSection() {
    return section;
}

public String getSectionTitle() {
    return sectionTitle;
}

public String getInstructor() {
    return instructor;
}

public String getSeats() {
    return seats;
}

public String getWaitlist() {
    return waitlist;
}
}
