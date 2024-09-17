# About
BetterClassNav is a web service that eases a student’s process of signing up for multiple
classes. Unlike the current classNav, searching only requires students to input all class IDs or
names. The output produces a list of class sections, where no timings collide. This contrasts
with the current classNav forcing students to find all the class timings manually and create a
schedule from there leading to a much smoother signup process for students and thus resulting
in slightly reduced workloads for academic advisors.

# How it works:
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

## Dependencies:
- [me.xdrop:fuzzywuzzy](https://github.com/xdrop/fuzzywuzzy) for fuzzy search.
- [jsoup](https://jsoup.org/) for web scrapping.
- [junit](https://github.com/junit-team/junit5) for testing.

## Development:
Contributing is greatly appreciated! Simply create a fork and propose your merges. Kidnly indicate if there are new dependencies added to your branch, 
and make small objective focused PRs. 
If you are creating a large refactor, please contact me and I will list it under updated forked projects. 

## Todos: 
- Localization?
- Host on website?
- 
