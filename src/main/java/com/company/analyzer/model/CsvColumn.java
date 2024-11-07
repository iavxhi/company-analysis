package com.company.analyzer.model;

/**
 * Enum representing the columns in the CSV file.
 */
public enum CsvColumn {
    ID(0),
    FIRST_NAME(1),
    LAST_NAME(2),
    SALARY(3),
    MANAGER_ID(4);

    private final int index;

    CsvColumn(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}