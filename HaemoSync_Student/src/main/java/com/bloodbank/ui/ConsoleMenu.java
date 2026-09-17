package com.bloodbank.ui;

import com.bloodbank.exception.*;
import com.bloodbank.model.*;
import com.bloodbank.service.BloodBankService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleMenu {
    private final BloodBankService service;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleMenu(BloodBankService service) {
        this.service = service;
    }

    public void start() {
        service.startExpiryChecker();
        System.out.println("\n=== HaemoSync Blood Bank ===");
        System.out.println("Simple command-line blood donation and inventory system.");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addDonor();
                    case "2" -> showDonors();
                    case "3" -> donation();
                    case "4" -> showStock();
                    case "5" -> requestBlood();
                    case "6" -> showRequests();
                    case "7" -> { int n = service.removeExpired(); System.out.println("Expired units removed: " + n); }
                    case "0" -> running = false;
                    default -> System.out.println("Please choose one of the listed options.");
                }
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
        scanner.close();
        System.out.println("Program closed.");
    }

    private void printMenu() {
        System.out.println("\n1. Register donor");
        System.out.println("2. View donors");
        System.out.println("3. Record blood donation");
        System.out.println("4. View blood stock");
        System.out.println("5. Request blood");
        System.out.println("6. View requests");
        System.out.println("7. Remove expired units");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }

    private void addDonor() {
        System.out.print("Donor ID: "); String id = scanner.nextLine();
        System.out.print("Name: "); String name = scanner.nextLine();
        System.out.print("Phone: "); String phone = scanner.nextLine();
        System.out.print("Age: "); int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Weight (kg): "); double weight = Double.parseDouble(scanner.nextLine());
        System.out.print("Blood group (A+, A-, B+, B-, AB+, AB-, O+, O-): ");
        BloodGroup group = BloodGroup.fromText(scanner.nextLine());

        service.registerDonor(new Donor(id, name, phone, age, weight, group));
        System.out.println("Donor registered.");
    }

    private void showDonors() {
        List<Donor> donors = service.donors();
        if (donors.isEmpty()) { System.out.println("No donors found."); return; }
        System.out.println("\nID | Name | Age | Weight | Group | Last donation");
        for (Donor d : donors)
            System.out.printf("%s | %s | %d | %.1f | %s | %s%n",
                    d.getId(), d.getName(), d.getAge(), d.getWeight(), d.getBloodGroup(),
                    d.getLastDonation() == null ? "-" : d.getLastDonation());
    }

    private void donation() throws DonorEligibilityException {
        System.out.print("Donor ID: "); String id = scanner.nextLine();
        System.out.print("Hemoglobin: "); double hb = Double.parseDouble(scanner.nextLine());
        System.out.print("Systolic BP: "); int sys = Integer.parseInt(scanner.nextLine());
        System.out.print("Diastolic BP: "); int dia = Integer.parseInt(scanner.nextLine());
        printComponents();
        ComponentType component = ComponentType.fromNumber(Integer.parseInt(scanner.nextLine()));

        BloodUnit unit = service.recordDonation(id, component, hb, sys, dia);
        System.out.println("Donation recorded. Unit ID: " + unit.getId());
        System.out.println("Expiry date: " + unit.getExpiresOn());
    }

    private void printComponents() {
        System.out.println("1. Whole Blood");
        System.out.println("2. Red Cells");
        System.out.println("3. Plasma");
        System.out.println("4. Platelets");
        System.out.print("Component: ");
    }

    private void showStock() {
        Map<BloodGroup, Long> stock = service.stockCount();
        System.out.println("\nBlood group | Available units");
        for (Map.Entry<BloodGroup, Long> entry : stock.entrySet())
            System.out.printf("%-11s | %d%n", entry.getKey(), entry.getValue());
    }

    private void requestBlood() throws StockException {
        System.out.print("Patient name: "); String patient = scanner.nextLine();
        System.out.print("Hospital: "); String hospital = scanner.nextLine();
        System.out.print("Required blood group: ");
        BloodGroup group = BloodGroup.fromText(scanner.nextLine());
        printComponents();
        ComponentType component = ComponentType.fromNumber(Integer.parseInt(scanner.nextLine()));
        System.out.print("Number of units: "); int units = Integer.parseInt(scanner.nextLine());
        System.out.println("1. Emergency  2. Urgent  3. Routine");
        System.out.print("Priority: ");
        RequestPriority priority = RequestPriority.fromNumber(Integer.parseInt(scanner.nextLine()));

        String path = service.fulfill(patient, hospital, group, component, units, priority);
        System.out.println("Request fulfilled.");
        System.out.println("Dispatch file: " + path);
    }

    private void showRequests() {
        List<String> rows = service.requests();
        if (rows.isEmpty()) { System.out.println("No requests found."); return; }
        System.out.println("\nID | Patient | Hospital | Group | Component | Units | Priority | Status");
        rows.forEach(System.out::println);
    }
}
