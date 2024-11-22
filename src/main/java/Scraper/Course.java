package Scraper;
import java.util.ArrayList;

public class Course {
    // General information
    private String CRN;
    private String subject;
    private String courseNumber;
    private String section;
    private String sectionTitle;
    private String instructor;
    private String description;

    // "Quick facts" information
    private String seatsLeft;
    private String schedule;
    private String delivery;
    private String term;
    private String genEdType;
    private String waitList;
    private String repeatable;

    // Meeting information
    private ArrayList<MeetingInfo> meetingInfos;

    // Constructor
    public Course(String CRN, String subject, String courseNumber, String section, String sectionTitle, String instructor, String description, String seatsLeft, String schedule, String delivery, String term, String genEdType, String waitList, String repeatable, ArrayList<MeetingInfo> meetingInfos) {
        this.CRN = CRN;
        this.subject = subject;
        this.courseNumber = courseNumber;
        this.section = section;
        this.sectionTitle = sectionTitle;
        this.instructor = instructor;
        this.description = description;
        this.seatsLeft = seatsLeft;
        this.schedule = schedule;
        this.delivery = delivery;
        this.term = term;
        this.genEdType = genEdType;
        this.waitList = waitList;
        this.repeatable = repeatable;
        this.meetingInfos = meetingInfos; // Note that meetingInfos is an ArrayList of MeetingInfo objects
    }

    // Getters
    public String getCRN() { return CRN; }
    public String getSubject() { return subject; }
    public String getCourseNumber() { return courseNumber; }
    public String getSection() { return section; }
    public String getSectionTitle() { return sectionTitle; }
    public String getInstructor() { return instructor; }
    public String getDescription() { return description; }
    public String getSeatsLeft() { return seatsLeft; }
    public String getSchedule() { return schedule; }
    public String getDelivery() { return delivery; }
    public String getTerm() { return term; }
    public String getGenEdType() { return genEdType; }
    public String getWaitList() { return waitList; }
    public String getRepeatable() { return repeatable; }
    public ArrayList<MeetingInfo> getMeetingInfos() { return meetingInfos; }

    // ToString
    public String toString() {
        String meetingInfoString = "";
        for (MeetingInfo meetingInfo : meetingInfos) {
            meetingInfoString += meetingInfo.toString() + "\n";
        }
        return "CRN: " + CRN + "\nSubject: " + subject + "\nCourse Number: " + courseNumber + "\nSection: " + section + "\nSection Title: " + sectionTitle + "\nInstructor: " + instructor + "\nDescription: " + description + "\nSeats Left: " + seatsLeft + "\nSchedule: " + schedule + "\nDelivery: " + delivery + "\nTerm: " + term + "\nGenEd Type: " + genEdType + "\nWait List: " + waitList + "\nRepeatable: " + repeatable + "\nMeeting Information:\n" + meetingInfoString;
    }
}
