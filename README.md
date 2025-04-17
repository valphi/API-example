```markdown
# SftpApplication

## Setting Environment Variables and Running The Application

To run the application, you need to:
- Save the Java file (`SftpApplication.java`) in your desired directory.
- Save the run file (`run_windows.bat`, `run_macos.sh`, `run_ubuntu.sh`) in your desired directory.
- Install Java 21 if it's not installed.
- Follow the steps below according to your Operating System (Windows, macOS, or Ubuntu).

### Windows
1. Open `run_windows.bat` in an editor (e.g., Notepad), set your environment variables, and save the file.
2. Open a Command Prompt (cmd.exe).
3. Run the following command to execute the script:
   run_windows.bat
4. This will run the Java file and copy files to the SFTP server.

### macOS
1. Open `run_macos.sh` in an editor (e.g., TextEdit), set your environment variables, and save the file.
2. Open a Terminal.
3. Make the script executable (if not already):
   chmod +x run_macos.sh
4. Run the script using:
   source run_macos.sh
5. This will run the Java file and copy files to the SFTP server.

**Note:** If you have more than one Java version installed, you can set the `JAVA_HOME` environment variable to point to the desired version. For example:
   #### bash
   export JAVA_HOME=$(/usr/libexec/java_home -v 21)
   source ~/.zshrc
   java -version

### Ubuntu
1. Open `run_ubuntu.sh` in an editor (e.g., Vim), set your environment variables, and save the file.
2. Open a Terminal.
3. Make the script executable (if not already):
   chmod +x run_ubuntu.sh
4. Run the script using:
   source run_ubuntu.sh
5. This will run the Java file and copy files to the SFTP server.

---

## Notes
- Ensure Java is installed and added to your system's `PATH`.
- Replace the paths in the environment variable scripts with the appropriate paths for your system.
```