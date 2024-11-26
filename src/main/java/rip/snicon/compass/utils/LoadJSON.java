package rip.snicon.compass.utils;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import rip.snicon.compass.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LoadJSON {


    public Map<String, String> load(File parentFolder) {

        if (!parentFolder.exists() || !parentFolder.isDirectory()) {
            Main.logger.warn("Parent folder does not exist or is not a directory: {}", parentFolder.getAbsolutePath());
            boolean hasCreated = parentFolder.mkdirs();

            if (hasCreated) {
                Main.logger.info("Created folder '{}'!", parentFolder.getName());
            } else {
                Main.logger.warn("Failed to create folder '{}'!", parentFolder.getName());
            }

        }

        // A set to store the names of all files processed during this load
        Set<File> foundJsonFiles = searchFiles(parentFolder);

        return processJsonFiles(foundJsonFiles, parentFolder);
    }


    private Set<File> searchFiles(File folder){

        File[] files = folder.listFiles();

        if (files == null) {
            Main.logger.warn("The {} folder does not contain any files", folder.getName());
            return new HashSet<>();
        }

        Set<File> processedFiles = new HashSet<>();

        for (File file : files) {
            if (file.isDirectory()) {
                processedFiles.addAll(searchFiles(file));
            }
            if (file.isFile() && file.getName().endsWith(".json")) {
                processedFiles.add(file);
            }

        }

        return processedFiles;

    }

    private static Map<String, String> processJsonFiles(Set<File> files, File parentFolder) {

        if (files == null || files.isEmpty()) {
            Main.logger.warn("The {} folder does not contain any json files", parentFolder.getName());
            return new HashMap<>();
        }

        Map<String, String> processedFiles = new HashMap<>();

        for (File file : files) {
            try {
                String config = new String(Files.readAllBytes(file.toPath()));
                String fileName = file.getName().replace(".json", "");

                processedFiles.put(fileName, config);

            } catch (JsonSyntaxException | JsonIOException e) {
                // Handle Gson-specific errors
                Main.logger.error("Error parsing JSON file: {}", file.getName());
            } catch (IOException e) {
                // Handle IO errors
                Main.logger.error("Error loading file in folder '{}' file: {}", parentFolder.getName(), file.getName());
            }
        }

        return processedFiles;
    }

    public String loadSpecificJsonFile(File parentFolder, String fileName) {

        // Recursively search for JSON files
        Set<File> foundJsonFiles = searchFiles(parentFolder);

        // Iterate through the found JSON files and search for the file that matches the fileName
        for (File file : foundJsonFiles) {
            if (file.getName().equalsIgnoreCase(fileName + ".json")) {
                try {
                    // Read the content of the file and return it as a string
                    return new String(Files.readAllBytes(file.toPath()));
                } catch (IOException e) {
                    Main.logger.error("Error loading specific file '{}' in folder '{}'", fileName, parentFolder.getName());
                    return "";
                }
            }
        }

        // If the file is not found, return an empty string
        Main.logger.warn("The file {}.json could not be found in folder: {}", fileName, parentFolder.getAbsolutePath());
        return "";
    }

}
