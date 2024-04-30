package com.directory.auto;

import java.io.IOException;

import com.directory.auto.util.CsvReader;
import com.directory.auto.util.UnzipFile;

public class Main {
    public static void main(String[] args) throws IOException {
        String source = args[0];
        String csvFile = args[1];
        String permissions = "rwxrwxrwx";

        UnzipFile uzf = new UnzipFile(source);
        uzf.zipFunction();

        CsvReader csv = new CsvReader(csvFile, permissions);
        csv.parseCsv();

        uzf.deleteZip();
    }
}