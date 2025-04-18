#!/bin/bash
export SFTP_SERVER=s-75f7a0622f7c4478a.server.transfer.eu-west-1.amazonaws.com
export SFTP_USER=user
export SFTP_PHRASE=password
export SFTP_PRIVATE_KEY=~/.ssh/id_rsa
export SFTP_LOCAL_USER_INDICATOR_DIRECTORY=~/sftp-sismo/user_indicator
export SFTP_LOCAL_MACRO_INDICATOR_DIRECTORY=~/sftp-sismo/macro_indicator
export SFTP_LOCAL_PORTFOLIO_DIRECTORY=~/sftp-sismo/portfolio
java -jar SftpApplication.jar