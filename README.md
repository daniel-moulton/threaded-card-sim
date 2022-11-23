## Multi-Threaded Card Playing Simulation
### READ ME

https://trello.com/b/jrJgzcUv/software-development

https://docs.google.com/document/d/1EHDBRzF_yYuABc4__mT5wmKOGnLPbOQV/edit?usp=sharing&ouid=102621577467213799338&rtpof=true&sd=true


# How to run our test suite:

We are using JUnit 4.13.2.

To run our test suite, extract the cardsTest.zip into a new folder. Navigate to that folder via the command line. Within the folder, depending on your OS, execute the following command:

Linux/Mac
- java -cp "lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:." test.JUnitTestRunner 

Windows
- java -cp "lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;." test.JUnitTestRunner