/**
 * Create new std class object based off of school classes in classNav for readibility ease

 *   All values should be read in as STRINGS
 */
public class standard_sch_class {
    @lombok.Getter
    private final String meetingDates;@lombok.Getter
    private final String meetingTimes;@lombok.Getter
    private final String meetingDays;@lombok.Getter
    private final String location;@lombok.Getter
    private final String crn;@lombok.Getter
    private final String subject;@lombok.Getter
    private final String course;@lombok.Getter
    private final String section;@lombok.Getter
    private final String sectionTitle;@lombok.Getter
    private final String instructor;@lombok.Getter
    private final String seats;@lombok.Getter
    private final String waitlist;

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
}
