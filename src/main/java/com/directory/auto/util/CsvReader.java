package com.directory.auto.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermissions;

public class CsvReader {

    public void parseCsv(String source) throws IOException {
        File csvFile = new File(source);
        String line, fileSource, destination;
        String cvsSplitBy = ";";
        String permissions = "rwxrwxrwx";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] addresses = line.split(cvsSplitBy);
                fileSource = addresses[0];
                destination = addresses[1];

                createDirectoriesLinux(fileSource, permissions);
                //createDirectoriesWin(destination);
                moveFiles(fileSource, destination);
            }
        }
    }

    private void createDirectoriesLinux(String destination, String permissions) throws IOException {
        Files.createDirectories(
            Paths.get(destination), 
            PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString(permissions))
        );
    }

    private void createDirectoriesWin(String destination) throws IOException {
        Files.createDirectories(Paths.get(destination));
    }

    private void moveFiles(String source, String destination) throws IOException {
        Path sourcePath = Paths.get(source);
        Path destinationPath = Paths.get(destination);

        Files.move(sourcePath, destinationPath.resolve(sourcePath.getFileName()), StandardCopyOption.REPLACE_EXISTING);

        System.out.println("File \"" + sourcePath.getFileName() + "\" moved to \"" + destination + "\"");
    }
}
