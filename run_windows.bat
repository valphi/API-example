@echo off
set SFTP_SERVER=s-75f7a0622f7c4478a.server.transfer.eu-west-1.amazonaws.com
set SFTP_USER=user
set SFTP_PHRASE=password
set SFTP_PRIVATE_KEY=%USERPROFILE%\.ssh\id_rsa
set SFTP_LOCAL_USER_INDICATOR_DIRECTORY=%USERPROFILE%\sftp-sismo\user_indicator
set SFTP_LOCAL_MACRO_INDICATOR_DIRECTORY=%USERPROFILE%\sftp-sismo\macro_indicator
set SFTP_LOCAL_PORTFOLIO_DIRECTORY=%USERPROFILE%\sftp-sismo\portfolio
java -jar SftpApplication.jar