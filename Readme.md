# Second Lowest Cost Silver Plan

## Setup Instructions

- Installing JDK 1.8+ (only tested with Oracle JDK) and Maven
    - Mac OS X
        - Run the following commands in the terminal: `brew tap caskroom/versions && brew cask install java8`.
    - Cent OS / Fedora
        - Run the following commands in the shell `cd ~ && wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://link_copied_from_site"`
        - After it is done downloading run `sudo yum localinstall jdk-8u161-linux-x64.rpm`.
        - At the prompt hit `y` and `ENTER`.
    - Debian Based Linux (e.g. Ubuntu)
        - Open up the terminal and enter `sudo add-apt-repository ppa:webupd8team/java && sudo apt-get update && sudo apt-get install oracle-java8-installer`.
            - Press `Enter` and accept the terms as necessary.
        - Make sure the default java installion being used is correct (Java 8+).
            - The easy way is to use `sudo apt install oracle-java8-set-default`. If you prefer to manually set it continue below.
            - Run `sudo update-alternatives --config java` in a terminal.
            - Select the appropriate java installation using a number.
        - Set your `JAVA_HOME` environment variable. This step is not always necessary but may be needed 
            - Type `sudo nano /etc/environment` in the terminal.
            - This should open up an editor that allows you to edit the file (the editor doesn't matter but you will need su permissions to open the file).
            - Append a line with `JAVA_HOME=` with the path to your java installation after, at the end of the file, similar to `JAVA_HOME="/usr/lib/jvm/java-8-oracle"`
            - Reload the terminal and type `source /etc/environment && echo $JAVA_HOME` to test if the environment variable is pointing to the right location.
        - Check that the correct java version is installed via `java -version`.
            - Should say something like `java version "1.8.0_191"`.
        - Install maven using the shell command `sudo apt-get install maven`.
        - Test that maven is installed using `mvn -version`. You should see something like `Maven home: /usr/share/maven` along with the various version numbers of Java printed out underneath that.
    - Windows
        - Go to https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html and download the appropriate version of the JDK
        - Update the Windows PATH and JAVA_HOME environment variables.
            - In the Control Panel select System.
            - Then in the System window click Advanced and then Environment Variables
            - Under System Variables find the PATH variable and append the path of the java installation to the end. It should look something like `C:\WINDOWS\system32;C:\WINDOWS;"C:\Program Files\Java\jdk-11\bin"`. Please note all variables must be separated by a semicolon.
            - Under System Variables again, create a new variable with the name `JAVA_HOME` and point it to the same JDK installation directory.

- Install Maven
    - Download the appropriate maven install zip file from https://maven.apache.org/download.cgi.
        - This can also be done via `sudo apt-get install maven` in the terminal in Debian based systems.
    - Unzip the file to any directory using your favorite program or `tar xzvf apache-maven-3.6.0-bin.tar.gz` in the terminal (with the appropriate version number).
    - Add the `bin` directory of the maven directory you unzipped to the Windows `PATH` variable.
    - Confirm that Maven is installed with `mvn -version` in the terminal.
        
- Compiling and running the program
    - In the root of the project compile the program using `mvn clean package`.
    - Run the program using `java -jar jar-dir/slcsp-1.0.0-RELEASE.jar --zipcodes.path=zip-csv-path --plans.path=plan-csv-path --slcsp.path=slcsp-csv-path`
        - epxcted path format: `/path/to/thing`
        - jar-dir is the path to the jar file for execution
        - zip-csv-path is the path to the CSV file with all the zipcodes and rate areas
        - plan-csv-path is the path to the CSV file with all the plans and rates
        - slcsp-csv-path is the path to the CSV file that contains the zipcodes you want to obtain rates for
        - if all the files are in the same path as the jar it should look something like `java -jar ./slcsp-1.0.0-RELEASE.jar --zipcodes.path=./zips.csv --plans.path=./plans.csv --slcsp.path=./slcsp.csv`

## Technology Rationale

- Spring Boot
    - All the usual Spring framework nicieties
        - convenient annotations
        - integrations with agnostic APIs

- H2
    - Relatively lightweight and easy to deploy
    - JDBC compatible (API agno)
    - Easy to switch between embedded and server modes
    - Usually better for small POC like projects
        - does not scale well, especially past 500k records