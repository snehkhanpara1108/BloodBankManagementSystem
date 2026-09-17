package com.bloodbank.model;

import java.time.LocalDate;

public class Donor extends Person {
    private final int age;
    private final double weight;
    private final BloodGroup bloodGroup;
    private LocalDate lastDonation;

    public Donor(String id, String name, String phone, int age,
                 double weight, BloodGroup bloodGroup) {
        super(id, name, phone);
        if (age < 1 || weight <= 0 || bloodGroup == null)
            throw new IllegalArgumentException("Invalid donor details.");
        this.age = age;
        this.weight = weight;
        this.bloodGroup = bloodGroup;
    }

    @Override
    public String getType() { return "Donor"; }

    public int getAge() { return age; }
    public double getWeight() { return weight; }
    public BloodGroup getBloodGroup() { return bloodGroup; }
    public LocalDate getLastDonation() { return lastDonation; }
    public void setLastDonation(LocalDate date) { this.lastDonation = date; }
}
