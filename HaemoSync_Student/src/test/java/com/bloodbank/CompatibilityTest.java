package com.bloodbank;

import com.bloodbank.model.BloodGroup;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompatibilityTest {
    @Test
    void onegCanDonateToAll() {
        for (BloodGroup receiver : BloodGroup.values())
            assertTrue(BloodGroup.O_NEG.canDonateTo(receiver));
    }

    @Test
    void positiveCannotDonateToNegative() {
        assertFalse(BloodGroup.A_POS.canDonateTo(BloodGroup.A_NEG));
        assertFalse(BloodGroup.O_POS.canDonateTo(BloodGroup.O_NEG));
    }

    @Test
    void aPositiveHasFourCompatibleDonors() {
        assertEquals(4, BloodGroup.A_POS.compatibleDonors().size());
    }
}
