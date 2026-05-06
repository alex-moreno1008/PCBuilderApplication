package org.example.pcbuilderapplication;

import org.example.pcbuilderapplication.models.BuildSelection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DatabaseManager.
 *
 * These tests use the real SQLite app.db database.
 * Make sure app.db is present in the project root before running.
 */
public class DatabaseManagerTest {

    private DatabaseManager db;

    @BeforeEach
    void setUp() {
        db = new DatabaseManager();
    }

    // ---------------------------------------------------------------
    // getPartPrice() tests
    // ---------------------------------------------------------------

    @Test
    void getPartPrice_knownPart_returnsCorrectPrice() {
        double price = db.getPartPrice("Ryzen 5 5600X");
        assertEquals(199.99, price, 0.01);
    }

    @Test
    void getPartPrice_unknownPart_returnsZero() {
        double price = db.getPartPrice("FakePart XYZ");
        assertEquals(0.0, price, 0.001);
    }

    @Test
    void getPartPrice_nullPart_returnsZero() {
        double price = db.getPartPrice(null);
        assertEquals(0.0, price, 0.001);
    }

    @Test
    void getPartPrice_gpuPart_returnsCorrectPrice() {
        double price = db.getPartPrice("RTX 4060");
        assertEquals(299.99, price, 0.01);
    }

    // ---------------------------------------------------------------
    // getPartsByCategory() tests
    // ---------------------------------------------------------------

    @Test
    void getPartsByCategory_cpu_returnsNonEmptyList() {
        List<String> cpus = db.getPartsByCategory("CPU");
        assertNotNull(cpus);
        assertFalse(cpus.isEmpty());
    }

    @Test
    void getPartsByCategory_cpu_containsExpectedPart() {
        List<String> cpus = db.getPartsByCategory("CPU");
        assertTrue(cpus.contains("Ryzen 5 5600X"));
    }

    @Test
    void getPartsByCategory_gpu_containsExpectedPart() {
        List<String> gpus = db.getPartsByCategory("GPU");
        assertTrue(gpus.contains("RTX 4070"));
    }

    @Test
    void getPartsByCategory_invalidCategory_returnsEmptyList() {
        List<String> parts = db.getPartsByCategory("INVALID");
        assertNotNull(parts);
        assertTrue(parts.isEmpty());
    }

    @Test
    void getPartsByCategory_ram_returnsMultipleResults() {
        List<String> ram = db.getPartsByCategory("RAM");
        assertTrue(ram.size() >= 2);
    }

    // ---------------------------------------------------------------
    // getSocketByPartName() tests
    // ---------------------------------------------------------------

    @Test
    void getSocketByPartName_amdCpu_returnsAM4() {
        String socket = db.getSocketByPartName("Ryzen 5 5600X");
        assertEquals("AM4", socket);
    }

    @Test
    void getSocketByPartName_intelCpu_returnsLGA1700() {
        String socket = db.getSocketByPartName("Intel i5-12400F");
        assertEquals("LGA1700", socket);
    }

    @Test
    void getSocketByPartName_unknownPart_returnsNull() {
        String socket = db.getSocketByPartName("FakePart");
        assertNull(socket);
    }

    @Test
    void getSocketByPartName_gpu_returnsNull() {
        // GPUs don't have a socket type
        String socket = db.getSocketByPartName("RTX 4060");
        assertNull(socket);
    }

    // ---------------------------------------------------------------
    // getRamTypeByPartName() tests
    // ---------------------------------------------------------------

    @Test
    void getRamTypeByPartName_ddr4Ram_returnsDDR4() {
        String ramType = db.getRamTypeByPartName("16GB DDR4");
        assertEquals("DDR4", ramType);
    }

    @Test
    void getRamTypeByPartName_ddr5Ram_returnsDDR5() {
        String ramType = db.getRamTypeByPartName("16GB DDR5");
        assertEquals("DDR5", ramType);
    }

    @Test
    void getRamTypeByPartName_unknownPart_returnsNull() {
        String ramType = db.getRamTypeByPartName("FakePart");
        assertNull(ramType);
    }

