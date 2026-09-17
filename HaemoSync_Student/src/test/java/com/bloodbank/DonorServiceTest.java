package com.bloodbank;

import com.bloodbank.dao.*;
import com.bloodbank.exception.DonorEligibilityException;
import com.bloodbank.model.*;
import com.bloodbank.service.BloodBankService;
import com.bloodbank.util.Database;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class DonorServiceTest {
    private BloodBankService service;

    @BeforeEach
    void setup() {
        service = new BloodBankService(new DonorDao(), new BloodUnitDao(), new RequestDao());
    }

    @Test
    void validDonorPasses() {
        Donor donor = new Donor("T1","Test","000",25,65,BloodGroup.O_POS);
        assertDoesNotThrow(() -> service.checkEligibility(donor,13,120,80));
    }

    @Test
    void lowWeightFails() {
        Donor donor = new Donor("T2","Test","000",25,45,BloodGroup.O_POS);
        assertThrows(DonorEligibilityException.class,
                () -> service.checkEligibility(donor,13,120,80));
    }

    @Test
    void lowHemoglobinFails() {
        Donor donor = new Donor("T3","Test","000",25,65,BloodGroup.O_POS);
        assertThrows(DonorEligibilityException.class,
                () -> service.checkEligibility(donor,12,120,80));
    }
}
