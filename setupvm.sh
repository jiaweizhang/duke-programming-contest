#!/bin/bash
sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt/ `lsb_release -cs`-pgdg main" >> /etc/apt/sources.list.d/pgdg.list'
wget -q https://www.postgresql.org/media/keys/ACCC4CF8.asc -O - | sudo apt-key add -
sudo apt-get update
sudo apt-get install postgresql postgresql-contrib

#install dependencies
sudo apt-get install software-properties-common

#install java
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer
sudo apt-get install oracle-java8-set-default

#install gradle
sudo add-apt-repository ppa:cwchien/gradle
sudo apt-get update
sudo apt-get install gradle-2.14.1

#install tmux
sudo apt-get install tmux

#install vim
sudo apt-get install vim

#install git
sudo apt-get install git

#clone repo
git clone https://github.com/jiaweizhang/duke-programming-contest.git

#setup postgres
sudo su - postgres
psql
\password
\q