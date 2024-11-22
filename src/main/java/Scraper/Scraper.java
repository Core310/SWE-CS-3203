package Scraper;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

// Uses selenium to scrape the OU ClassNav website and create Course objects
// Download chromedriver at https://googlechromelabs.github.io/chrome-for-testing/
public class Scraper {
    private WebDriver driver;
    private String URL;

    // Initialize the Scraper with a URL
    public Scraper(String URL) {
        // Set the path to the chromedriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        this.URL = URL;
        driver.get(URL);
        System.out.println();
    }

    // Initialize the Scraper with a default URL (OU ClassNav Fall 2024)
    public Scraper() {
        // Set the path to the chromedriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        this.URL = "https://classnav.ou.edu/#semester/202410";
        driver.get(URL);
        System.out.println();
    }

    // Close the driver
    public void close() {
        driver.quit();
    }

    // Getters
    public WebDriver getDriver() { return driver; }
    public String getURL() { return URL; }

    // Course scraper using CRN
    // Returns course object with all information
    public Course getCourse(String searchCRN) {
        try {
            // Create a WebDriverWait for up to 10 seconds
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // Wait for the search bar to become visible
            WebElement searchBar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@aria-controls='clist']")));

            // Enter the search information
            searchBar.clear();
            searchBar.sendKeys(searchCRN);

            // Find and click the expand button
            WebElement expandButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#clist > tbody > tr:nth-child(1) > td:nth-child(1) > span.ui-icon.ui-icon-circle-plus.ui-opacity")));
            expandButton.click();

            // Wait for seats and waitlist to become visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Facts > table > tbody > tr:nth-child(1) > td:nth-child(2) > span > span")));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Facts > table > tbody > tr:nth-child(2) > td:nth-child(6) > span > span")));

            // Getting general information
            String CRN = driver.findElement(By.cssSelector("#clist > tbody > tr.odd > td:nth-child(2)")).getText();
            String subject = driver.findElement(By.cssSelector("#clist > tbody > tr.odd > td:nth-child(3)")).getText();
            String courseNumber = driver.findElement(By.cssSelector("#clist > tbody > tr.odd > td:nth-child(4)")).getText();
            String section = driver.findElement(By.cssSelector("#clist > tbody > tr.odd > td:nth-child(5)")).getText();
            String sectionTitle = driver.findElement(By.cssSelector("#clist > tbody > tr.odd > td:nth-child(6)")).getText();
            String instructor = driver.findElement(By.cssSelector("#clist > tbody > tr.odd > td:nth-child(7)")).getText();
            String description = driver.findElement(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.SubTablePanel.Narrative > div > p:nth-child(4)")).getText();

            // Getting "Quick facts" information
            String seatsLeft = driver.findElement(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Facts > table > tbody > tr:nth-child(1) > td:nth-child(2) > span > span")).getText();
            String schedule = driver.findElement(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Facts > table > tbody > tr:nth-child(1) > td:nth-child(4)")).getText();
            String delivery = driver.findElement(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Facts > table > tbody > tr:nth-child(1) > td:nth-child(6)")).getText();
            String term = driver.findElement(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Facts > table > tbody > tr:nth-child(2) > td:nth-child(2)")).getText();
            String genEdType = driver.findElement(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Facts > table > tbody > tr:nth-child(2) > td:nth-child(4)")).getText();
            String waitList = driver.findElement(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Facts > table > tbody > tr:nth-child(2) > td:nth-child(6) > span > span")).getText();
            String repeatable = driver.findElement(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Facts > table > tbody > tr:nth-child(3) > td:nth-child(2)")).getText();

            // Getting meeting information
            ArrayList<MeetingInfo> meetingInfos = new ArrayList<MeetingInfo>();
            WebElement meetingInfoTable = driver.findElement(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Meeting > div > table"));

            // Get all <tbody> elements in the table
            List<WebElement> tbodyElements = meetingInfoTable.findElements(By.tagName("tbody"));

            // Iterate over each <tbody>
            for (WebElement tbody : tbodyElements) {
                // Get all rows
                List<WebElement> rows = tbody.findElements(By.tagName("tr"));

                // Iterate over each row
                for (WebElement row : rows) {
                    // Get all columns
                    List<WebElement> columns = row.findElements(By.tagName("td"));

                    // Get the text from each column
                    String dates = columns.get(0).getText();
                    String times = columns.get(1).getText();
                    String location = columns.get(2).getText();
                    String days = columns.get(3).getText();

                    // Create a MeetingInfo object and add it to the list
                    MeetingInfo meetingInfo = new MeetingInfo(dates, times, location, days);
                    meetingInfos.add(meetingInfo);
                }
            }
            
            // Create a Course object and return it
            Course course = new Course(CRN, subject, courseNumber, section, sectionTitle, instructor, description, seatsLeft, schedule, delivery, term, genEdType, waitList, repeatable, meetingInfos);
            return course;

        } catch (Exception e) {
            e.printStackTrace();
            driver.quit();
        }
        return null;

    }

    // Course scraper using subject and course number
    public Course getCourse(String searchSubject, String searchCourseNumber) {
        try {
            String subjectCode = searchSubject.replace(" ", "%20");
            driver.get(URL + "/subject_code/" + subjectCode);

            // Create a WebDriverWait for up to 10 seconds
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // Wait for the search bar to become visible
            WebElement searchBar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@aria-controls='clist']")));

            // Enter the search information
            searchBar.clear();
            searchBar.sendKeys(searchCourseNumber);

            // Find and click the expand button
            WebElement expandButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#clist > tbody > tr:nth-child(1) > td:nth-child(1) > span.ui-icon.ui-icon-circle-plus.ui-opacity")));
            expandButton.click();

            // Wait for seats and waitlist to become visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Facts > table > tbody > tr:nth-child(1) > td:nth-child(2) > span > span")));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Facts > table > tbody > tr:nth-child(2) > td:nth-child(6) > span > span")));

            // Getting general information
            String CRN = driver.findElement(By.cssSelector("#clist > tbody > tr.odd > td:nth-child(2)")).getText();
            String subject = driver.findElement(By.cssSelector("#clist > tbody > tr.odd > td:nth-child(3)")).getText();
            String courseNumber = driver.findElement(By.cssSelector("#clist > tbody > tr.odd > td:nth-child(4)")).getText();
            String section = driver.findElement(By.cssSelector("#clist > tbody > tr.odd > td:nth-child(5)")).getText();
            String sectionTitle = driver.findElement(By.cssSelector("#clist > tbody > tr.odd > td:nth-child(6)")).getText();
            String instructor = driver.findElement(By.cssSelector("#clist > tbody > tr.odd > td:nth-child(7)")).getText();
            String description = driver.findElement(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.SubTablePanel.Narrative > div > p:nth-child(4)")).getText();

            // Getting "Quick facts" information
            String seatsLeft = driver.findElement(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Facts > table > tbody > tr:nth-child(1) > td:nth-child(2) > span > span")).getText();
            String schedule = driver.findElement(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Facts > table > tbody > tr:nth-child(1) > td:nth-child(4)")).getText();
            String delivery = driver.findElement(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Facts > table > tbody > tr:nth-child(1) > td:nth-child(6)")).getText();
            String term = driver.findElement(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Facts > table > tbody > tr:nth-child(2) > td:nth-child(2)")).getText();
            String genEdType = driver.findElement(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Facts > table > tbody > tr:nth-child(2) > td:nth-child(4)")).getText();
            String waitList = driver.findElement(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Facts > table > tbody > tr:nth-child(2) > td:nth-child(6) > span > span")).getText();
            String repeatable = driver.findElement(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Facts > table > tbody > tr:nth-child(3) > td:nth-child(2)")).getText();

            // Getting meeting information
            ArrayList<MeetingInfo> meetingInfos = new ArrayList<MeetingInfo>();
            WebElement meetingInfoTable = driver.findElement(By.cssSelector("#clist > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(2) > td.FactsMeeting > div.SubTablePanel.Meeting > div > table"));

            // Get all <tbody> elements in the table
            List<WebElement> tbodyElements = meetingInfoTable.findElements(By.tagName("tbody"));

            // Iterate over each <tbody>
            for (WebElement tbody : tbodyElements) {
                // Get all rows
                List<WebElement> rows = tbody.findElements(By.tagName("tr"));

                // Iterate over each row
                for (WebElement row : rows) {
                    // Get all columns
                    List<WebElement> columns = row.findElements(By.tagName("td"));

                    // Get the text from each column
                    String dates = columns.get(0).getText();
                    String times = columns.get(1).getText();
                    String location = columns.get(2).getText();
                    String days = columns.get(3).getText();

                    // Create a MeetingInfo object and add it to the list
                    MeetingInfo meetingInfo = new MeetingInfo(dates, times, location, days);
                    meetingInfos.add(meetingInfo);
                }
            }
            
            // Create a Course object and return it
            Course course = new Course(CRN, subject, courseNumber, section, sectionTitle, instructor, description, seatsLeft, schedule, delivery, term, genEdType, waitList, repeatable, meetingInfos);
            return course;

        } catch (Exception e) {
            e.printStackTrace();
            driver.quit();
        }
        return null;

    }
}