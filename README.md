# Banking Application

This project is a three-tier Java web application for a banking system. It uses JSP, Servlets, JDBC, and MySQL to perform direct SQL-based CRUD operations for customers, accounts, transactions, and loans.

## Architecture

1. Presentation Layer
   JSP pages, HTML, CSS, and JavaScript in `src/main/webapp`
2. Application Layer
   Servlets and service classes in `src/main/java/com/bank/web` and `src/main/java/com/bank/service`
3. Data Layer
   MySQL schema and seed scripts in [`database/schema.sql`](C:/Users/jonah/Documents/Codex/2026-04-28/i-need-you-to-build-a/database/schema.sql) and [`database/seed.sql`](C:/Users/jonah/Documents/Codex/2026-04-28/i-need-you-to-build-a/database/seed.sql)

## Features

- Customer creation, update, listing, and deletion
- Account creation, update, listing, and deletion
- Deposits and withdrawals
- Transfers between accounts
- Transaction log display and deletion
- Loan creation, update, processing by status, listing, and deletion
- Database-backed login and logout with session-based access control

## JDBC and SQL usage

The application uses:

- `DriverManager` through `ConnectionFactory`
- JDBC `Connection`, `PreparedStatement`, and `ResultSet`
- Direct SQL `SELECT`, `INSERT`, `UPDATE`, and `DELETE` statements in DAO classes
- SQL transactions for deposit, withdrawal, and transfer workflows

## Project structure

```text
src/
  main/
    java/com/bank/
      config/
      dao/
      model/
      service/
      util/
      web/
    resources/
      db.properties
    webapp/
      assets/
      WEB-INF/views/dashboard.jsp
database/
  schema.sql
  seed.sql
```

## Setup

1. Create the MySQL database:

```sql
SOURCE database/schema.sql;
SOURCE database/seed.sql;
```

2. Update database credentials in [`src/main/resources/db.properties`](C:/Users/jonah/Documents/Codex/2026-04-28/i-need-you-to-build-a/src/main/resources/db.properties).

3. Build and deploy the WAR with a Jakarta Servlet compatible server such as Tomcat 10+:

```bash
mvn clean package
```

4. Deploy `target/banking-application.war` to your servlet container and open `/banking-application/`.

## Demo login

- Username: `admin`
- Password: `password123`
- Username: `manager`
- Password: `manager123`

## Notes

- Maven is not installed in this workspace, so the project could not be packaged locally here.
- The code is written to standard Maven webapp conventions so it can be built in a normal Java web environment with Maven and MySQL available.
