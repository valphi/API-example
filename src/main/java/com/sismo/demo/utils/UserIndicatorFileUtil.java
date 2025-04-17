package com.sismo.demo.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.sismo.demo.utils.FileUtil.clearPreviousFiles;
import static com.sismo.demo.utils.FileUtil.compressAndDeleteInitFile;

public class UserIndicatorFileUtil {

    public static void main(String[] args) {
        // Set up test data
        String externalId = "123AB";
        String currentDate = LocalDate.now().toString().replace("-", "");
        String localDirectory = "~/sftp-sismo/user_indicator";

        // Clear previous files
        clearPreviousFiles(localDirectory);

        // Delete upload
        List<UserIndicator> data = new ArrayList<>();
        data.add(new UserIndicator(currentDate));
        saveUserIndicatorToFile(data, externalId, "D", true, localDirectory);

        // Merge upload
        data = new ArrayList<>();
        data.add(new UserIndicator(currentDate, "US0231351067", 50.0f)); // "Amazon.com Inc."
        data.add(new UserIndicator(currentDate, "US1234567890", 25.0f)); // "Apple Inc."
        data.add(new UserIndicator(currentDate, "US9876543210", 25.0f)); // "Microsoft Corp."
        saveUserIndicatorToFile(data, externalId, "M", true, localDirectory);

        // Full upload
        data = new ArrayList<>();
        data.add(new UserIndicator(currentDate, "US1234567890", 25.0f)); // "Apple Inc."
        data.add(new UserIndicator(currentDate, "US9876543210", 75.0f)); // "Microsoft Corp."
        saveUserIndicatorToFile(data, externalId, "F", true, localDirectory);

    }

    private static void saveUserIndicatorToFile(List<UserIndicator> data, String externalId, String operationType, boolean compressFile, String localDirectory) {
        try {
            // Generate the file name
            String fileName = generateFileName(externalId, operationType);

            // Create the CSV content
            String content = createCSVContent(data);

            Path filePath = java.nio.file.Paths.get(localDirectory, fileName);
            Files.write(filePath, content.getBytes());

            // Archive the file if required
            if (compressFile && !operationType.equals("D")) {
                compressAndDeleteInitFile(filePath.toString(), filePath.toString().replace(".csv", ".zip"));
            }

            log("File " + fileName + " saved successfully in " + localDirectory, localDirectory);
        } catch (IOException e) {
            log("Error saving file: " + e.getMessage(), localDirectory);
            throw new RuntimeException("Failed to save file", e);
        }
    }

    private static String createCSVContent(List<UserIndicator> data) {
        StringBuilder sb = new StringBuilder();
        for (UserIndicator record : data) {
            // Add date to CSV
            sb.append(record.date().toString());

            // Add ISIN if present
            if (record.isin != null && !record.isin.isEmpty()) {
                sb.append(",").append(record.isin());
            }

            // Add value if present
            if (record.value != null) {
                sb.append(",").append(record.value());
            }

            sb.append("\n");
        }
        return sb.toString();
    }

    private static String generateFileName(String externalId, String operation) {
        long epochMillis = System.currentTimeMillis();
        return externalId + "-" + epochMillis + "-" + operation + ".csv";
    }

    private static void log(String message, String localDirectory) {
        // Save logs in the local directory to log.txt file
        try (FileWriter writer = new FileWriter(localDirectory + "/log.txt", true)) {
            String timestampedMessage = "[" + java.time.LocalDateTime.now() + "] " + message;
            writer.write(timestampedMessage + "\n");
            System.out.println(timestampedMessage);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    record UserIndicator(String date, String isin, Float value) {
        public UserIndicator(String date) {
            this(date, null, null);
        }
    }
}
