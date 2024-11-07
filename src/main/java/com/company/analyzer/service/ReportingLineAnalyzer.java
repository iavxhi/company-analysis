package com.company.analyzer.service;

import com.company.analyzer.model.Employee;

/**
 * Service class for analyzing reporting lines.
 */
public class ReportingLineAnalyzer {

    /**
     * Analyzes the reporting lines of employees.
     *
     * @param employee the employee
     * @param depth the current depth of the reporting line
     */
    public void analyzeReportingLines(Employee employee, int depth) {
        if (depth > 4) {
            System.out.println(employee.getFirstName() + " " + employee.getLastName() + " has a reporting line which is too long by " + (depth - 4));
        }
        employee.getSubordinates().forEach(subordinate -> analyzeReportingLines(subordinate, depth + 1));
    }
}