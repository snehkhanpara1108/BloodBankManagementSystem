# Project Report
## HaemoSync - Blood Bank Management System

### 1. Introduction

HaemoSync is a Java command-line application made for managing basic blood-bank records. The application keeps donor details, donated blood units, and hospital requests in a local SQLite database.

The idea behind the project is straightforward: a blood bank needs to know who donated, what units are available, which units are close to expiry, and whether a requested blood group has a compatible unit.

The project combines these tasks with the Java topics taught in the course.

---

## 2. Objectives

The main objectives are:

- create a working Java application that can be run from a terminal
- store information using JDBC and SQLite
- demonstrate object-oriented programming
- use collections for managing blood units
- handle invalid input with exceptions
- use file I/O for logs and dispatch notes
- demonstrate a basic use of multithreading
- write tests for important pieces of logic

---

## 3. Functional Requirements

### 3.1 Donor registration

The system accepts:

- donor ID
- name
- phone number
- age
- weight
- blood group

A donor ID cannot be registered twice.

### 3.2 Donor eligibility

Before a donation is recorded, the program checks:

- age between 18 and 65
- weight of at least 50 kg
- hemoglobin of at least 12.5 g/dL
- systolic pressure between 90 and 180
- diastolic pressure between 50 and 100
- a 90-day gap from the previous recorded donation

These values are project rules for the academic simulation and are not a substitute for professional medical screening.

### 3.3 Blood inventory

Every donation creates a blood unit with:

- unit ID
- blood group
- component type
- collection date
- calculated expiry date
- donor ID
- current status

The status can be `AVAILABLE`, `DISPATCHED`, or `EXPIRED`.

### 3.4 Blood request

A request contains:

- patient name
- hospital
- required blood group
- component
- number of units
- priority

The service searches available compatible units. The list is sorted by expiry date and the earlier-expiring units are selected first.

### 3.5 File output

The program creates:

- `logs/activity.log`
- `reports/dispatch_<request-id>.txt`

These files are simple text records intended to demonstrate Java file I/O.

---

## 4. Design

The program uses a small layered structure.

```text
ConsoleMenu
     |
     v
BloodBankService
     |
     +------> DonorDao
     |
     +------> BloodUnitDao
     |
     +------> RequestDao
                 |
                 v
             SQLite DB
```

The model classes hold the application's data. DAO classes handle database access. The service class contains the main rules, and the console class handles user input.

---

## 5. OOP Design

`Person` is an abstract class containing common donor information. `Donor` extends it.

`Identifiable` provides a small interface containing `getId()`.

Enums are used where a value should come from a fixed set:

- `BloodGroup`
- `ComponentType`
- `RequestPriority`
- `BloodUnit.Status`

Private fields and public methods provide encapsulation.

`BloodUnit` implements `Comparable<BloodUnit>`. Its comparison is based on the expiry date. This gives a direct example of how an object can define its natural ordering.

---

## 6. Collections

Several Java collections are used.

### List

Lists are used when returning multiple donors or blood units.

### Map

An `EnumMap<BloodGroup, Long>` is used to display stock counts for each blood group.

### PriorityQueue

The tests use a `PriorityQueue<BloodUnit>`. Since `BloodUnit` implements `Comparable`, the unit with the earliest expiry date is returned first.

This is the project's FEFO (First-Expire, First-Out) example.

---

## 7. Exception Handling

Two custom checked exceptions are used:

- `DonorEligibilityException`
- `StockException`

For example, an invalid donor screening result stops the donation process and displays a useful message instead of allowing an invalid donation into the inventory.

Database and file errors are also caught and converted into readable application errors where appropriate.

---

## 8. File I/O

`FileUtil` handles two simple file operations.

### Activity log

Each important action is appended to:

```text
logs/activity.log
```

### Dispatch note

When a blood request is fulfilled, a text file is created under:

```text
reports/
```

The file contains the request details and the selected blood-unit IDs.

---

## 9. JDBC and SQLite

The project uses the Xerial SQLite JDBC driver.

There are three database tables:

```text
donors
blood_units
requests
```

The database is created automatically when the application starts.

Prepared statements are used for values supplied by the user. This also keeps the SQL code easier to read.

Because SQLite is embedded, the user does not need to install or start MySQL, WAMP, or XAMPP.

---

## 10. Concurrency

The application contains a `ReentrantLock` in the service class. The lock protects the part of the program where donations and blood allocations modify stock.

A daemon thread periodically checks for expired units. Since it is a daemon thread, it does not keep the program alive after the main application exits.

The concurrency part is intentionally small because the goal is to demonstrate the Java concept rather than build a production multi-user server.

---

## 11. Testing

The project contains three test classes.

### CompatibilityTest

Checks:

- O- can donate to all groups in the simplified model
- positive blood cannot be given to a negative group in the tested cases
- A+ has four compatible donor groups

### DonorServiceTest

Checks:

- a valid donor passes
- an underweight donor is rejected
- low hemoglobin is rejected

### InventoryTest

Checks that a blood unit with an earlier expiry date is returned first by a `PriorityQueue`.

Run all tests with:

```powershell
mvn test
```

---

## 12. Command-line execution

The complete workflow is:

```powershell
mvn clean compile
mvn test
mvn exec:java
```

The program does not require a graphical interface.

The SQLite database is created automatically in the project directory.

---

## 13. Example workflow

A simple demonstration can be done in this order:

1. register a donor
2. record a valid donation
3. view stock
4. create a request for a compatible blood group
5. observe the selected unit
6. check the generated dispatch file
7. view the request list

This sequence demonstrates the main parts of the application without requiring any additional software.

---

## 14. Limitations

This is an academic project, not a real clinical system.

It does not include:

- user authentication
- a web interface
- hospital network integration
- real patient records
- real laboratory testing
- complete clinical transfusion rules
- real cold-chain monitoring hardware

The compatibility logic and donor checks are simplified rules used to demonstrate programming concepts.

---

## 15. Course Mapping

| Course topic | Where it appears |
|---|---|
| Java classes and objects | All model and service classes |
| Inheritance | `Donor extends Person` |
| Abstraction | `Person` |
| Interfaces | `Identifiable` |
| Encapsulation | Private model fields with methods |
| Enums | `BloodGroup`, `ComponentType`, `RequestPriority` |
| Exceptions | `DonorEligibilityException`, `StockException` |
| File I/O | `FileUtil` |
| Collections | `List`, `Map`, `PriorityQueue` |
| Generics | Java collection and DAO types |
| Comparable | `BloodUnit implements Comparable` |
| JDBC | DAO classes and `Database` |
| Multithreading | expiry checker daemon thread |
| Synchronization | `ReentrantLock` |
| Testing | JUnit 5 |

---

## 16. Conclusion

HaemoSync brings several Java concepts together in one small application. The project is deliberately kept understandable: the user interface is a terminal menu, the database is a local SQLite file, and the main business rules are placed in one service class.

The most useful part of the project from a learning point of view is the connection between the requirements and the Java features. Blood-unit expiry gives a practical reason for `Comparable` and `PriorityQueue`, donor validation gives a reason for custom exceptions, and the database gives a practical example of JDBC.

The final application can be compiled, tested, and run from the command line.
