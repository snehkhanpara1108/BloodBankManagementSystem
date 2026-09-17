# BloodBankManagementSystem

## 1. What this project does

HaemoSync is a small command-line Java project for managing basic blood bank activities. I designed it around a few operations that are easy to demonstrate from a terminal:

- register a donor
- check whether a donor meets the project rules before a donation
- record a donated blood unit
- view available stock
- receive a blood request from a hospital
- select compatible blood units
- use the unit with the earliest expiry date first
- mark dispatched and expired units
- save simple activity logs and dispatch notes

This is an academic simulation. It is not intended to replace software used in an actual hospital.

## 2. Java concepts used

The project was kept simple enough to explain in a viva.

### Module 1 - Java basics and execution
- JDK and JVM
- packages
- classes and objects
- Maven compile/run commands

### Module 2 - Object-oriented programming
- `Person` is an abstract class.
- `Donor` extends `Person`.
- `Identifiable` is an interface.
- `BloodGroup`, `ComponentType`, and `RequestPriority` are enums.
- private fields and methods are used for encapsulation.
- `BloodUnit` implements `Comparable` so units can be sorted by expiry date.

### Module 3 - Exceptions and file handling
- custom checked exceptions are used for donor eligibility and stock problems.
- `try-with-resources` is used with JDBC and file writers.
- activity information is written to `logs/activity.log`.
- successful dispatches are written to `reports/`.

### Module 4 - Collections
- `List` stores groups of donors and blood units.
- `Map` is used for stock counts.
- `PriorityQueue` demonstrates FEFO ordering.
- `ArrayList` and `EnumMap` are used in the services.

### Module 5 - JDBC and concurrency
- SQLite is used as the local database.
- JDBC `PreparedStatement` is used for database operations.
- a `ReentrantLock` protects the donation/allocation section.
- a small daemon thread checks for expired blood units in the background.

## 3. Project structure

```text
BloodBankManagementSystem/
├── pom.xml
├── README.md
├── statement.md
├── PROJECT_REPORT.md
├── src/
│   ├── main/java/com/bloodbank/
│   │   ├── Main.java
│   │   ├── model/
│   │   ├── dao/
│   │   ├── exception/
│   │   ├── service/
│   │   ├── util/
│   │   └── ui/
│   └── test/java/com/bloodbank/
│       ├── CompatibilityTest.java
│       ├── DonorServiceTest.java
│       └── InventoryTest.java
├── logs/
└── reports/
```

The `logs`, `reports`, and `bloodbank.db` files are created when the program is used. They do not need to be prepared manually.

## 4. Requirements

Install:

1. JDK 17 or newer
2. Apache Maven 3.9 or newer

Check them with:

```powershell
java -version
mvn -version
```

No WAMP/XAMPP/MySQL installation is needed. The project uses SQLite through the JDBC driver, so the database is kept in the project folder.

## 5. Run from the command line

Open PowerShell in the project folder.

```powershell
cd C:\path	o\BloodBankManagementSystem
```

Compile:

```powershell
mvn clean compile
```

Run tests:

```powershell
mvn test
```

Start the application:

```powershell
mvn exec:java
```

The first run creates `bloodbank.db` automatically.

## 6. Typical use

### Register a donor

Choose:

```text
1. Register donor
```

Enter an ID, name, phone number, age, weight, and blood group.

### Record a donation

Choose:

```text
3. Record blood donation
```

The program asks for the donor ID and a few screening values. The project applies the rules written in `statement.md`.

### Check stock

Choose:

```text
4. View blood stock
```

The count is shown by blood group.

### Request blood

Choose:

```text
5. Request blood
```

The program looks for available units with the requested component and a compatible blood group. Matching units are sorted by expiry date so the earlier-expiring unit is selected first.

A dispatch note is created inside `reports/`.

## 7. Database

The database contains three main tables:

- `donors`
- `blood_units`
- `requests`

The database is created by `Database.setup()`. There is no separate SQL import step.

This was done deliberately so that the project can be run from a normal terminal without depending on a separate database server.

## 8. Testing

Run:

```powershell
mvn test
```

The tests check:

- basic blood-group compatibility
- Rh-positive to Rh-negative restrictions
- FEFO ordering using `PriorityQueue`
- donor weight and hemoglobin validation

The tests are small on purpose. Each one checks a particular piece of project logic instead of trying to test the whole application at once.

## 9. Limitations

This project is meant for learning Java. It does not model every medical rule used by real blood banks. In particular, the compatibility logic is a simplified educational model and should not be used for real transfusion decisions.

There is also no login system, web interface, network server, or external hospital integration.

## 10. Main learning outcome

The main purpose of the project is to show how several Java topics can be combined into one usable console application:

```text
Console UI
    ↓
Service / business rules
    ↓
DAO classes
    ↓
SQLite database
```

The project can therefore be compiled, tested, and demonstrated entirely from the command line.

## Student Information

**Student:** SNEH ATULBHAI KHANPARA  
**Registration No.:** 25BAI10397  
**Branch:** CSE — Artificial Intelligence & Machine Learning  
**Institution:** VIT Bhopal

