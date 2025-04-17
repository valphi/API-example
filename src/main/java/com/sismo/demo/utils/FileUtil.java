package com.sismo.demo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {

    public static void clearPreviousFiles(String localDirectory) {
        File folder = new File(localDirectory);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && (file.getName().endsWith(".zip") || file.getName().endsWith(".csv"))) {
                        deleteFile(file);
                    }
                }
            }
        }
    }

    public static void compressAndDeleteInitFile(String sourceFilePath, String zipFilePath) {
        try (FileInputStream fis = new FileInputStream(sourceFilePath);
             FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            // Create a zip entry for the file
            ZipEntry zipEntry = new ZipEntry(new File(sourceFilePath).getName());
            zos.putNextEntry(zipEntry);

            // Read the file and write it to the zip output stream
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }

            zos.closeEntry();
            File sourceFile = new File(sourceFilePath);
            deleteFile(sourceFile);
            System.out.println("File compressed successfully: " + zipFilePath);
        } catch (IOException e) {
            System.err.println("Error compressing file: " + e.getMessage());
        }
    }

    public static void deleteFile(File file) {
        if (file.delete()) {
            System.out.println("File " + file.getName() + " deleted successfully.");
        } else {
            System.out.println("Failed to delete file " + file.getName());
        }
    }
}

