package rip.snicon.utils.motd;

import com.google.gson.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import rip.snicon.Main;
import rip.snicon.utils.TextUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Utility class for managing and generating the Message of the Day (MOTD) configurations.
 *
 * <p>This class provides methods for loading MOTD configurations from JSON files,
 * processing them, and generating a MOTD {@link Component} to be displayed in the server.</p>
 *
 * <p>MOTD configurations are stored in JSON files within a specified directory. The class
 * supports dynamic generation of MOTD messages by selecting random entries from
 * configuration files.</p>
 *
 * @see net.kyori.adventure.text.Component
 * @see TextUtils
 *
 * @author znopp
 */
public class MOTD {

    private static File dataFolder;
    private static Gson gson;
    private static Map<String, String> motdConfig;

    /**
     * Constructs a new MOTD instance and initializes the configuration.
     *
     * <p>This constructor initializes the data folder path, sets up the Gson instance
     * for JSON parsing, and loads the MOTD configurations from the files in the
     * specified directory.</p>
     *
     * @author znopp
     */
    public MOTD() {
        dataFolder = new File("resources/motd");
        gson = new GsonBuilder().setPrettyPrinting().create();
        motdConfig = new HashMap<>();
        loadMOTD();
    }

    /**
     * Loads the MOTD configurations from the JSON files located in the data folder.
     *
     * <p>This method clears the existing configurations and checks if the data folder exists
     * and is a directory. It then searches for all JSON files in the folder and loads their
     * contents into the configuration map.</p>
     *
     * <p>If the folder does not exist, it creates the folder and logs an error message.</p>
     *
     * @author znopp
     */
    private void loadMOTD() {
        motdConfig.clear();
        if (dataFolder.exists() && dataFolder.isDirectory()) {
            searchFiles(dataFolder);
        } else {
            Main.logger.error("MOTD folder does not exist! Creating...");
            dataFolder.mkdir();
        }
    }

    /**
     * Recursively searches for JSON files in the specified folder and its subdirectories.
     *
     * <p>This method traverses the given folder and processes all JSON files found within it,
     * adding their content to the MOTD configuration map. It also searches subdirectories
     * recursively for additional JSON files.</p>
     *
     * @param folder the folder to search for JSON configuration files
     *
     * @author znopp
     */
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

    /**
     * Processes a JSON file and adds its contents to the MOTD configuration map.
     *
     * <p>This method reads the contents of the specified JSON file, parses it, and stores
     * it in the MOTD configuration map with the filename (without the .json extension) as the key.</p>
     *
     * <p>Logs appropriate messages upon successful loading or error during parsing.</p>
     *
     * @param file the JSON file to process and load into the configuration map
     *
     * @author znopp
     */
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


    /**
     * Generates the MOTD {@link Component} based on the loaded configurations.
     *
     * <p>This method constructs a message of the day (MOTD) {@link Component} using the first
     * loaded MOTD configuration. It processes the first and second rows of the configuration,
     * appending them to the MOTD {@link Component}. If multiple options are available for the
     * second row, one is selected randomly.</p>
     *
     * @return the generated MOTD {@link Component} to be displayed
     *
     * @author znopp
     */
    public static Component createMOTD() {
        Component motd = Component.empty();

        for (Map.Entry<String, String> entry : motdConfig.entrySet()) {

            MOTDConfig config = gson.fromJson(entry.getValue(), MOTDConfig.class);

            // Process row1 & row2Options
            List<String> row1 = config.getRow1().getFirst();
            List<List<String>> row2Options = config.getRow2();

            motd = motd.append(TextUtils.convertStringToComponent(row1)); // Pass row1 directly to getComponent

            motd = motd.append(Component.newline());

            if (row2Options != null && !row2Options.isEmpty()) {
                // Randomly select one option from row2
                Random random = new Random();
                List<String> selectedRow2 = row2Options.get(random.nextInt(row2Options.size()));

                motd = motd.append(TextUtils.convertStringToComponent(selectedRow2)); // Pass selectedRow2 directly to getComponent
            }

            break; // Process only the first MOTD config
        }

        return motd;
    }
}
