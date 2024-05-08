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

import org.apache.commons.lang3.SystemUtils;

public class CsvReader {
    private String source;
    private String permissions;

    public CsvReader(String source, String permissions) {
        this.source = source;
        this.permissions = permissions;
    }

    public void parseCsv() throws IOException {
        File csvFile = new File(source);
        String csvSplitBy = ";";

        if(SystemUtils.IS_OS_LINUX) {
            parseCsvLinux(csvFile, csvSplitBy);
        } else if(SystemUtils.IS_OS_WINDOWS) {
            parseCsvWin(csvFile, csvSplitBy);
        }
    }

    private void parseCsvLinux(File csvFile, String csvSplitBy) throws IOException {
        String line, fileSource, destination;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] addresses = line.split(csvSplitBy);
                fileSource = addresses[0];
                destination = addresses[1];
    
                createDirectoriesLinux(destination, permissions);
                if(!fileSource.isEmpty() && fileSource != null) {
                    moveFiles(fileSource, destination);
                }
            }
        }
    }

    private void parseCsvWin(File csvFile, String csvSplitBy) throws IOException {
        String line, fileSource, destination;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] addresses = line.split(csvSplitBy);
                fileSource = addresses[0];
                destination = addresses[1];

                createDirectoriesWin(destination);
                System.out.println();
                if(!fileSource.isEmpty() && fileSource != null) {
                    moveFiles(fileSource, destination);
                }
            }
        }
    }

    private void createDirectoriesLinux(String destination, String permissions) throws IOException {
        Path destPath = Paths.get(destination);
        
        if (!Files.exists(destPath)) {
            Files.createDirectories(
                destPath, 
                PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString(permissions))
            );

            System.out.print("Directory \"" + destination + "\" created,");
        } else {
            System.out.print("Directory \"" + destination + "\" already exists,");
        }

        Files.setPosixFilePermissions(
                destPath,
                PosixFilePermissions.fromString(permissions));
                
        System.out.println(" given \"" + permissions + "\" permissions to it.");
    }

    private void createDirectoriesWin(String destination) throws IOException {
        Files.createDirectories(Paths.get(destination));
        System.out.println("Directory \"" + destination + "\" created.");
    }

    private void moveFiles(String source, String destination) throws IOException {
        Path sourcePath = Paths.get(source);
        Path destinationPath = Paths.get(destination);

        Files.move(sourcePath, destinationPath.resolve(sourcePath.getFileName()), StandardCopyOption.REPLACE_EXISTING);

        System.out.println("File \"" + sourcePath.getFileName() + "\" moved to \"" + destination + "\"");
        System.out.println();
    }
}
