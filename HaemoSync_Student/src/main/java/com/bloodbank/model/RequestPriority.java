package com.bloodbank.model;

public enum RequestPriority {
    EMERGENCY(1), URGENT(2), ROUTINE(3);

    private final int number;
    RequestPriority(int number) { this.number = number; }
    public int number() { return number; }

    public static RequestPriority fromNumber(int n) {
        for (RequestPriority p : values()) if (p.number == n) return p;
        throw new IllegalArgumentException("Choose 1, 2 or 3.");
    }
}
