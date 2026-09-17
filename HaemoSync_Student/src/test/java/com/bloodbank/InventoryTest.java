package com.bloodbank;

import com.bloodbank.model.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
    @Test
    void earlierExpiryComesFirst() {
        BloodUnit first = new BloodUnit("1",BloodGroup.B_POS,ComponentType.RED_CELLS,
                LocalDate.now().minusDays(40),"D1");
        BloodUnit second = new BloodUnit("2",BloodGroup.B_POS,ComponentType.RED_CELLS,
                LocalDate.now().minusDays(10),"D2");
        PriorityQueue<BloodUnit> queue = new PriorityQueue<>();
        queue.add(second); queue.add(first);
        assertEquals("1", queue.poll().getId());
    }
}
