package rip.snicon.utils;

import com.google.gson.*;
import net.kyori.adventure.text.Component;
import rip.snicon.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MOTD {

    private static File dataFolder;
    private static Gson gson;
    private static Map<String, String> motdConfig;

    public MOTD() {
        dataFolder = new File("resources/motd");
        gson = new GsonBuilder().setPrettyPrinting().create();
        motdConfig = new HashMap<>();
        loadMOTD();
    }

    private void loadMOTD() {
        motdConfig.clear();
        if (dataFolder.exists() && dataFolder.isDirectory()) {
            searchFiles(dataFolder);
        } else {
            Main.logger.error("MOTD folder does not exist! Creating...");
            dataFolder.mkdir();
        }
    }

    private static void searchFiles(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursively search in subdirectories
                    searchFiles(file);
                } else if (file.isFile() && file.getName().endsWith(".json")) {
                    // Process JSON files
                    processJsonFile(file);
                }
            }
        } else {
            Main.logger.error("The oblivion config file does not exist!");
        }
    }

    private static void processJsonFile(File file) {
        try {
            String motdConfigString = new String(Files.readAllBytes(file.toPath()));
            String name = file.getName().replace(".json", "");
            motdConfig.put(name, motdConfigString);

            Main.logger.info("Loaded MOTD config: " + name);

        } catch (JsonSyntaxException | JsonIOException e) {
            // Handle Gson-specific errors
            Main.logger.error("Error parsing JSON file: " + file.getName());
        } catch (IOException e) {
            // Handle IO errors
            Main.logger.error("Error loading MOTD file: " + file.getName());
        }
    }

    public static Component createMOTD() {
        Component motd = Component.empty();

        for (Map.Entry<String, String> entry : motdConfig.entrySet()) {
            JsonObject json = gson.fromJson(entry.getValue(), JsonObject.class);

            // Process row1
            JsonArray row1 = json.getAsJsonArray("row1");
            motd = getComponent(motd, row1);

            motd = motd.append(Component.newline());

            // Process row2
            JsonArray row2Options = json.getAsJsonArray("row2");
            if (row2Options != null && row2Options.size() > 0) {
                // Randomly select one option from row2
                Random random = new Random();
                JsonArray selectedRow2 = row2Options.get(random.nextInt(row2Options.size())).getAsJsonArray();

                motd = getComponent(motd, selectedRow2);
            }

            break; // Process only the first MOTD config
        }

        return motd;
    }

    private static Component getComponent(Component motd, JsonArray selectedRow) {
        for (JsonElement element : selectedRow) {
            JsonObject textObj = element.getAsJsonObject();
            String text = textObj.get("text").getAsString();
            String color = textObj.get("color").getAsString();

            Component textComponent = TextUtils.componentFormatMinecraft(text, color);
            motd = motd.append(textComponent);
        }
        return motd;
    }
}
