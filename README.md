# About
BetterClassNav is a web service that eases a student’s process of signing up for multiple
classes. Unlike the current classNav, searching only requires students to input all class IDs or
names. The output produces a list of class sections, where no timings collide. This contrasts
with the current classNav forcing students to find all the class timings manually and create a
schedule from there leading to a much smoother signup process for students and thus resulting
in slightly reduced workloads for academic advisors.

## How it works:
Each `class` object is stored as below, where each avaliable Course is pulled up. After calling all COURSES, the program will return all possible permutations of all courses, and you can pick which one fits your schedule best.  
`MeetingDates,MeetingTimes,MeetingDays,Location,Crn,Subject,Course,Section,SectionTitle,Instructor,Seats,Waitlist`
OR 
```json
  {
    "MeetingDates": "value1",
    "MeetingTimes": "value2",
    "MeetingDays": "value3",
    "Location": "value4",
    "Crn": "value5",
    "Subject": "value6",
    "Course": "value7",
    "Section": "value8",
    "SectionTitle": "value9",
    "Instructor": "value10",
    "Seats": "value11",
    "Waitlist": "value12"
  },

```

# Development & Contribution:
For those approved to contribute, per PAIR, create a new branch named after the feature being implemented (eg. branch name = "webscrapping").
After you're done, send in a PR with what you have done and I'll approve it or tell you what needs to be done. 


Thank you for your interest in contributing to this project! Sadly, we are not accepting any right now without good reason. Please create an issue for us to review and we will get back to you on PR approval.
<!-- Contributing is greatly appreciated!  Simply create a fork and propose your merges. Kidnly indicate if there are new dependencies added to your branch, and make small objective focused PRs. If you are creating a large refactor, please contact me and I will list it under updated forked projects.  -->

## Dependencies:
- [me.xdrop:fuzzywuzzy GPL-2](https://github.com/xdrop/fuzzywuzzy) for fuzzy search.
- [jsoup MIT](https://jsoup.org/) for web scrapping.
- [junit EPL-2](https://github.com/junit-team/junit5) for testing.
- [projectlombok](https://projectlombok.org/setup/maven)


## Preferred Styling & Branch Rules
- [Use .NET Framework Design Guideline](https://learn.microsoft.com/en-us/dotnet/standard/design-guidelines/)
- No more than 3 layers of abstraction 
- Only rebase with permission
- Document methods using javaDoc for ease of viewing
- Please please name your variables well
- Ask before performing more than 4 layers of abstraction


## Todos: 
- Localization?
- Host on website?
- Setup CD/CI 
- Set tests
