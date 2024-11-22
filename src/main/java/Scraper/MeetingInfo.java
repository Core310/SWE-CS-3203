package Scraper;

public class MeetingInfo {
    private String dates;
    private String times;
    private String location;
    private String days;

    // Constructor
    public MeetingInfo(String dates, String times, String location, String days) {
        this.dates = dates;
        this.times = times;
        this.location = location;
        this.days = days;
    }

    // Getters
    public String getDates() { return dates; }
    public String getTimes() { return times; }
    public String getLocation() { return location; }
    public String getDays() { return days; }

    // ToString
    public String toString() {
        return "Dates: " + dates + "\nTimes: " + times + "\nLocation: " + location + "\nDays: " + days;
    }
}
