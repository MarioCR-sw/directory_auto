package com.directory.auto.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class UnzipFile {
    private String source;
    private String destination;
    private String folderName;

    //Constructor for extracting when there's only one zip file in the folder
    public UnzipFile() {
        File[] files = new File(".").listFiles((dir, name) -> name.endsWith(".zip"));
        
        if(files != null && files.length != 0) {
            this.source = files[0].getName();
        } else {
            System.out.println("Exception in no-arguments constructor of class UnzipFile");
            throw new RuntimeException("There's no zip file in this folder.");
        }

        destination = ".";
    }

    //Constructor for extracting to the same folder
    public UnzipFile(String source) {
        this.source = source;
        this.destination = ".";
    }

    public UnzipFile(String source, String destination) {
        this.source = source;
        this.destination = destination;
    }

    public void zipFunction() {
        try (ZipFile zipFile = new ZipFile(source)) {
            List<FileHeader> fileHeaders = zipFile.getFileHeaders();
            FileHeader fileHeader;
            int size = fileHeaders.size();
            int i = 0;

            //Gets the folder name inside the archive for deletion at the end.
            //Should get the folder name from CsvReader after reading the path to the files,
            //but that might be a little more complex than initially thought.
            do {
                fileHeader = fileHeaders.get(i);
                i++;
            } while (!checkForDirectory(fileHeader) && i < size);
            folderName = getFolderNameFromFileHeader(fileHeader);
            
            zipFile.extractAll(destination);
            zipFile.close();

            System.out.println("Zip file \"" + source + "\" successfully extracted.");
            System.out.println();
        } catch (ZipException e) {
            System.out.println("Exception in method zipFunction() of class UnzipFile");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Exception in method zipFunction() of class UnzipFile");
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Exception in method zipFunction() of class UnzipFile");
            System.out.println("Zip file \"" + source + "\" has no contents or doesn't exist.");
            e.printStackTrace();
        }
    }

    private Boolean checkForDirectory(FileHeader fileHeader) {
        return fileHeader.isDirectory() || 
            fileHeader.getFileName().contains("\\") || 
            fileHeader.getFileName().contains("/");
    }

    private String getFolderNameFromFileHeader(FileHeader fileHeader) {
        if (fileHeader.isDirectory()) {
            return folderName = fileHeader.getFileName();
        } else if(fileHeader.getFileName().contains("/")) {
            return folderName = fileHeader.getFileName().split("/")[0];
        } else if(fileHeader.getFileName().contains("\\")) {
            return folderName = fileHeader.getFileName().split("\\\\")[0];
        } else {
            return "";
        }
    }

    public void deleteZip() {
        File zip = new File(source);
        zip.delete();
        System.out.println();
        System.out.println("Zip file \"" + source + "\" successfully deleted.");

        if(folderName != null && !folderName.isEmpty()) {
            File folder = new File(folderName);
            folder.delete();
            System.out.println("Extracted folder \"" + folderName + "\" successfully deleted.");
        }
    }
}