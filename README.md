# Library Management System

A Java desktop library management system built with a Swing client, a socket-based server, and a MySQL database. The application lets users view books, search the catalog, borrow and return books, add new books, and delete books through a simple graphical interface.

## Features

- View all books in a table
- Search books from the client interface
- Borrow available books
- Return borrowed books
- Add new books with title and author
- Delete books from the catalog
- Multi-client server support using one handler thread per connection
- MySQL persistence through JDBC

## Project Structure

```text
.
+-- CLIENT/
|   +-- src/
|       +-- IHM/
|       |   +-- MainFrame.java
|       +-- model/
|       |   +-- Book.java
|       +-- network/
|       |   +-- TestClient.java
|       |   +-- TestClient2.java
|       +-- util/
|           +-- ResponseParser.java
+-- SERVER/
    +-- src/
        +-- DAO/
        |   +-- BookDAO.java
        |   +-- TestDAO.java
        +-- Database/
        |   +-- DatabaseConnection.java
        |   +-- TestConnection.java
        +-- model/
        |   +-- Book.java
        +-- network/
            +-- ClientHandler.java
            +-- Server.java
```

## Technologies Used

- Java
- Java Swing
- Java Sockets
- JDBC
- MySQL
- IntelliJ IDEA project modules

## Requirements

- JDK 8 or newer
- MySQL Server
- MySQL Connector/J
- IntelliJ IDEA, or another Java IDE

The server module currently references MySQL Connector/J from a local path in `SERVER/SERVER.iml`. If you open this project on another machine, update the connector path or add the JDBC driver again in your IDE.

## Database Setup

Create a MySQL database named `library_db`:

```sql
CREATE DATABASE library_db;
USE library_db;
```

Create the `book` table:

```sql
CREATE TABLE book (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE
);
```

Optional sample data:

```sql
INSERT INTO book (title, author, available) VALUES
('Clean Code', 'Robert C. Martin', TRUE),
('Effective Java', 'Joshua Bloch', TRUE),
('The Pragmatic Programmer', 'Andrew Hunt and David Thomas', TRUE);
```

Database credentials are configured in:

```text
SERVER/src/Database/DatabaseConnection.java
```

Default configuration:

```java
private static final String URL = "jdbc:mysql://localhost:3306/library_db";
private static final String USER = "root";
private static final String PASSWORD = "";
```

Update these values if your local MySQL username or password is different.

## How to Run

1. Start MySQL and make sure the `library_db` database exists.
2. Open the project in IntelliJ IDEA.
3. Add MySQL Connector/J to the `SERVER` module dependencies if it is not already configured.
4. Run the server:

```text
SERVER/src/network/Server.java
```

The server listens on:

```text
localhost:1234
```

5. Run the client GUI:

```text
CLIENT/src/IHM/MainFrame.java
```

6. Click **Load Books** to fetch books from the server.

## Client-Server Commands

The client and server communicate through simple text commands over TCP sockets.

| Command | Description |
| --- | --- |
| `GET_BOOKS` | Fetch all books |
| `BORROW|id` | Mark a book as borrowed |
| `RETURN|id` | Mark a book as returned |
| `ADD|title|author` | Add a new book |
| `DELETE|id` | Delete a book |

Example server response for a book list:
```
BOOK_LIST|1,Clean Code,Robert C. Martin,true;2,Effective Java,Joshua Bloch,true;
```

## Main Classes

- `Server.java` starts the socket server on port `1234`.
- `ClientHandler.java` handles client requests and sends responses.
- `BookDAO.java` contains database operations for books.
- `DatabaseConnection.java` manages the MySQL connection.
- `MainFrame.java` provides the Swing user interface.
- `ResponseParser.java` converts server responses into `Book` objects.

## Notes

- The server must be running before the client can load, add, borrow, return, or delete books.
- The current socket protocol separates book fields with commas, so book titles or author names containing commas may not parse correctly.
- Compiled `.class` files are present in the `out/` directories. For GitHub, it is usually better to ignore generated build output and commit only the source files and project configuration.

![image](https://github.com/khaoukhaou4869-jpg/Management-Library-Client-Server-/blob/ca4a45c9f4670a56b0efccf9b1fecbbd4a0c832f/2026-06-30%20031801.png)
