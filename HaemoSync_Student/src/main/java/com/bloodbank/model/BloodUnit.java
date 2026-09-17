package com.bloodbank.model;

import java.time.LocalDate;

public class BloodUnit implements Comparable<BloodUnit> {
    public enum Status { AVAILABLE, DISPATCHED, EXPIRED }

    private final String id;
    private final BloodGroup group;
    private final ComponentType component;
    private final LocalDate collectedOn;
    private final LocalDate expiresOn;
    private final String donorId;
    private Status status;

    public BloodUnit(String id, BloodGroup group, ComponentType component,
                     LocalDate collectedOn, String donorId) {
        this.id = id;
        this.group = group;
        this.component = component;
        this.collectedOn = collectedOn;
        this.expiresOn = collectedOn.plusDays(component.shelfLifeDays());
        this.donorId = donorId;
        this.status = Status.AVAILABLE;
    }

    public String getId() { return id; }
    public BloodGroup getGroup() { return group; }
    public ComponentType getComponent() { return component; }
    public LocalDate getCollectedOn() { return collectedOn; }
    public LocalDate getExpiresOn() { return expiresOn; }
    public String getDonorId() { return donorId; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public boolean isExpired() { return expiresOn.isBefore(LocalDate.now()); }

    @Override
    public int compareTo(BloodUnit other) {
        return this.expiresOn.compareTo(other.expiresOn);
    }
}
