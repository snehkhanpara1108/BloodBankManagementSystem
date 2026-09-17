package com.bloodbank.model;

import java.util.ArrayList;
import java.util.List;

public enum BloodGroup {
    O_NEG("O-"), O_POS("O+"),
    A_NEG("A-"), A_POS("A+"),
    B_NEG("B-"), B_POS("B+"),
    AB_NEG("AB-"), AB_POS("AB+");

    private final String label;

    BloodGroup(String label) { this.label = label; }

    public String label() { return label; }

    public boolean canDonateTo(BloodGroup receiver) {
        if (receiver == null) return false;

        boolean aboOk = switch (this) {
            case O_NEG, O_POS -> true;
            case A_NEG, A_POS -> receiver == A_NEG || receiver == A_POS || receiver == AB_NEG || receiver == AB_POS;
            case B_NEG, B_POS -> receiver == B_NEG || receiver == B_POS || receiver == AB_NEG || receiver == AB_POS;
            case AB_NEG, AB_POS -> receiver == AB_NEG || receiver == AB_POS;
        };

        if (!aboOk) return false;

        boolean donorRhPositive = this == O_POS || this == A_POS || this == B_POS || this == AB_POS;
        boolean receiverRhNegative = receiver == O_NEG || receiver == A_NEG || receiver == B_NEG || receiver == AB_NEG;
        return !(donorRhPositive && receiverRhNegative);
    }

    public List<BloodGroup> compatibleDonors() {
        List<BloodGroup> result = new ArrayList<>();
        for (BloodGroup group : values()) {
            if (group.canDonateTo(this)) result.add(group);
        }
        return result;
    }

    public static BloodGroup fromText(String text) {
        if (text == null) throw new IllegalArgumentException("Blood group is required.");
        String value = text.trim().toUpperCase().replace(" ", "");
        for (BloodGroup group : values()) {
            if (group.label.equals(value)) return group;
        }
        throw new IllegalArgumentException("Invalid blood group. Use A+, A-, B+, B-, AB+, AB-, O+ or O-.");
    }

    @Override
    public String toString() { return label; }
}
