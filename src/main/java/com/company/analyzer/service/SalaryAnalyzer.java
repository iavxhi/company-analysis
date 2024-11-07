package com.company.analyzer.service;

import com.company.analyzer.model.Employee;

/**
 * Service class for analyzing salaries.
 */
public class SalaryAnalyzer {

    /**
     * Analyzes the salaries of employees.
     *
     * @param manager the manager employee
     */
    public void analyzeSalaries(Employee manager) {
        if (manager == null) {
            throw new IllegalArgumentException("Manager cannot be null");
        }

        manager.getSubordinates().forEach(this::analyzeSalaries);

        if (manager.getSubordinates().isEmpty()) {
            return;
        }

        double averageSalary = manager.getSubordinates().stream()
            .mapToDouble(Employee::getSalary)
            .average()
            .orElse(0);

        double minimumManagerSalary = averageSalary * 1.2; // 20% increase
        double maximumManagerSalary = averageSalary * 1.5; // 50% increase

        if (minimumManagerSalary > manager.getSalary()) {
            System.out.println(manager.getFirstName() + " " + manager.getLastName() + " earns less than they should by " + (minimumManagerSalary - manager.getSalary()));
        } else if (maximumManagerSalary < manager.getSalary()) {
            System.out.println(manager.getFirstName() + " " + manager.getLastName() + " earns more than they should by " + (manager.getSalary() - maximumManagerSalary));
        }
    }
}