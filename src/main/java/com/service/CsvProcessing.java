package com.service;

public interface CsvProcessing {

    <T> void readCsv(String file, Class<T> mapper);

    void writeCsv(String file);
}
