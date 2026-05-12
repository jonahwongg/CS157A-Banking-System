# Banking Application

This project is a three-tier Java web application for a banking system. It uses JSP, Servlets, JDBC, and MySQL to perform direct SQL-based CRUD operations for customers, accounts, transactions, and loans.

## Project Overview

The system demonstrates a three-tier architecture:

1. Presentation Layer
   JSP pages, HTML, CSS, and JavaScript in `src/main/webapp`
2. Application Layer
   Servlets, service classes, and JDBC DAO classes in `src/main/java/com/bank`
3. Data Layer
   MySQL database schema and initialization scripts in `database/`

## Features

- Customer creation, update, listing, and deletion
- Account creation, update, listing, and deletion
- Deposits and withdrawals
- Transfers between accounts
- Transaction log display and deletion
- Loan creation, update, processing by status, listing, and deletion
- Database-backed login and logout with session-based access control

## Project Directory Structure

```text
C:\Users\jonah\Documents\Codex\2026-04-28\i-need-you-to-build-a
|-- database
|   |-- create_schema.sql
|   |-- initialize_data.sql
|   |-- schema.sql
|   `-- seed.sql
|-- src
|   `-- main
|       |-- java\com\bank
|       |   |-- config
|       |   |-- dao
|       |   |-- model
|       |   |-- service
|       |   |-- util
|       |   `-- web
|       |-- resources
|       |   `-- db.properties
|       `-- webapp
|           |-- assets
|           |-- index.jsp
|           `-- WEB-INF
|               |-- web.xml
|               `-- views
|-- pom.xml
`-- README.md
```

## JDBC and SQL usage

The application uses:

- `DriverManager` through `ConnectionFactory`
- JDBC `Connection`, `PreparedStatement`, and `ResultSet`
- Direct SQL `SELECT`, `INSERT`, `UPDATE`, and `DELETE` statements in DAO classes
- SQL transactions for deposit, withdrawal, and transfer workflows

The main SQL statements are inside these JDBC DAO classes:

- [CustomerDao.java](C:/Users/jonah/Documents/Codex/2026-04-28/i-need-you-to-build-a/src/main/java/com/bank/dao/CustomerDao.java)
- [AccountDao.java](C:/Users/jonah/Documents/Codex/2026-04-28/i-need-you-to-build-a/src/main/java/com/bank/dao/AccountDao.java)
- [TransactionDao.java](C:/Users/jonah/Documents/Codex/2026-04-28/i-need-you-to-build-a/src/main/java/com/bank/dao/TransactionDao.java)
- [LoanDao.java](C:/Users/jonah/Documents/Codex/2026-04-28/i-need-you-to-build-a/src/main/java/com/bank/dao/LoanDao.java)
- [UserDao.java](C:/Users/jonah/Documents/Codex/2026-04-28/i-need-you-to-build-a/src/main/java/com/bank/dao/UserDao.java)

## Dependencies and Required Software

- Java 17 or newer
- Maven
- MySQL Server
- Apache Tomcat 10 or newer
- MySQL JDBC driver through Maven dependency `mysql-connector-j`
- JSTL libraries resolved through Maven

## Setup and Run Instructions

1. Create the MySQL database:

```sql
SOURCE C:/Users/jonah/Documents/Codex/2026-04-28/i-need-you-to-build-a/database/create_schema.sql;
SOURCE C:/Users/jonah/Documents/Codex/2026-04-28/i-need-you-to-build-a/database/initialize_data.sql;
```

2. Update database credentials in [`src/main/resources/db.properties`](C:/Users/jonah/Documents/Codex/2026-04-28/i-need-you-to-build-a/src/main/resources/db.properties).

3. Build the WAR file with Maven:

```bash
mvn clean package
```

4. Deploy `target/banking-application.war` to Tomcat’s `webapps` directory.

5. Start Tomcat and open:

```text
http://localhost:8080/banking-application/
```

## Additional Database Configuration

- Default JDBC URL:
  `jdbc:mysql://localhost:3306/banking_app`
- If MySQL uses a different port, schema name, username, or password, update them in [db.properties](C:/Users/jonah/Documents/Codex/2026-04-28/i-need-you-to-build-a/src/main/resources/db.properties).
- If Tomcat is already running an older deployment, stop Tomcat, replace the WAR in `webapps`, and restart it.
- If MySQL scripts are run from another working directory, use the absolute paths shown above.

## Demo login

- Username: `admin`
- Password: `password123`
- Username: `manager`
- Password: `manager123`

## SQL Scripts Included

- [create_schema.sql](C:/Users/jonah/Documents/Codex/2026-04-28/i-need-you-to-build-a/database/create_schema.sql)
  Recreates the database schema, tables, and constraints.
- [initialize_data.sql](C:/Users/jonah/Documents/Codex/2026-04-28/i-need-you-to-build-a/database/initialize_data.sql)
  Populates the tables with at least 15 rows per main table and no `NULL` fields in seeded data.

## Error Handling and Permissions

- Database operations use `try-with-resources` so JDBC connections, statements, and result sets are closed safely.
- Connection and configuration failures throw clear exceptions from [DatabaseConfig.java](C:/Users/jonah/Documents/Codex/2026-04-28/i-need-you-to-build-a/src/main/java/com/bank/config/DatabaseConfig.java) and [ConnectionFactory.java](C:/Users/jonah/Documents/Codex/2026-04-28/i-need-you-to-build-a/src/main/java/com/bank/util/ConnectionFactory.java).
- Service-layer validation throws readable error messages for invalid phone numbers, invalid account numbers, insufficient balance, and inactive accounts.
- The application needs standard read/write access to the project folder for building, and Tomcat needs permission to read the deployed WAR file.

## Notes

- Maven is not installed in this workspace, so the project could not be packaged locally here.
- The code is written to standard Maven webapp conventions so it can be built in a normal Java web environment with Maven and MySQL available.
