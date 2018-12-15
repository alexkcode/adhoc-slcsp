## Setup Instructions

- Install JDK v1.8
    - Linux
        1. Open up the terminal and enter `sudo apt-get update && apt-get upgrade`.
        2.
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