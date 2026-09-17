package com.bloodbank.util;

import com.bloodbank.model.BloodUnit;
import com.bloodbank.model.BloodRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

public final class FileUtil {
    private FileUtil() {}

    public static void log(String message) {
        File dir = new File("logs");
        if (!dir.exists()) dir.mkdirs();
        try (BufferedWriter out = new BufferedWriter(new FileWriter(
                new File(dir, "activity.log"), StandardCharsets.UTF_8, true))) {
            out.write(LocalDateTime.now() + " - " + message);
            out.newLine();
        } catch (IOException e) {
            System.err.println("Could not write log: " + e.getMessage());
        }
    }

    public static String writeDispatchFile(BloodRequest request, List<BloodUnit> units) {
        File dir = new File("reports");
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, "dispatch_" + request.getId() + ".txt");

        try (BufferedWriter out = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            out.write("BLOOD BANK DISPATCH NOTE");
            out.newLine();
            out.write("-----------------------");
            out.newLine();
            out.write("Request ID: " + request.getId()); out.newLine();
            out.write("Patient: " + request.getPatient()); out.newLine();
            out.write("Hospital: " + request.getHospital()); out.newLine();
            out.write("Blood group: " + request.getGroup()); out.newLine();
            out.write("Component: " + request.getComponent().label()); out.newLine();
            out.write("Priority: " + request.getPriority()); out.newLine();
            out.write("Units: " + units.size()); out.newLine();
            out.newLine();
            for (BloodUnit unit : units) {
                out.write(unit.getId() + " | " + unit.getGroup() +
                        " | expires " + unit.getExpiresOn());
                out.newLine();
            }
            return file.getPath();
        } catch (IOException e) {
            throw new RuntimeException("Could not create dispatch file.", e);
        }
    }
}
