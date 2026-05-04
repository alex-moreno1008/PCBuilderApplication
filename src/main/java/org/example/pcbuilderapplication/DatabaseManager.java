package org.example.pcbuilderapplication;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:app.db";
    private Connection connection;

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database connected.");
            createTables();
            seedParts();
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }

    private void createTables() {
        String usersTable = """
        CREATE TABLE IF NOT EXISTS users (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        username TEXT NOT NULL,
        password TEXT NOT NULL
    );
    """;

        String partsTable = """
        CREATE TABLE IF NOT EXISTS parts (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        category TEXT NOT NULL,
        name TEXT NOT NULL,
        brand TEXT,
        price REAL NOT NULL,
        socket TEXT
    );
    """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(usersTable);
            stmt.execute(partsTable);
        } catch (SQLException e) {
            System.err.println("createTables failed: " + e.getMessage());
        }
    }

    private void seedParts() {
        String countSql = "SELECT COUNT(*) FROM parts";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(countSql)) {

            if (rs.next() && rs.getInt(1) > 0) {
                return; // already seeded
            }

        } catch (SQLException e) {
            System.err.println("seedParts check failed: " + e.getMessage());
        }

        String insertSql = "INSERT INTO parts (category, name, brand, price, socket) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {

            // CPUs
            insertPart(pstmt, "CPU", "Ryzen 5 5600X", "AMD", 199.99, "AM4");
            insertPart(pstmt, "CPU", "Ryzen 7 7700X", "AMD", 329.99, "AM5");
            insertPart(pstmt, "CPU", "Ryzen 9 7900X", "AMD", 429.99, "AM5");
            insertPart(pstmt, "CPU", "Intel i5-12400F", "Intel", 179.99, "LGA1700");
            insertPart(pstmt, "CPU", "Intel i7-12700K", "Intel", 299.99, "LGA1700");
            insertPart(pstmt, "CPU", "Intel i9-12900K", "Intel", 399.99, "LGA1700");

            // Motherboards
            insertPart(pstmt, "Motherboard", "MSI B550", "MSI", 149.99, "AM4");
            insertPart(pstmt, "Motherboard", "ASUS X570", "ASUS", 219.99, "AM4");
            insertPart(pstmt, "Motherboard", "ASUS X670", "ASUS", 249.99, "AM5");
            insertPart(pstmt, "Motherboard", "MSI B650", "MSI", 189.99, "AM5");
            insertPart(pstmt, "Motherboard", "Gigabyte B660", "Gigabyte", 159.99, "LGA1700");
            insertPart(pstmt, "Motherboard", "ASUS Z790", "ASUS", 279.99, "LGA1700");

            // GPUs
            insertPart(pstmt, "GPU", "RTX 4060", "NVIDIA", 299.99, null);
            insertPart(pstmt, "GPU", "RTX 4070", "NVIDIA", 549.99, null);
            insertPart(pstmt, "GPU", "RTX 4080", "NVIDIA", 1199.99, null);
            insertPart(pstmt, "GPU", "RX 7600", "AMD", 269.99, null);
            insertPart(pstmt, "GPU", "RX 7700 XT", "AMD", 399.99, null);
            insertPart(pstmt, "GPU", "RX 7800 XT", "AMD", 499.99, null);

            // RAM
            insertPart(pstmt, "RAM", "16GB DDR4", "Corsair", 59.99, null);
            insertPart(pstmt, "RAM", "32GB DDR4", "G.Skill", 109.99, null);
            insertPart(pstmt, "RAM", "16GB DDR5", "Corsair", 89.99, null);
            insertPart(pstmt, "RAM", "32GB DDR5", "G.Skill", 149.99, null);
            insertPart(pstmt, "RAM", "64GB DDR5", "Corsair", 249.99, null);

            // Storage
            insertPart(pstmt, "Storage", "500GB SSD", "Samsung", 49.99, null);
            insertPart(pstmt, "Storage", "1TB SSD", "Samsung", 89.99, null);
            insertPart(pstmt, "Storage", "2TB SSD", "Crucial", 149.99, null);
            insertPart(pstmt, "Storage", "1TB NVMe", "WD", 109.99, null);
            insertPart(pstmt, "Storage", "2TB NVMe", "Samsung", 179.99, null);

        } catch (SQLException e) {
            System.err.println("seedParts failed: " + e.getMessage());
        }
    }

    private void insertPart(PreparedStatement pstmt, String category, String name,
                            String brand, double price, String socket) throws SQLException {
        pstmt.setString(1, category);
        pstmt.setString(2, name);
        pstmt.setString(3, brand);
        pstmt.setDouble(4, price);
        pstmt.setString(5, socket);
        pstmt.executeUpdate();
    }

    public List<String> getPartsByCategory(String category) {
        List<String> parts = new ArrayList<>();
        String sql = "SELECT name FROM parts WHERE category = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                parts.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("getPartsByCategory failed: " + e.getMessage());
        }

        return parts;
    }

    public double getPartPrice(String partName) {
        String sql = "SELECT price FROM parts WHERE name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, partName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("price");
            }
        } catch (SQLException e) {
            System.err.println("getPartPrice failed: " + e.getMessage());
        }

        return 0.0;
    }

    public void insertUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("insertUser failed: " + e.getMessage());
        }
    }

    public boolean userExists(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("userExists failed: " + e.getMessage());
        }

        return false;
    }
}