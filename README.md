# Twitter Challenge

This project replicates the mono repo twitter uses, so the source code for all projects as well as the 
Approval tool sits in the same code base. The project uses gradle as a build manager.

**Usage:**

    ./buildDependencyGraph.sh
    ./validate_approvals.sh -approvers app1 app2 -changed-files path/to/file1 path/to/file2

*** Also please remember that the bash scripts only run on bash linux machines

if you are using any other operating system with prerequisites already installed follow the 3 steps below:

    gradle build
    gradle run
    python src/main/python/Challenge.py -approvers app1 app2 -changed-files path/to/file1 path/to/file2


The `buildDependencyGraph.sh` script will run gradle build and gradle run. The twitter challenge program which is 
present in src/main/java/Reader is configured to run via gradle. This need not be run for every commit, this can script 
can be run only when we add a new package in our code base.
 
The `validate_approvals` script takes in the approvers and the relative path to the files changed

**Prerequisites:**

Make sure you have the following installed before running the application

* `Java 1.8` 
* `python 3.6` 
* `gradle 4.1`


