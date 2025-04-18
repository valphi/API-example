```markdown
# SftpApplication

## Overview

The `SftpApplication` is a Java-based application that connects to an SFTP server, generates a CSV file with portfolio data, and uploads it to a specified directory on the server. The application uses SSH for secure communication and requires Java 21 to run.

## Prerequisites

1. **Install Java 21**  
   Ensure Java 21 is installed on your system and added to your system's `PATH`. You can verify the installation by running:
   java -version

2. **Files Provided**
    - `SftpApplication.jar`: The compiled application.
    - `run_windows.bat`, `run_macos.sh`, `run_ubuntu.sh`: Scripts to set environment variables and run the application.
   

## Simplest Way of Running the Application

## Notes
- Put the `SftpApplication.jar` file in the same directory as the run files.
- Ensure the paths in the run file match your system's file structure.
- The application will generate a CSV file locally and upload it to the SFTP server. If successful, you will see a confirmation message in the terminal.

### Step 1: Adjust the Run File
Open the appropriate run file for your operating system in a text editor and set the following environment variables:
- `SFTP_SERVER`: The SFTP server address.
- `SFTP_USER`: The username for the SFTP server.
- `SFTP_PHRASE`: The passphrase for the private key (if applicable).
- `SFTP_PRIVATE_KEY`: The path to the private key file.
- `SFTP_LOCAL_PORTFOLIO_DIRECTORY`: The local directory where the CSV file will be generated.

### Step 2: Run the Application

#### Windows
1. Open a Command Prompt (cmd.exe).
2. Run the following command:
   run_windows.bat

#### macOS
1. Open a Terminal.
2. Make the script executable (if not already):
   chmod +x run_macos.sh
3. Run the script:
   ./run_macos.sh

#### Ubuntu
1. Open a Terminal.
2. Make the script executable (if not already):
   chmod +x run_ubuntu.sh
3. Run the script:
   ./run_ubuntu.sh
   
   
## Longest Way to Run the SftpApplication

If your computer has an old version of Gradle and/or Java installed, and you want to run the `SftpApplication` without affecting your existing setup, follow these steps:

### Step 1: Clone the Repository
1. Open a terminal or command prompt.
2. Clone the GitHub repository:
   https://github.com/valphi/API-example.git
3. Navigate to the cloned repository:
   cd <repository-folder>

### Step 2: Install Java 21
1. Download Java 21 from the official Oracle or OpenJDK website.
2. Extract or install Java 21 to a directory of your choice.
3. Set up a temporary `JAVA_HOME` environment variable pointing to the Java 21 installation:
   - **Windows**:
     set JAVA_HOME=C:\path\to\java21
     set PATH=%JAVA_HOME%\bin;%PATH%
  
   - **macOS**:
     export JAVA_HOME=$(/usr/libexec/java_home -v 21)
     source ~/.zshrc
     java -version
     
   - **Linux**:
     export JAVA_HOME=/path/to/java21
     export PATH=$JAVA_HOME/bin:$PATH
4. Verify the Java version:
   java -version

### Step 3: Use the Wrapper to Run Gradle
The repository includes a Gradle wrapper (`gradlew`) that allows you to use the correct Gradle version without installing it globally.

1. Run the Gradle wrapper to build the project:
   - **Windows**:
     gradlew.bat build
   - **macOS/Linux**:
     ./gradlew build
     
### Step 5: Run the Application

1. Navigate to the `build/libs` directory where the `SftpApplication.jar` file is generated:
   cd build/libs

2. Copy the `SftpApplication.jar` file to the root project directory where the run files (`run_windows.bat`, `run_macos.sh`, `run_ubuntu.sh`) are located:
   cp SftpApplication.jar ../../

3. Run the appropriate file for your operating system:
   - **Windows**:
     Open a Command Prompt (cmd.exe) and run:
     run_windows.bat
  
   - **macOS**:
     Open a Terminal, make the script executable (if not already), and run:
     chmod +x run_macos.sh
     ./run_macos.sh
  
   - **Ubuntu**:
     Open a Terminal, make the script executable (if not already), and run:
       chmod +x run_ubuntu.sh
     ./run_ubuntu.sh

### Step 6: Restore Your Previous Setup
1. Reset your `JAVA_HOME` and `PATH` environment variables to point to your old Java version.
2. If necessary, remove any temporary changes made to your system.

### Notes
- Ensure the environment variables are correctly set before running the application.
- The Gradle wrapper ensures compatibility with the project’s required Gradle version.
- This method avoids modifying your existing Java and Gradle installations.

```