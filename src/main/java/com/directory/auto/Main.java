package com.directory.auto;

import java.io.IOException;

import com.directory.auto.util.CsvReader;
import com.directory.auto.util.UnzipFile;

public class Main {
    public static void main(String[] args) throws IOException {
        String source = args[0];
        String csvFile = args[1];

        UnzipFile uzf = new UnzipFile(source);
        uzf.zipFunction();

        CsvReader csv = new CsvReader();
        csv.parseCsv(csvFile);

        uzf.deleteZip();
    }
}