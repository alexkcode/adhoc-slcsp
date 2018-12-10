##Technology Rationale

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