package com.sismo.demo;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import static com.sismo.demo.utils.FileUtil.compressAndDeleteInitFile;
import static java.time.LocalDateTime.now;

public class SftpApplication {
    private final static String SERVER = System.getenv("SFTP_SERVER");
    private final static String USER = System.getenv("SFTP_USER");
    private final static String PHRASE = System.getenv("SFTP_PHRASE");
    private final static String PRIVATE_KEY = System.getenv("SFTP_PRIVATE_KEY");
    private final static String LOCAL_USER_INDICATOR_DIRECTORY = System.getenv("SFTP_LOCAL_USER_INDICATOR_DIRECTORY");
    private final static String LOCAL_MACRO_INDICATOR_DIRECTORY = System.getenv("SFTP_LOCAL_MACRO_INDICATOR_DIRECTORY");
    private final static String LOCAL_PORTFOLIO_DIRECTORY = System.getenv("SFTP_LOCAL_PORTFOLIO_DIRECTORY");

    private static final String SFTP_USER_INDICATOR_DIRECTORY = "/user_indicator";
    private static final String SFTP_MACRO_INDICATOR_DIRECTORY = "/macro_indicator";
    private static final String SFTP_PORTFOLIO_DIRECTORY = "/portfolio";
    private static final Set<String> TO_ARCHIVE_FILES_IN_DIRECTORY = Set.of(SFTP_USER_INDICATOR_DIRECTORY);

    public static void main(String[] args) {
        try (SSHClient ssh = new SSHClient()) {
            sshClientAuth(ssh);
            runFileUpload(ssh, LOCAL_USER_INDICATOR_DIRECTORY, SFTP_USER_INDICATOR_DIRECTORY);
            runFileUpload(ssh, LOCAL_MACRO_INDICATOR_DIRECTORY, SFTP_MACRO_INDICATOR_DIRECTORY);
            runFileUpload(ssh, LOCAL_PORTFOLIO_DIRECTORY, SFTP_PORTFOLIO_DIRECTORY);
        } catch (Exception e) {
            System.out.println("File upload error." + e.getMessage());
        }
    }

    private static void runFileUpload(SSHClient ssh, String localDirectory, String sftpDirectory) {
        if (isDirectorySetInEnvVariables(localDirectory)) {
            log("Start copying user indicator files to SFTP server", localDirectory);
            copyFiles(ssh, localDirectory, sftpDirectory);
            log("Finish copying user indicator files to SFTP server", localDirectory);
        }
    }

    private static void sshClientAuth(SSHClient ssh) throws IOException {
        // Skip host key verification for simplicity
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(SERVER);

        File key = new File(PRIVATE_KEY);
        KeyProvider keys;
        if (PHRASE != null && !PHRASE.isEmpty()) {
            keys = ssh.loadKeys(key.getPath(), PHRASE);
        } else {
            keys = ssh.loadKeys(key.getPath());
        }
        ssh.authPublickey(USER, keys);
    }

    private static void copyFiles(SSHClient ssh, String localDirectory, String sftpDirectory) {
        // Initialize folder with files
        File folder = new File(localDirectory);
        if (!folder.exists() || !folder.isDirectory()) {
            log("Invalid user indicator data location: " + localDirectory, localDirectory);
            return;
        }

        // Get all files in the directory
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            log("No files found in user indicator data location: " + localDirectory, localDirectory);
            return;
        }

        // Iterate through each file and copy to SFTP
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile() && (fileName.endsWith(".zip") || fileName.endsWith(".csv"))) {
                if (TO_ARCHIVE_FILES_IN_DIRECTORY.contains(sftpDirectory) && !fileName.endsWith("-D.csv") && !fileName.endsWith(".zip")) {
                    String filePath = file.getPath();
                    compressAndDeleteInitFile(filePath, filePath.replace(".csv", ".zip"));
                }
                copyFilesToSFTP(ssh, fileName, localDirectory, sftpDirectory);
                log("File " + fileName + " copied successfully.", localDirectory);
            }
        }
    }

    private static void copyFilesToSFTP(SSHClient ssh, String fileName, String localDirectory, String sftpDirectory) {
        try (SFTPClient sftp = ssh.newSFTPClient()) {
            sftp.put(localDirectory + "/" + fileName, sftpDirectory + "/" + fileName); // Upload the file
            System.out.println("File uploaded successfully.");
        } catch (Exception e) {
            System.out.println("File upload error." + e.getMessage());
        }
    }

    private static void log(String message, String localDirectory) {
        // Save logs in the local directory to log.txt file
        try (FileWriter writer = new FileWriter(localDirectory + "/log.txt", true)) {
            String timestampedMessage = "[" + now() + "] " + message;
            writer.write(timestampedMessage + "\n");
            System.out.println(timestampedMessage);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    private static boolean isDirectorySetInEnvVariables(String directory) {
        return directory != null && !directory.isEmpty();
    }
}
