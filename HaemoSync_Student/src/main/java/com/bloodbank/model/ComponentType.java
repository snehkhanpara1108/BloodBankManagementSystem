package com.bloodbank.model;

public enum ComponentType {
    WHOLE_BLOOD("Whole Blood", 35),
    RED_CELLS("Red Cells", 42),
    PLASMA("Plasma", 365),
    PLATELETS("Platelets", 5);

    private final String label;
    private final int shelfLifeDays;

    ComponentType(String label, int shelfLifeDays) {
        this.label = label;
        this.shelfLifeDays = shelfLifeDays;
    }

    public String label() { return label; }
    public int shelfLifeDays() { return shelfLifeDays; }

    public static ComponentType fromNumber(int n) {
        ComponentType[] values = values();
        if (n < 1 || n > values.length) throw new IllegalArgumentException("Choose a valid component.");
        return values[n - 1];
    }
}
