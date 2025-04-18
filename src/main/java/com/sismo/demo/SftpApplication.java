package com.sismo.demo;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class SftpApplication {
    public static void main(String[] args) {
        // You need to set environment variables before running this application (see README.md)
        // Open a connection to the SFTP server in try-with-resources block to prevent resource leaks
        try (SSHClient sshClient = new SSHClient()) {
            // Init SFTP parameters
            String SERVER = System.getenv("SFTP_SERVER");
            String USER = System.getenv("SFTP_USER");
            String PHRASE = System.getenv("SFTP_PHRASE");
            String PRIVATE_KEY = System.getenv("SFTP_PRIVATE_KEY");
            String LOCAL_PORTFOLIO_DIRECTORY = System.getenv("SFTP_LOCAL_PORTFOLIO_DIRECTORY");
            String SFTP_PORTFOLIO_DIRECTORY = "/portfolio";

            // Provide with the external id for the portfolio in Sismo
            String externalId = "123AB";

            // Generate the file name
            String operationType = "M"; // M for merge
            long epochMillis = System.currentTimeMillis();
            String fileName = externalId + "-" + epochMillis + "-" + operationType + ".csv";

            // Create the CSV content
            String currentDate = LocalDate.now().toString().replace("-", "");
            String content = currentDate + "," + "US0231351067" + "," + "Amazon.com Inc." + "," + 0.5f + "\n" +
                             currentDate + "," + "US0378331005" + "," + "Apple Inc." + "," + 0.25f + "\n" +
                             currentDate + "," + "US5949181045" + "," + "Microsoft Corp." + "," + 0.25f;

            // Save the file locally
            Path filePath = Paths.get(LOCAL_PORTFOLIO_DIRECTORY, fileName);
            Files.write(filePath, content.getBytes());

            // Connect to the SFTP server
            // Skip host key verification for simplicity
            sshClient.addHostKeyVerifier(new PromiscuousVerifier());
            sshClient.connect(SERVER);

            // Get the keys
            File key = new File(PRIVATE_KEY);
            KeyProvider keys;

            // Load private and public key with passphrase if provided
            if (PHRASE != null && !PHRASE.isEmpty()) {
                keys = sshClient.loadKeys(key.getPath(), PHRASE);
            } else {
                keys = sshClient.loadKeys(key.getPath());
            }
            sshClient.authPublickey(USER, keys);

            // Upload the file to SFTP server
            SFTPClient sftp = sshClient.newSFTPClient();
            sftp.put(LOCAL_PORTFOLIO_DIRECTORY + "/" + fileName, SFTP_PORTFOLIO_DIRECTORY + "/" + fileName); // Upload the file
            System.out.println("File "+ fileName + " uploaded successfully.");
        } catch (Exception e) {
            System.out.println("File upload error." + e.getMessage());
        }
    }
}