    // ---------------------------------------------------------------
    // userExists() and insertUser() tests
    // ---------------------------------------------------------------

    @Test
    void insertUser_thenUserExists_returnsTrue() {
        String username = "testuser_" + System.currentTimeMillis();
        String password = "testpass";

        db.insertUser(username, password);
        assertTrue(db.userExists(username, password));
    }

    @Test
    void userExists_wrongPassword_returnsFalse() {
        String username = "testuser2_" + System.currentTimeMillis();
        db.insertUser(username, "correctpass");

        assertFalse(db.userExists(username, "wrongpass"));
    }

    @Test
    void userExists_nonExistentUser_returnsFalse() {
        assertFalse(db.userExists("ghost_user_xyz", "nopass"));
    }

    // ---------------------------------------------------------------
    // getUserId() tests
    // ---------------------------------------------------------------

    @Test
    void getUserId_existingUser_returnsPositiveId() {
        String username = "idtestuser_" + System.currentTimeMillis();
        db.insertUser(username, "pass123");

        int id = db.getUserId(username, "pass123");
        assertTrue(id > 0);
    }

    @Test
    void getUserId_nonExistentUser_returnsNegativeOne() {
        int id = db.getUserId("nobody_xyz", "nopass");
        assertEquals(-1, id);
    }

    // ---------------------------------------------------------------
    // saveBuild() and getSavedBuildSummaries() tests
    // ---------------------------------------------------------------

    @Test
    void saveBuild_thenSummariesContainsBuild() {
        String username = "builduser_" + System.currentTimeMillis();
        db.insertUser(username, "pass");
        int userId = db.getUserId(username, "pass");

        String buildName = "TestBuild_" + System.currentTimeMillis();
        db.saveBuild(userId, buildName, "Ryzen 5 5600X", "MSI B550",
                "RTX 4060", "16GB DDR4", "1TB SSD", 799.95);

        Map<Integer, String> summaries = db.getSavedBuildSummaries(userId);
        assertFalse(summaries.isEmpty());

        boolean found = summaries.values().stream()
                .anyMatch(s -> s.contains(buildName));
        assertTrue(found);
    }

    @Test
    void deleteBuild_removesFromSummaries() {
        String username = "deluser_" + System.currentTimeMillis();
        db.insertUser(username, "pass");
        int userId = db.getUserId(username, "pass");

        String buildName = "DeleteBuild_" + System.currentTimeMillis();
        db.saveBuild(userId, buildName, "Ryzen 7 7700X", "ASUS X670",
                "RTX 4070", "32GB DDR5", "2TB NVMe", 1309.95);

        Map<Integer, String> before = db.getSavedBuildSummaries(userId);
        int buildId = before.keySet().iterator().next();

        db.deleteBuild(buildId);

        Map<Integer, String> after = db.getSavedBuildSummaries(userId);
        assertFalse(after.containsKey(buildId));
    }

    // ---------------------------------------------------------------
    // getBuildById() tests
    // ---------------------------------------------------------------

    @Test
    void getBuildById_existingBuild_populatesBuildSelection() {
        String username = "getbyid_" + System.currentTimeMillis();
        db.insertUser(username, "pass");
        int userId = db.getUserId(username, "pass");

        db.saveBuild(userId, "MyBuild", "Intel i7-12700K", "Gigabyte B660",
                "RX 7700 XT", "32GB DDR4", "2TB SSD", 1009.95);

        Map<Integer, String> summaries = db.getSavedBuildSummaries(userId);
        int buildId = summaries.keySet().iterator().next();

        BuildSelection result = db.getBuildById(buildId);
        assertNotNull(result);
        assertEquals("Intel i7-12700K", BuildSelection.cpu);
        assertEquals("Gigabyte B660", BuildSelection.motherboard);
        assertEquals("RX 7700 XT", BuildSelection.gpu);
    }

    @Test
    void getBuildById_nonExistentId_returnsNull() {
        BuildSelection result = db.getBuildById(-999);
        assertNull(result);
    }
}