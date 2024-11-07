package com.company.analyzer.service;

import com.company.analyzer.converter.EmployeeConverter;
import com.company.analyzer.model.Employee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing employees.
 */
public class EmployeeService {

    /**
     * Reads employees from a CSV file.
     *
     * @param inputStream the input stream of the CSV file
     * @return a list of employees
     * @throws IOException if an I/O error occurs
     */
    public List<Employee> readEmployeesFromInputStream(InputStream inputStream) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            List<String> lines = br.lines().collect(Collectors.toList());
            if (lines.size() > 1001) { // 1000 lines + 1 header
                throw new IOException("File contains more than 1000 lines");
            }
            return lines.stream()
                        .skip(1) // Skip header
                        .map(EmployeeConverter::fromString)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IOException("Error reading employees from input stream", e);
        }
    }

    /**
     * Builds a map of employees by their ID.
     *
     * @param employees the list of employees
     * @return a map of employees by ID
     */
    public Map<Integer, Employee> buildEmployeeMap(List<Employee> employees) {
        return employees.stream()
                        .collect(Collectors.toMap(Employee::getId, employee -> employee));
    }

    /**
     * Finds the CEO of the company.
     *
     * @param employees the list of employees
     * @return the CEO employee
     * @throws IllegalStateException if the CEO is not found
     */
    public Employee findCEO(List<Employee> employees) {
        return employees.stream()
                        .filter(employee -> employee.getManagerId() == null)
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("CEO not found"));
    }

    /**
     * Builds the organizational hierarchy.
     *
     * @param manager the manager employee
     * @param employeeMap the map of employees by ID
     */
    public void buildHierarchy(Employee manager, Map<Integer, Employee> employeeMap) {
        employeeMap.values().stream()
                   .filter(employee -> Objects.equals(manager.getId(), employee.getManagerId()))
                   .forEach(employee -> {
                       manager.addSubordinate(employee);
                       buildHierarchy(employee, employeeMap);
                   });
    }
}
