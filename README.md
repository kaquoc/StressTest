# StressTest

Java project to stress test machine processors using various methods.

### Project Goal
- Revise Java Multithread learn in CS330
- Understand the Java Virtual Machine and its approaches to threads
- Practice documentation


### Documentation Notes
These are the notes relating to the project.

Documentation.md is another documentation markdown containing information about the M1 processor 
architecture and overall information about multithreading.

Source:
https://www.infoworld.com/article/2074481/java-101--understanding-java-threads--part-4---thread-groups--volatility--and-threa.html

To simplify Thread management, Java organizes threads into ThreadGroup. Similar to how all 
objects are inheritance from the main Object class, Java requires every Thread and ThreadGroup to 
be apart of some other ThreadGroup (except for the root System ThreadGroup).

At the top of Java Thread management structure is - **system**. Java Virtual Machine created 
**system**
group to organize all JVM threads.

Below **system** is the JVM-create **main** ThreadGroup, which is **system** subgroup.

**main** contains at least one thread created by JVM to execute intructions in the main() method.

Below **main** is all the application created thread-subgroup.


