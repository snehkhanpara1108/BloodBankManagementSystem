package com.bloodbank.service;

import com.bloodbank.dao.*;
import com.bloodbank.exception.*;
import com.bloodbank.model.*;
import com.bloodbank.util.FileUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class BloodBankService {
    private final DonorDao donorDao;
    private final BloodUnitDao unitDao;
    private final RequestDao requestDao;
    private final ReentrantLock stockLock = new ReentrantLock();

    public BloodBankService(DonorDao donorDao, BloodUnitDao unitDao, RequestDao requestDao) {
        this.donorDao = donorDao;
        this.unitDao = unitDao;
        this.requestDao = requestDao;
    }

    public void registerDonor(Donor donor) {
        if (donorDao.findById(donor.getId()).isPresent())
            throw new IllegalArgumentException("A donor with this ID already exists.");
        donorDao.save(donor);
        FileUtil.log("Registered donor " + donor.getId());
    }

    public void checkEligibility(Donor donor, double hemoglobin, int systolic, int diastolic)
            throws DonorEligibilityException {
        if (donor.getAge() < 18 || donor.getAge() > 65)
            throw new DonorEligibilityException("Age should be between 18 and 65.");
        if (donor.getWeight() < 50)
            throw new DonorEligibilityException("Weight should be at least 50 kg.");
        if (hemoglobin < 12.5)
            throw new DonorEligibilityException("Hemoglobin should be at least 12.5 g/dL.");
        if (systolic < 90 || systolic > 180 || diastolic < 50 || diastolic > 100)
            throw new DonorEligibilityException("Blood pressure is outside the allowed range.");
        if (donor.getLastDonation() != null &&
                donor.getLastDonation().plusDays(90).isAfter(LocalDate.now()))
            throw new DonorEligibilityException("Donor has not completed the 90-day donation gap.");
    }

    public BloodUnit recordDonation(String donorId, ComponentType component, double hemoglobin,
                                     int systolic, int diastolic) throws DonorEligibilityException {
        Donor donor = donorDao.findById(donorId)
                .orElseThrow(() -> new IllegalArgumentException("Donor not found."));
        checkEligibility(donor, hemoglobin, systolic, diastolic);

        BloodUnit unit = new BloodUnit(
                "U-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                donor.getBloodGroup(), component, LocalDate.now(), donor.getId());

        stockLock.lock();
        try {
            unitDao.save(unit);
            donorDao.updateLastDonation(donorId, LocalDate.now());
        } finally {
            stockLock.unlock();
        }

        FileUtil.log("Donation recorded: " + unit.getId() + " from " + donorId);
        return unit;
    }

    public List<Donor> donors() { return donorDao.findAll(); }

    public List<BloodUnit> availableUnits() { return unitDao.findAvailable(); }

    public Map<BloodGroup, Long> stockCount() {
        Map<BloodGroup, Long> result = new EnumMap<>(BloodGroup.class);
        for (BloodGroup group : BloodGroup.values()) result.put(group, 0L);
        for (BloodUnit unit : unitDao.findAvailable())
            if (!unit.isExpired())
                result.put(unit.getGroup(), result.get(unit.getGroup()) + 1);
        return result;
    }

    public String fulfill(String patient, String hospital, BloodGroup group,
                          ComponentType component, int quantity, RequestPriority priority)
            throws StockException {
        BloodRequest request = new BloodRequest(
                "R-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                patient, hospital, group, component, quantity, priority);
        requestDao.save(request);

        stockLock.lock();
        try {
            List<BloodUnit> candidates = new ArrayList<>();
            for (BloodUnit unit : unitDao.findAvailable()) {
                if (!unit.isExpired() && unit.getComponent() == component &&
                        unit.getGroup().canDonateTo(group)) {
                    candidates.add(unit);
                }
            }
            Collections.sort(candidates);

            if (candidates.size() < quantity) {
                requestDao.updateStatus(request.getId(), "SHORTAGE");
                throw new StockException("Not enough compatible blood units. Available: " + candidates.size());
            }

            List<BloodUnit> chosen = candidates.subList(0, quantity);
            for (BloodUnit unit : chosen) unitDao.updateStatus(unit.getId(), BloodUnit.Status.DISPATCHED);
            requestDao.updateStatus(request.getId(), "FULFILLED");

            String path = FileUtil.writeDispatchFile(request, chosen);
            FileUtil.log("Request " + request.getId() + " fulfilled. File: " + path);
            return path;
        } finally {
            stockLock.unlock();
        }
    }

    public List<String> requests() { return requestDao.recent(); }

    public int removeExpired() {
        int count = 0;
        stockLock.lock();
        try {
            for (BloodUnit unit : unitDao.findAvailable()) {
                if (unit.isExpired()) {
                    unitDao.updateStatus(unit.getId(), BloodUnit.Status.EXPIRED);
                    count++;
                }
            }
            if (count > 0) FileUtil.log("Expired units removed: " + count);
            return count;
        } finally { stockLock.unlock(); }
    }

    public void startExpiryChecker() {
        Thread checker = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                removeExpired();
                try { Thread.sleep(60000); }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "expiry-checker");
        checker.setDaemon(true);
        checker.start();
    }
}
