package com.company.analyzer.service;

import com.company.analyzer.model.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class SalaryAnalyzerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testAnalyzeSalariesManagerUnderpaid() {
        Employee manager = new Employee(123, "Joe", "Doe", 100000, null);
        Employee subordinate1 = new Employee(124, "Martin", "Chekov", 90000, 123);
        Employee subordinate2 = new Employee(125, "Bob", "Ronstad", 80000, 123);
        manager.addSubordinate(subordinate1);
        manager.addSubordinate(subordinate2);

        SalaryAnalyzer salaryAnalyzer = new SalaryAnalyzer();
        salaryAnalyzer.analyzeSalaries(manager);

        String expectedOutput = "Joe Doe earns less than they should by 2000.0\n";
        assertEquals(expectedOutput, outContent.toString(), "Manager underpaid test failed");
    }

    @Test
    public void testAnalyzeSalariesManagerOverpaid() {
        Employee manager = new Employee(123, "Joe", "Doe", 100000, null);
        Employee subordinate1 = new Employee(124, "Martin", "Chekov", 50000, 123);
        Employee subordinate2 = new Employee(125, "Bob", "Ronstad", 50000, 123);
        manager.addSubordinate(subordinate1);
        manager.addSubordinate(subordinate2);

        SalaryAnalyzer salaryAnalyzer = new SalaryAnalyzer();
        salaryAnalyzer.analyzeSalaries(manager);
        String expectedOutput = "Joe Doe earns more than they should by 25000.0\n";


        assertEquals(expectedOutput, outContent.toString(), "Manager overpaid test failed");
    }

    @Test
    public void testAnalyzeSalariesManagerWithinRange() {
        Employee manager = new Employee(123, "Joe", "Doe", 60000, null);
        Employee subordinate1 = new Employee(124, "Martin", "Chekov", 45000, 123);
        Employee subordinate2 = new Employee(125, "Bob", "Ronstad", 47000, 123);
        manager.addSubordinate(subordinate1);
        manager.addSubordinate(subordinate2);

        SalaryAnalyzer salaryAnalyzer = new SalaryAnalyzer();
        salaryAnalyzer.analyzeSalaries(manager);

        String expectedOutput = "";
        assertEquals(expectedOutput, outContent.toString(), "Manager within range test failed");
    }

    @Test
    public void testAnalyzeSalariesManagerNoSubordinates() {
        Employee manager = new Employee(123, "Joe", "Doe", 70000, null);

        SalaryAnalyzer salaryAnalyzer = new SalaryAnalyzer();
        salaryAnalyzer.analyzeSalaries(manager);

        String expectedOutput = "";
        assertEquals(expectedOutput, outContent.toString(), "Manager with no subordinates test failed");
    }
}
