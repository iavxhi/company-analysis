package com.company.analyzer.service;

import com.company.analyzer.model.Employee;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ReportingLineAnalyzerTest {
    @Test
    public void testAnalyzeReportingLinesTooLong() {
        Employee ceo = new Employee(123, "Joe", "Doe", 60000, null);
        Employee manager1 = new Employee(124, "Martin", "Chekov", 45000, 123);
        Employee manager2 = new Employee(125, "Bob", "Ronstad", 47000, 124);
        Employee manager3 = new Employee(126, "Alice", "Hasacat", 50000, 125);
        Employee manager4 = new Employee(127, "Brett", "Hardleaf", 34000, 126);
        Employee employee = new Employee(128, "John", "Smith", 30000, 127);

        ceo.addSubordinate(manager1);
        manager1.addSubordinate(manager2);
        manager2.addSubordinate(manager3);
        manager3.addSubordinate(manager4);
        manager4.addSubordinate(employee);

        ReportingLineAnalyzer reportingLineAnalyzer = new ReportingLineAnalyzer();

        // Capture the output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        reportingLineAnalyzer.analyzeReportingLines(ceo, 0);

        // Check the output
        String expectedOutput = "John Smith has a reporting line which is too long by 1\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testAnalyzeReportingLinesWithinLimit() {
        Employee ceo = new Employee(123, "Joe", "Doe", 60000, null);
        Employee manager1 = new Employee(124, "Martin", "Chekov", 45000, 123);
        Employee manager2 = new Employee(125, "Bob", "Ronstad", 47000, 124);
        Employee manager3 = new Employee(126, "Alice", "Hasacat", 50000, 125);
        Employee employee = new Employee(127, "John", "Smith", 30000, 126);

        ceo.addSubordinate(manager1);
        manager1.addSubordinate(manager2);
        manager2.addSubordinate(manager3);
        manager3.addSubordinate(employee);

        ReportingLineAnalyzer reportingLineAnalyzer = new ReportingLineAnalyzer();

        // Capture the output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        reportingLineAnalyzer.analyzeReportingLines(ceo, 0);

        // Check the output
        String expectedOutput = "";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testAnalyzeReportingLinesNoSubordinates() {
        Employee ceo = new Employee(123, "Joe", "Doe", 60000, null);

        ReportingLineAnalyzer reportingLineAnalyzer = new ReportingLineAnalyzer();

        // Capture the output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        reportingLineAnalyzer.analyzeReportingLines(ceo, 0);

        // Check the output
        String expectedOutput = "";
        assertEquals(expectedOutput, outContent.toString());
    }
}
