# Company Analysis

## Overview

This application analyzes the organizational structure of a company based on employee data provided in a CSV file. It reports:
- Managers who earn significantly more or less than their subordinates.
- Employees with more than four managerial levels between them and the CEO.

## Assumptions

- The CSV file includes a header row.
- The CEO is the only employee without a manager.
- Salary analysis ensures a manager earns at least 20% but no more than 50% more than the average salary of their direct subordinates.

## Requirements

- Java SE 21
- Maven for project structure and build

## Project Structure

```
company-analysis
|-- src
|   |-- main
|   |   |-- java
|   |   |   |-- com
|   |   |       |-- company
|   |   |           |-- Main.java
|   |   |       |-- analyzer
|   |   |           |-- model
|   |   |               |-- CsvColumn.java
|   |   |               |-- Employee.java
|   |   |           |-- service
|   |   |               |-- EmployeeService.java
|   |   |               |-- SalaryAnalyzer.java
|   |   |               |-- ReportingLineAnalyzer.java
|   |   |-- resources
|   |       |-- employees.csv
|-- src
|   |-- test
|       |-- java
|           |-- com
|               |-- company
|                   |-- analyzer
|                       |-- service
|                           |-- EmployeeServiceTest.java
|                           |-- SalaryAnalyzerTest.java
|                           |-- ReportingLineAnalyzerTest.java
|-- pom.xml
|-- README.md
```

## Setup

1. **Place the employee data CSV file in the `resources` directory.**
2. **Build the project using Maven:**
    ```sh
    mvn clean install
    ```
3. **Run the application:**
    ```sh
    java -jar target/company-analysis-1.0.jar --file /path/to/employees.csv
    ```

### Command-Line Options
- `--file`, `-f`: Path to the CSV file.
- `--classpath`, `-c`: Classpath resource name.

## Testing

The project includes comprehensive tests to ensure robustness. The tests cover various scenarios, including typical hierarchy, missing manager IDs, invalid data, circular references, deep hierarchy, multiple CEOs, and large files.

### Running Tests

To run the tests, use the following Maven command:
```sh
mvn test
```

### Test Scenarios

- **Valid CSV with a typical hierarchy:** Tests a normal case with a typical hierarchy.
- **CSV with missing managerId for some employees:** Tests handling of missing manager IDs.
- **CSV with invalid data (non-numeric salary):** Tests handling of invalid data.
- **CSV with circular reference in managerId:** Tests handling of circular references.
- **CSV with a very deep hierarchy:** Tests handling of deep hierarchies.
- **CSV with multiple CEOs (no managerId):** Tests handling of multiple CEOs.
- **CSV with 1000 rows:** Tests handling of large files.
- **CSV with 1001 rows:** Tests handling of files with more lines than 1000.

### Example CSV Files

The `src/test/resources` directory contains example CSV files for testing purposes:
- `valid_employees.csv`
- `missing_manager_id.csv`
- `invalid_data.csv`
- `circular_reference.csv`
- `deep_hierarchy.csv`
- `multiple_ceos.csv`
- `employees_1000.csv`

### Generating a Large CSV File

A Python script is provided to generate a CSV file with 1000 rows that includes various scenarios. Run this script to generate the `employees_1000.csv` file.

```
import csv
import random

def generate_csv(file_path, num_rows):
    with open(file_path, mode='w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(["Id", "firstName", "lastName", "salary", "managerId"])
        
        # Generate typical hierarchy
        for i in range(1, 201):
            id = i
            first_name = f"Name{id}"
            last_name = f"LastName{id}"
            salary = 50000 + (id * 100)  # Incremental salary for variety
            manager_id = id - 1 if id > 1 else ""
            writer.writerow([id, first_name, last_name, salary, manager_id])
        
        # Generate missing managerId for some employees
        for i in range(201, 301):
            id = i
            first_name = f"Name{id}"
            last_name = f"LastName{id}"
            salary = 50000 + (id * 100)
            manager_id = id - 1 if id % 2 == 0 else ""
            writer.writerow([id, first_name, last_name, salary, manager_id])
        
        # Generate invalid data (non-numeric salary)
        for i in range(301, 401):
            id = i
            first_name = f"Name{id}"
            last_name = f"LastName{id}"
            salary = "invalid_salary" if i % 10 == 0 else 50000 + (id * 100)
            manager_id = id - 1 if id > 1 else ""
            writer.writerow([id, first_name, last_name, salary, manager_id])
        
        # Generate circular reference in managerId
        for i in range(401, 501):
            id = i
            first_name = f"Name{id}"
            last_name = f"LastName{id}"
            salary = 50000 + (id * 100)
            manager_id = id - 1 if id > 1 else ""
            if id == 401:
                manager_id = 500  # Circular reference
            writer.writerow([id, first_name, last_name, salary, manager_id])
        
        # Generate very deep hierarchy
        for i in range(501, 701):
            id = i
            first_name = f"Name{id}"
            last_name = f"LastName{id}"
            salary = 50000 + (id * 100)
            manager_id = id - 1 if id > 1 else ""
            writer.writerow([id, first_name, last_name, salary, manager_id])
        
        # Generate multiple CEOs (no managerId)
        for i in range(701, 801):
            id = i
            first_name = f"Name{id}"
            last_name = f"LastName{id}"
            salary = 50000 + (id * 100)
            manager_id = "" if i % 2 == 0 else id - 1
            writer.writerow([id, first_name, last_name, salary, manager_id])
        
        # Generate remaining typical hierarchy
        for i in range(801, 1001):
            id = i
            first_name = f"Name{id}"
            last_name = f"LastName{id}"
            salary = 50000 + (id * 100)
            manager_id = id - 1 if id > 1 else ""
            writer.writerow([id, first_name, last_name, salary, manager_id])

generate_csv("employees_1000.csv", 1000)
```

## Example

An example CSV file format:
```csv
EmployeeID,Name,ManagerID,Salary
1,John Doe,,100000
2,Jane Smith,1,80000
3,Bob Johnson,1,75000
4,Alice Brown,2,60000
5,Charlie Davis,2,55000
```