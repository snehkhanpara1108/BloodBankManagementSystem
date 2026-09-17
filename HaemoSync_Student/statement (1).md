# Project Statement

## Project Title

**HaemoSync - Blood Bank Management System**

## Problem Statement

Managing blood donation and stock information using paper records or separate files can make it difficult to keep track of available blood units. It can also be easy to overlook expired units or spend unnecessary time checking whether a blood group is compatible with a request.

This project was developed as a small Java application that brings these basic blood bank tasks together in one place. It is designed to be simple enough to run through a terminal while also demonstrating the Java concepts covered in the course.

## Proposed Solution

**HaemoSync** is a command-line Blood Bank Management System that stores donor, blood-unit, and hospital-request information in an SQLite database. The application provides a simple way to manage the main blood bank operations from the terminal.

### High-Level Features

The application allows the user to:

1. Register donors.
2. Check donor eligibility before recording a donation.
3. Add donated blood to the inventory.
4. Check the current number of available blood units.
5. Submit a blood request.
6. Find compatible units for a requested blood group.
7. Select units with the earliest expiry date first.
8. Mark selected units as dispatched.
9. Remove expired blood units.
10. View request information.
11. Create a simple dispatch note.
12. Maintain an activity log.

## Java Concepts Demonstrated

The project brings together several concepts covered in the Programming in Java course:

- Classes and objects
- Constructors and methods
- Encapsulation
- Inheritance
- Abstraction
- Interfaces
- Enums
- Exception handling
- File input/output
- Collections
- `PriorityQueue`
- Generics
- `Comparable`
- JDBC
- SQLite
- Threads and a daemon thread
- `ReentrantLock`

## Scope of the Project

This application is intended mainly for academic demonstration. Its scope is focused on managing donor and blood inventory information rather than providing a complete hospital blood-bank system.

The blood compatibility rules used in the application are simplified for educational purposes. They are not intended to be used for actual medical or blood transfusion decisions.

## Target Users

The project is designed for users who need to demonstrate or work with basic blood bank management operations in an academic setting. It can be used to enter sample donor and blood-unit information, manage inventory, process blood requests, and observe how the different Java concepts work together.

## Expected Result

After completing the project, a user should be able to open a terminal, compile and run the program, enter sample information, and see that the information is saved in the local SQLite database.

The application should also generate simple text files for activity logging and successful blood dispatches.

## Why This Project Is Useful for the Course

The main purpose of this project is to connect different Java concepts through one practical example instead of demonstrating each concept separately.

For example, the blood-unit expiry requirement provides a practical use for `Comparable` and `PriorityQueue`. Similarly, donor eligibility checks provide a reason to use custom exceptions. The project therefore shows how the Java concepts from the course can be applied together in a single application.

The command-line interface also makes the application straightforward to demonstrate without depending on a graphical application.
