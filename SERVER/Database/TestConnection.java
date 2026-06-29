package Database;

import Database.DatabaseConnection;
import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        Connection conn = DatabaseConnection.getConnection();

        if (conn != null) {
            System.out.println("✅ Connected successfully!");
        } else {
            System.out.println("❌ Connection failed!");
        }
    }
}