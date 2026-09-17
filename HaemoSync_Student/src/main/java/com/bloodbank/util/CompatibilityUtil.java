package com.bloodbank.util;

import com.bloodbank.model.BloodGroup;
import java.util.List;

public final class CompatibilityUtil {
    private CompatibilityUtil() {}

    public static List<BloodGroup> donorsFor(BloodGroup receiver) {
        return receiver.compatibleDonors();
    }
}
