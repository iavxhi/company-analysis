package com.company.analyzer.service;

import com.company.analyzer.model.Employee;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeServiceTest {

    @Test
    public void testReadEmployeesFromInputStream() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("valid_employees.csv");
        EmployeeService employeeService = new EmployeeService();
        List<Employee> employees = employeeService.readEmployeesFromInputStream(inputStream);
        assertEquals(5, employees.size());
    }

    @Test
    public void testReadEmployeesFromInputStreamWithMissingManagerId() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("missing_manager_id.csv");
        EmployeeService employeeService = new EmployeeService();
        List<Employee> employees = employeeService.readEmployeesFromInputStream(inputStream);
        assertEquals(5, employees.size());
    }

    @Test
    public void testReadEmployeesFromInputStreamWithInvalidData() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("invalid_data.csv");
        EmployeeService employeeService = new EmployeeService();
        assertThrows(NumberFormatException.class, () -> employeeService.readEmployeesFromInputStream(inputStream));
    }

    @Test
    public void testReadEmployeesFromInputStreamWithMoreThan1000Lines() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("employees_1001.csv");
        EmployeeService employeeService = new EmployeeService();
        assertThrows(IOException.class, () -> employeeService.readEmployeesFromInputStream(inputStream));
    }

    @Test
    public void testReadEmployeesFromInputStreamWithCircularReference() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("circular_reference.csv");
        EmployeeService employeeService = new EmployeeService();
        List<Employee> employees = employeeService.readEmployeesFromInputStream(inputStream);
        assertEquals(5, employees.size());
    }

    @Test
    public void testReadEmployeesFromInputStreamWithDeepHierarchy() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("deep_hierarchy.csv");
        EmployeeService employeeService = new EmployeeService();
        List<Employee> employees = employeeService.readEmployeesFromInputStream(inputStream);
        assertEquals(7, employees.size());
    }

    @Test
    public void testReadEmployeesFromInputStreamWithMultipleCEOs() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("multiple_ceos.csv");
        EmployeeService employeeService = new EmployeeService();
        List<Employee> employees = employeeService.readEmployeesFromInputStream(inputStream);
        assertEquals(5, employees.size());
    }

    @Test
    public void testReadEmployeesFromInputStreamWithLargeFile() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("employees_1000.csv");
        EmployeeService employeeService = new EmployeeService();
        List<Employee> employees = employeeService.readEmployeesFromInputStream(inputStream);
        assertEquals(1000, employees.size());
    }

    @Test
    public void testBuildEmployeeMap() {
        List<Employee> employees = List.of(
                new Employee(123, "Joe", "Doe", 60000, null),
                new Employee(124, "Martin", "Chekov", 45000, 123)
        );
        EmployeeService employeeService = new EmployeeService();
        Map<Integer, Employee> employeeMap = employeeService.buildEmployeeMap(employees);
        assertEquals(2, employeeMap.size());
        assertTrue(employeeMap.containsKey(123));
        assertTrue(employeeMap.containsKey(124));
    }

    @Test
    public void testFindCEO() {
        List<Employee> employees = List.of(
                new Employee(123, "Joe", "Doe", 60000, null),
                new Employee(124, "Martin", "Chekov", 45000, 123)
        );
        EmployeeService employeeService = new EmployeeService();
        Employee ceo = employeeService.findCEO(employees);
        assertNotNull(ceo);
        assertEquals(123, ceo.getId());
    }

    @Test
    public void testFindCEONotFound() {
        List<Employee> employees = List.of(
                new Employee(124, "Martin", "Chekov", 45000, 123)
        );
        EmployeeService employeeService = new EmployeeService();
        assertThrows(IllegalStateException.class, () -> employeeService.findCEO(employees));
    }

    @Test
    public void testBuildHierarchy() {
        List<Employee> employees = List.of(
                new Employee(123, "Joe", "Doe", 60000, null),
                new Employee(124, "Martin", "Chekov", 45000, 123),
                new Employee(125, "Bob", "Ronstad", 47000, 124)
        );
        EmployeeService employeeService = new EmployeeService();
        Map<Integer, Employee> employeeMap = employeeService.buildEmployeeMap(employees);
        Employee ceo = employeeService.findCEO(employees);
        employeeService.buildHierarchy(ceo, employeeMap);
        assertEquals(1, ceo.getSubordinates().size());
        assertEquals(124, ceo.getSubordinates().get(0).getId());
        assertEquals(1, ceo.getSubordinates().get(0).getSubordinates().size());
        assertEquals(125, ceo.getSubordinates().get(0).getSubordinates().get(0).getId());
    }
}
