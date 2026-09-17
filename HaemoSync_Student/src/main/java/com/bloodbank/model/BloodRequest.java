package com.bloodbank.model;

import java.time.LocalDateTime;

public class BloodRequest {
    private final String id;
    private final String patient;
    private final String hospital;
    private final BloodGroup group;
    private final ComponentType component;
    private final int units;
    private final RequestPriority priority;
    private final LocalDateTime createdAt;
    private String status;

    public BloodRequest(String id, String patient, String hospital, BloodGroup group,
                        ComponentType component, int units, RequestPriority priority) {
        if (units <= 0) throw new IllegalArgumentException("Units must be greater than zero.");
        this.id = id;
        this.patient = patient;
        this.hospital = hospital;
        this.group = group;
        this.component = component;
        this.units = units;
        this.priority = priority;
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    public String getId() { return id; }
    public String getPatient() { return patient; }
    public String getHospital() { return hospital; }
    public BloodGroup getGroup() { return group; }
    public ComponentType getComponent() { return component; }
    public int getUnits() { return units; }
    public RequestPriority getPriority() { return priority; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
