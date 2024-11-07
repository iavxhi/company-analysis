package com.company.analyzer.converter;

import com.company.analyzer.model.CsvColumn;
import com.company.analyzer.model.Employee;

public class EmployeeConverter {
    public static String SEPARATOR = ",";

    public static Employee fromString(String csvLine) {
        String[] values = csvLine.split(SEPARATOR);
        try {
            int id = Integer.parseInt(values[CsvColumn.ID.getIndex()]);
            String firstName = values[CsvColumn.FIRST_NAME.getIndex()];
            String lastName = values[CsvColumn.LAST_NAME.getIndex()];
            double salary = Double.parseDouble(values[CsvColumn.SALARY.getIndex()]);
            Integer managerId = values.length > CsvColumn.MANAGER_ID.getIndex() && !values[CsvColumn.MANAGER_ID.getIndex()].isEmpty() ? Integer.parseInt(values[CsvColumn.MANAGER_ID.getIndex()]) : null;
            Employee employee = new Employee(id, firstName, lastName, salary, managerId);
            return employee;
        } catch (Exception e) {
            System.err.println("Error parsing employee: " + csvLine);
            throw e;
        }

    }
}
