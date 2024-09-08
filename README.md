<div style="text-align: right"> <strong> Group B </strong> </div>
<div style="text-align: right"> Luke Chey, Masiko Mamba, Arika Khor, Emily Locklear, Daniela Sanchez, Terriauna James</div>

# Project overview (Arika)
BetterClassNav is a web service that eases a student’s process of signing up for multiple
classes. Unlike the current classNav, searching only requires students to input all class IDs or
names. The output produces a list of class sections, where no timings collide. This contrasts
with the current classNav forcing students to find all the class timings manually and create a
schedule from there leading to a much smoother signup process for students and thus resulting
in slightly reduced workloads for academic advisors.

# Git, and its use in the project 
## Branching 
We will use this feature to allow each of us to work on our separate copies of a file in the project simultaneously without the dangers of our individual work being overwritten. This feature will warn us that changes were made by another team member. Which will then lead us to communicate what changes were made and why and then together we will re-edit the file to successfully submit to the master branch. 
## Squashing (Terriauna) 
We will use this feature to rewrite our commit history and combine multiple commits into a single commit to help make things more readable and easier to understand. We will make the changes and then submit it to the parent commit.  
## Rebasing (Luke) 
Rebasing is a process in version control systems used to integrate changes from one branch onto another. It works by taking a series of commits from a branch and replaying them onto another base branch, effectively changing the history of the branch being rebased. Rebasing is similar to merging in that they are both ways to integrate changes from one branch into another, but they do so in different ways, each with distinct effects on the branch history. When you merge two branches in Git, a new "merge commit" that ties the two branches together is created. This commit represents the moment when the histories of both branches were joined. On the other hand, when rebasing, the commit history is rewritten. Instead of showing the branch point, it applies the commits of one branch on top of another, as if one branch had always been based on the current state of the other. The result is a linear history, with no merge commit, as if the two branches never diverged. 

While most casual Git users are more familiar with merging, it may be beneficial for us to focus on rebasing when possible, leading to a clearer and less branching commit history. This would be helpful when errors arise, and certain versions of the codebase need to be tracked down to either look at or roll back to. 
## Merging (Daniela) 
Merging is the process of combining two or more separate code branches into a single branch. Using this will allow us to work on different parts of the codebase without interfering with each other. New features or bug fixes developed in separate branches can be integrated into the main codebase. When a developer is ready to integrate any changes, they submit a merge request which is reviewed by other team members to ensure it meets the quality standards. 

This is a fundamental operation in Git that is used to incorporate new features, fix bugs, or experiment code into the main development branch. If there are no conflicting changes between the two branches, Git can perform a fast-forward merge, simply moving the main branch’s pointer to the latest commit on the new-feature branch. If there are conflicts, Git will create a new commit that represents the merged changes.

___
# Features: 
- Works on all systems (built through docker)
- Scrapes data through the OU class nav system (no admin permissions required)
- Outputs data in raw JSON 
- Fully open sourced
- Easily export the requested data in multiple formats (CSV/raw JSON/TSV)

# How it works
- Data is pulled from OU class nav through jsoup, and thrown into a JSON file. 
- A JSON parser is then used to normalize all the data into a readable class 
- The user inputs their preferred classes, and a caller class outputs all the possible times
- The times are then outputted from a REST API

# Dependencies 
- [me.xdrop:fuzzywuzzy](https://github.com/xdrop/fuzzywuzzy) for fuzzy search.
- [jsoup](https://jsoup.org/) for web scrapping.
- [junit](https://github.com/junit-team/junit5) for testing.


<div style="text-align: right"> <strong> Group B </strong> </div>

<div style="text-align: right"> Luke Chey, Masiko Mamba, Arika Khor, Emily Locklear, Daniela Sanchez, Terriauna James</div>
