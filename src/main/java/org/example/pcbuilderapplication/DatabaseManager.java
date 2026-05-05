package org.example.pcbuilderapplication;

import org.example.pcbuilderapplication.models.BuildSelection;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        socket TEXT,
        ram_type TEXT
    );
    """;

        String savedBuildsTable = """
        CREATE TABLE IF NOT EXISTS saved_builds (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        user_id INTEGER NOT NULL,
        build_name TEXT NOT NULL,
        cpu TEXT,
        motherboard TEXT,
        gpu TEXT,
        ram TEXT,
        storage TEXT,
        total_price REAL,
        FOREIGN KEY (user_id) REFERENCES users(id)
    );
    """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(usersTable);
            stmt.execute(partsTable);
            stmt.execute(savedBuildsTable);
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

        String insertSql = "INSERT INTO parts (category, name, brand, price, socket, ram_type) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {

            // CPUs
            insertPart(pstmt, "CPU", "Ryzen 5 5600X", "AMD", 199.99, "AM4", null);
            insertPart(pstmt, "CPU", "Ryzen 7 7700X", "AMD", 329.99, "AM5", null);
            insertPart(pstmt, "CPU", "Ryzen 9 7900X", "AMD", 429.99, "AM5", null);
            insertPart(pstmt, "CPU", "Intel i5-12400F", "Intel", 179.99, "LGA1700", null);
            insertPart(pstmt, "CPU", "Intel i7-12700K", "Intel", 299.99, "LGA1700", null);
            insertPart(pstmt, "CPU", "Intel i9-12900K", "Intel", 399.99, "LGA1700", null);

            // Motherboards (ADD RAM TYPE HERE)
            insertPart(pstmt, "Motherboard", "MSI B550", "MSI", 149.99, "AM4", "DDR4");
            insertPart(pstmt, "Motherboard", "ASUS X570", "ASUS", 219.99, "AM4", "DDR4");
            insertPart(pstmt, "Motherboard", "ASUS X670", "ASUS", 249.99, "AM5", "DDR5");
            insertPart(pstmt, "Motherboard", "MSI B650", "MSI", 189.99, "AM5", "DDR5");
            insertPart(pstmt, "Motherboard", "Gigabyte B660", "Gigabyte", 159.99, "LGA1700", "DDR4");
            insertPart(pstmt, "Motherboard", "ASUS Z790", "ASUS", 279.99, "LGA1700", "DDR5");

            // GPUs
            insertPart(pstmt, "GPU", "RTX 4060", "NVIDIA", 299.99, null, null);
            insertPart(pstmt, "GPU", "RTX 4070", "NVIDIA", 549.99, null, null);
            insertPart(pstmt, "GPU", "RTX 4080", "NVIDIA", 1199.99, null, null);
            insertPart(pstmt, "GPU", "RX 7600", "AMD", 269.99, null, null);
            insertPart(pstmt, "GPU", "RX 7700 XT", "AMD", 399.99, null, null);
            insertPart(pstmt, "GPU", "RX 7800 XT", "AMD", 499.99, null, null);

            // RAM
            insertPart(pstmt, "RAM", "16GB DDR4", "Corsair", 59.99, null, "DDR4");
            insertPart(pstmt, "RAM", "32GB DDR4", "G.Skill", 109.99, null, "DDR4");
            insertPart(pstmt, "RAM", "16GB DDR5", "Corsair", 89.99, null, "DDR5");
            insertPart(pstmt, "RAM", "32GB DDR5", "G.Skill", 149.99, null, "DDR5");
            insertPart(pstmt, "RAM", "64GB DDR5", "Corsair", 249.99, null, "DDR5");

            // Storage
            insertPart(pstmt, "Storage", "500GB SSD", "Samsung", 49.99, null, null);
            insertPart(pstmt, "Storage", "1TB SSD", "Samsung", 89.99, null, null);
            insertPart(pstmt, "Storage", "2TB SSD", "Crucial", 149.99, null, null);
            insertPart(pstmt, "Storage", "1TB NVMe", "WD", 109.99, null, null);
            insertPart(pstmt, "Storage", "2TB NVMe", "Samsung", 179.99, null, null);
        } catch (SQLException e) {
            System.err.println("seedParts failed: " + e.getMessage());
        }
    }

    private void insertPart(PreparedStatement pstmt, String category, String name,
                            String brand, double price, String socket, String ramType) throws SQLException {
        pstmt.setString(1, category);
        pstmt.setString(2, name);
        pstmt.setString(3, brand);
        pstmt.setDouble(4, price);
        pstmt.setString(5, socket);
        pstmt.setString(6, ramType);
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

    public String getSocketByPartName(String partName) {
        String sql = "SELECT socket FROM parts WHERE name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, partName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("socket");
            }
        } catch (SQLException e) {
            System.err.println("getSocketByPartName failed: " + e.getMessage());
        }

        return null;
    }

    public String getRamTypeByPartName(String partName) {
        String sql = "SELECT ram_type FROM parts WHERE name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, partName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("ram_type");
            }
        } catch (SQLException e) {
            System.err.println("getRamTypeByPartName failed: " + e.getMessage());
        }

        return null;
    }

    public void saveBuild(int userId, String buildName, String cpu, String motherboard,
                          String gpu, String ram, String storage, double total) {
        String sql = "INSERT INTO saved_builds (user_id, build_name, cpu, motherboard, gpu, ram, storage, total_price) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setInt(1, userId);
            p.setString(2, buildName);
            p.setString(3, cpu);
            p.setString(4, motherboard);
            p.setString(5, gpu);
            p.setString(6, ram);
            p.setString(7, storage);
            p.setDouble(8, total);
            p.executeUpdate();
        } catch (SQLException e) {
            System.err.println("saveBuild failed: " + e.getMessage());
        }
    }

    public Map<Integer, String> getSavedBuildSummaries(int userId) {
        Map<Integer, String> results = new LinkedHashMap<>();
        String sql = "SELECT id, build_name, cpu, gpu, total_price FROM saved_builds WHERE user_id = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setInt(1, userId);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                results.put(rs.getInt("id"), String.format("[%s] CPU: %s | GPU: %s | $%.2f",
                        rs.getString("build_name"),
                        rs.getString("cpu"),
                        rs.getString("gpu"),
                        rs.getDouble("total_price")));
            }
        } catch (SQLException e) {
            System.err.println("getSavedBuildSummaries failed: " + e.getMessage());
        }
        return results;
    }

    public int getUserId(String username, String password) {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setString(1, username);
            p.setString(2, password);
            ResultSet rs = p.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            System.err.println("getUserId failed: " + e.getMessage());
        }
        return -1;
    }

    public void deleteBuild(int buildId) {
        String sql = "DELETE FROM saved_builds WHERE id = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setInt(1, buildId);
            p.executeUpdate();
        } catch (SQLException e) {
            System.err.println("deleteBuild failed: " + e.getMessage());
        }
    }

    public BuildSelection getBuildById(int buildId) {
        String sql = "SELECT * FROM saved_builds WHERE id = ?";
        try (PreparedStatement p = connection.prepareStatement(sql)) {
            p.setInt(1, buildId);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                BuildSelection build = new BuildSelection();
                build.cpu = rs.getString("cpu");
                build.motherboard = rs.getString("motherboard");
                build.gpu = rs.getString("gpu");
                build.ram = rs.getString("ram");
                build.storage = rs.getString("storage");
                return build;
            }
        } catch (SQLException e) {
            System.err.println("getBuildById failed: " + e.getMessage());
        }
        return null;
    }
}