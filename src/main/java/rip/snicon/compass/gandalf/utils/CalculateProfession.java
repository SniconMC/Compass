package rip.snicon.compass.gandalf.utils;

import com.github.sniconmc.container.config.ContainerItem;
import com.github.sniconmc.container.config.ContainerItemData;
import com.github.sniconmc.container.config.ContainerItemDisplay;
import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import com.github.sniconmc.utils.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minestom.server.entity.Player;
import rip.snicon.compass.Main;
import rip.snicon.compass.gandalf.GandalfManager;
import rip.snicon.compass.gandalf.config.GandalfProfession;
import rip.snicon.compass.gandalf.config.GandalfProfile;

import java.util.*;

public class CalculateProfession {

    public static void updateProfession(Player player) {
        GandalfProfile profile = GandalfManager.getProfiles(player);
        Double totalXP = profile.getProfession_total_xp();
        List<GandalfProfession> professions = GandalfManager.getAllProfessions();

        double cumulativeXP = 0; // Keep track of cumulative XP for professions
        String highestUnlockedProfessionId = profile.getProfession(); // Start with the current profession in the profile

        // Determine the highest unlocked profession based on total XP
        for (GandalfProfession profession : professions) {
            cumulativeXP += profession.getXpRequired(); // Assuming getXpRequired() returns the XP needed for the profession

            if (totalXP >= cumulativeXP) {
                highestUnlockedProfessionId = profession.getProfession_id(); // Update highest unlocked profession
            }
        }

        // Check if we need to update the profile and reload the GUI
        if (!profile.getProfession().equals(highestUnlockedProfessionId)) {
            profile.setOldProfession(profile.getProfession());
            profile.setProfession(highestUnlockedProfessionId); // Update the profile with the new highest profession
            updateProfessionGUI(player, profile); // Update the GUI with the new profession

            String oldProfessionStyle = GandalfManager.getProfession(profile.getOldProfession()).getProfession_style_sidebar();
            String professionStyle = GandalfManager.getProfession(profile.getProfession()).getProfession_style_sidebar();

            String oldProfessionStyleIcon = GandalfManager.getProfession(profile.getOldProfession()).getProfession_icon_style();
            String professionStyleIcon = GandalfManager.getProfession(profile.getProfession()).getProfession_icon_style();

            player.sendMessage(TextUtils.convertStringToComponent(
                    "<strikethrough><gray>                                                                                 </gray></strikethrough>\n" +
                            "                                 <bold><gradient:#ffff1c:gold>LEVEL UP!</gradient></bold>\n" +
                            "                        <yellow>You have advanced from</yellow>\n" +
                            "                                   " + oldProfessionStyleIcon + " <gray>" + oldProfessionStyle + "</gray>\n" +
                            "                                        <yellow>to</yellow>\n" +
                            "                                   " + professionStyleIcon + " <gray>" + professionStyle + "</gray>\n" +
                            "<strikethrough><gray>                                                                                 </gray></strikethrough>"
            ));

        }
    }

    public static void updateProfessionGUI(Player player, GandalfProfile profile) {
        Map<String, String> placeholders = new HashMap<>();
        List<GandalfProfession> professions = GandalfManager.getAllProfessions();
        Double totalXP = profile.getProfession_total_xp();

        // StringBuilder to hold all JSON items
        StringBuilder jsonItemsBuilder = new StringBuilder();

        // Create a Gson instance for pretty-printing
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        int i = 0;
        double cumulativeXP = 0; // Keep track of cumulative XP for professions

        List<List<String>> progession = new ArrayList<>();

        for (GandalfProfession profession : professions) {
            i++;
            cumulativeXP += profession.getXpRequired(); // Assuming getXpRequired() returns the XP needed for the profession

            ContainerItem item = new ContainerItem(0, "", 1);

            // Set slot based on index
            if (i > 7) {
                item.setSlot(i + 20);
            } else {
                item.setSlot(i + 18);
            }

            // Determine glass pane color based on XP
            if (totalXP >= cumulativeXP) {
                item.setId("minecraft:green_stained_glass_pane"); // Unlocked
            } else if (i > 1 && totalXP >= cumulativeXP - profession.getXpRequired()) {
                item.setId("minecraft:orange_stained_glass_pane"); // Below this level
            } else {
                item.setId("minecraft:red_stained_glass_pane"); // Locked
            }

            // Display settings for the item
            ContainerItemDisplay display = new ContainerItemDisplay(
                    List.of("» " + profession.getProfession_style() + " «"),
                    List.of(
                            List.of("balle1"),
                            List.of("balle2"),
                            List.of("balle3")
                    ),
                    false, "", true
            );
            ContainerItemData data = new ContainerItemData("", "", false);
            item.setDisplay(display);
            item.setData(data);

            // Convert the item to a JsonElement
            JsonElement jsonElement = JsonParser.parseString(gson.toJson(item));

            // Pretty-print the JSON
            String prettyJson = gson.toJson(jsonElement);

            // Append the pretty-printed JSON to the builder
            if (i > 1) {
                jsonItemsBuilder.append(",\n");
            }
            jsonItemsBuilder.append(prettyJson);

            if (Objects.equals(profile.getProfession(), profession.getProfession_id())) {
                progession.add(List.of("\"<green><strikethrough>  </strikethrough>> </green>" + profession.getProfession_icon_style() + " " + profession.getProfession_style() + "\""));
            } else {
                progession.add(List.of("\"     " + profession.getProfession_icon_style() + " " + profession.getProfession_style() + "\""));
            }

        }
        
        // Add the JSON objects to the placeholders
        placeholders.put("profession_gui_items", jsonItemsBuilder.toString());
        placeholders.put("profession_progression_order", progession.toString().substring(1, progession.toString().length() - 1));
        PlaceholderManager.addPlaceholdersToPlayer(player, placeholders);

        ProfileUtils.update(player, profile);
    }

}
