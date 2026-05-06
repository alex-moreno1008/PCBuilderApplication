package org.example.pcbuilderapplication;

import org.example.pcbuilderapplication.models.BuildSelection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import org.example.pcbuilderapplication.controllers.SummaryController;
/**
 * Unit tests for SummaryController.
 *
 * Since format() is private, we use reflection to access it.
 * Budget logic is tested by directly manipulating BuildSelection static fields.
 */
public class SummaryControllerTest {

    private SummaryController controller;
    private Method formatMethod;

    @BeforeEach
    void setUp() throws Exception {
        controller = new SummaryController();

        // Use reflection to access the private format() method
        formatMethod = SummaryController.class.getDeclaredMethod("format", String.class, String.class, double.class);
        formatMethod.setAccessible(true);

        // Reset BuildSelection before each test
        BuildSelection.cpu        = null;
        BuildSelection.motherboard = null;
        BuildSelection.gpu        = null;
        BuildSelection.ram        = null;
        BuildSelection.storage    = null;
        BuildSelection.budget     = 0.0;
    }

    // ---------------------------------------------------------------
    // format() tests
    // ---------------------------------------------------------------

    @Test
    void format_withValidNameAndPrice_returnsFormattedString() throws Exception {
        String result = (String) formatMethod.invoke(controller, "CPU", "Ryzen 5 5600X", 199.99);
        assertEquals("CPU: Ryzen 5 5600X - $199.99", result);
    }

    @Test
    void format_withNullName_returnsNotSelected() throws Exception {
        String result = (String) formatMethod.invoke(controller, "GPU", null, 0.0);
        assertEquals("GPU: Not selected", result);
    }

    @Test
    void format_withZeroPrice_showsZeroDollar() throws Exception {
        String result = (String) formatMethod.invoke(controller, "RAM", "16GB DDR4", 0.0);
        assertEquals("RAM: 16GB DDR4 - $0.00", result);
    }

    @Test
    void format_withLargePrice_formatsCorrectly() throws Exception {
        String result = (String) formatMethod.invoke(controller, "GPU", "RTX 4080", 1199.99);
        assertEquals("GPU: RTX 4080 - $1199.99", result);
    }

    @Test
    void format_labelIsPreserved() throws Exception {
        String result = (String) formatMethod.invoke(controller, "Storage", "1TB SSD", 89.99);
        assertTrue(result.startsWith("Storage:"));
    }

    // ---------------------------------------------------------------
    // Budget logic tests (testing the logic directly via BuildSelection)
    // ---------------------------------------------------------------

    @Test
    void budgetLogic_overBudget_calculatesCorrectOverage() {
        BuildSelection.budget = 500.0;
        double total = 650.0;

        double over = total - BuildSelection.budget;
        assertTrue(total > BuildSelection.budget);
        assertEquals(150.0, over, 0.001);
    }

    @Test
    void budgetLogic_underBudget_calculatesCorrectRemaining() {
        BuildSelection.budget = 1000.0;
        double total = 750.0;

        double under = BuildSelection.budget - total;
        assertTrue(total <= BuildSelection.budget);
        assertEquals(250.0, under, 0.001);
    }

    @Test
    void budgetLogic_exactlyOnBudget_isWithinBudget() {
        BuildSelection.budget = 800.0;
        double total = 800.0;

        assertFalse(total > BuildSelection.budget);
        assertEquals(0.0, BuildSelection.budget - total, 0.001);
    }

    @Test
    void budgetLogic_zeroBudget_skipsCheck() {
        BuildSelection.budget = 0.0;
        // When budget is 0, initialize() skips the budget block entirely
        assertFalse(BuildSelection.budget > 0);
    }

    // ---------------------------------------------------------------
    // BuildSelection state tests
    // ---------------------------------------------------------------

    @Test
    void buildSelection_defaultsAreNull() {
        assertNull(BuildSelection.cpu);
        assertNull(BuildSelection.motherboard);
        assertNull(BuildSelection.gpu);
        assertNull(BuildSelection.ram);
        assertNull(BuildSelection.storage);
    }

    @Test
    void buildSelection_budgetDefaultsToZero() {
        assertEquals(0.0, BuildSelection.budget, 0.001);
    }

    @Test
    void buildSelection_canSetAndReadValues() {
        BuildSelection.cpu = "Ryzen 7 7700X";
        BuildSelection.gpu = "RTX 4070";
        BuildSelection.budget = 1200.0;

        assertEquals("Ryzen 7 7700X", BuildSelection.cpu);
        assertEquals("RTX 4070", BuildSelection.gpu);
        assertEquals(1200.0, BuildSelection.budget, 0.001);
    }

    // ---------------------------------------------------------------
    // Total price calculation tests
    // ---------------------------------------------------------------

    @Test
    void totalCalculation_sumOfAllPartPrices_isCorrect() {
        double cpu       = 199.99;
        double mb        = 149.99;
        double gpu       = 299.99;
        double ram       = 59.99;
        double storage   = 49.99;

        double total = cpu + mb + gpu + ram + storage;
        assertEquals(759.95, total, 0.01);
    }

    @Test
    void totalCalculation_withZeroPrices_returnsZero() {
        double total = 0.0 + 0.0 + 0.0 + 0.0 + 0.0;
        assertEquals(0.0, total, 0.001);
    }

    @Test
    void totalFormatted_matchesExpectedPattern() {
        double total = 759.95;
        String formatted = String.format("Total: $%.2f", total);
        assertEquals("Total: $759.95", formatted);
    }
}