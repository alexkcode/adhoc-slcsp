## Setup Instructions

- Linux
    1. Open up the terminal and enter `sudo apt-get update && apt-get upgrade`.
    2.
    3.
    4. Check that the correct java version is installed via `java -version`.
    5. Install maven using the shell command `sudo apt-get install maven`.
    6. Compile the program using `mvn clean package`.
    7. Run the using `java -jar jar-dir/slcsp-0.0.1-RELEASE.jar --zipcodes.path=zip-csv-path --plans.path=plan-csv-path --slcsp.path=slcsp-csv-path`
        - epxcted path format: `/path/to/thing`
        - jar-dir is the path to the jar file for execution
        - zip-csv-path is the path to the CSV file with all the zipcodes and rate areas
        - plan-csv-path is the path to the CSV file with all the plans and rates
        - slcsp-csv-path is the path to the CSV file that contains the zipcodes you want to obtain rates for
- Windows
    1. Go to https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
    2.

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