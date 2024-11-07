package com.company;

import com.company.analyzer.model.Employee;
import com.company.analyzer.service.EmployeeService;
import com.company.analyzer.service.SalaryAnalyzer;
import com.company.analyzer.service.ReportingLineAnalyzer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Main class for running the application.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            printUsage();
            System.exit(1);
        }

        String option = args[0];
        String value = args[1];
        InputStream inputStream = null;

        switch (option) {
            case "--file":
            case "-f":
                // Read from the specified file path
                Path filePath = Paths.get(value);
                try {
                    inputStream = Files.newInputStream(filePath);
                } catch (IOException e) {
                    System.err.println("Error reading file: " + filePath);
                    e.printStackTrace();
                    System.exit(1);
                }
                break;
            case "--classpath":
            case "-c":
                // Read from the classpath
                inputStream = Main.class.getClassLoader().getResourceAsStream(value);
                if (inputStream == null) {
                    System.err.println("File not found in classpath: " + value);
                    System.exit(1);
                }
                break;
            default:
                printUsage();
                System.exit(1);
        }
        try {
            analyze(inputStream);
        } catch (IOException e) {
            System.err.println("Error analyzing employees");
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

    }

    private static void analyze(InputStream inputStream) throws IOException {
        EmployeeService employeeService = new EmployeeService();
        List<Employee> employees = employeeService.readEmployeesFromInputStream(inputStream);
        Map<Integer, Employee> employeeMap = employeeService.buildEmployeeMap(employees);
        Employee ceo = employeeService.findCEO(employees);
        employeeService.buildHierarchy(ceo, employeeMap);

        SalaryAnalyzer salaryAnalyzer = new SalaryAnalyzer();
        salaryAnalyzer.analyzeSalaries(ceo);

        ReportingLineAnalyzer reportingLineAnalyzer = new ReportingLineAnalyzer();
        reportingLineAnalyzer.analyzeReportingLines(ceo, 0);
    }

    private static void printUsage() {
        System.out.println(
                "Usage: java -jar company-analysis-1.0.jar [--file <path-to-csv-file> | --classpath <resource-name>]");
        System.out.println("Options:");
        System.out.println("  --file, -f       Path to the CSV file");
        System.out.println("  --classpath, -c  Classpath resource name");
    }
}