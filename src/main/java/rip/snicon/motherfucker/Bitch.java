package rip.snicon.motherfucker;

import com.github.sniconmc.container.utils.ReloadContainer;
import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import com.github.sniconmc.utils.placeholder.PlaceholderReplacer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import rip.snicon.gandalf.GandalfManager;
import rip.snicon.gandalf.config.GandalfRank;
import rip.snicon.gandalf.config.GandalfProfile;
import rip.snicon.gandalf.utils.LoadGandalf;
import rip.snicon.gandalf.utils.TabUtils;
import rip.snicon.gandalf.utils.TeamUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static rip.snicon.gandalf.utils.SaveProfile.saveProfileToFile;

public class Bitch {


    public static void toggleProfession(Player player, Event event) {

        // TODO

        Map<String, String> profileDataJSONData = new LoadGandalf().load(GandalfManager.getDataProfileFolder());

        String playerProfile = profileDataJSONData.get(player.getUuid().toString());

        Map<String, String> dataRankFileJSONData = new LoadGandalf().load(GandalfManager.getDataFolderRanks());

        GandalfProfile profile = GandalfManager.getGson().fromJson(playerProfile, GandalfProfile.class);

        GandalfRank config = GandalfManager.getGson().fromJson(dataRankFileJSONData.get(profile.getRank_id()), GandalfRank.class);

        // Cast the event to InventoryPreClickEvent if needed
        if (!(event instanceof InventoryPreClickEvent clickEvent)) {
            return;
        }

        ItemStack clickedItem = clickEvent.getClickedItem();

        if (clickedItem.material() == Material.OAK_SIGN) {

            String oldProfessionFormatState = profile.getSettings().getProfession_format();

            switch (oldProfessionFormatState.toLowerCase()) {
                case "icon" -> {
                    player.sendMessage("nub");
                    PlaceholderManager.setPlaceholderToPlayer(player, "profession_format_state", "Text");

                    profile.getSettings().setProfession_format("text");

                    // Extract common logic for config.getRankFormat()
                    List<String> prefixComplex = List.of(PlaceholderReplacer.replacePlaceholders(player, TeamUtils.getPrefixBeforeUsernamePlaceholder(config.getRankFormat().getFirst())));
                    List<String> suffixComplex = List.of(PlaceholderReplacer.replacePlaceholders(player, TeamUtils.getSuffixAfterUsernamePlaceholder(config.getRankFormat().getFirst())));

                    // Iterate through UUIDs and change appearance using the complex rank format
                    for (UUID uuid : TabUtils.getPlayerUUIDMap().get(player).values()) {
                        TeamUtils.changeApperence(player, uuid, prefixComplex, suffixComplex);
                    }

                }
                case "text" -> {
                    player.sendMessage("obama");
                    PlaceholderManager.setPlaceholderToPlayer(player, "profession_format_state", "Icon");

                    profile.getSettings().setProfession_format("icon");
                    List<String> prefixSimple = List.of(PlaceholderReplacer.replacePlaceholders(player, TeamUtils.getPrefixBeforeUsernamePlaceholder(config.getRankFormatSimple().getFirst())));
                    List<String> suffixSimple = List.of(PlaceholderReplacer.replacePlaceholders(player, TeamUtils.getSuffixAfterUsernamePlaceholder(config.getRankFormatSimple().getFirst())));

                    // Iterate through UUIDs and change appearance using the simple rank format
                    for (UUID uuid : TabUtils.getPlayerUUIDMap().get(player).values()) {
                        TeamUtils.changeApperence(player, uuid, prefixSimple, suffixSimple);
                    }
                }
            }
            saveProfileToFile(player.getUuid().toString(), profile, GandalfManager.getDataProfileFolder(), GandalfManager.getGson());
        }

        ReloadContainer.reloadCurrentContainers(player);
    }

    public static void togglePlayerVisibility(Player player, Event event) {
        if (!(event instanceof InventoryPreClickEvent clickEvent)) {
            return;
        }

        ItemStack clickedItem = clickEvent.getClickedItem();

        if (clickedItem.material() == Material.LIME_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item", "gray_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state", "<red>Hide Players</red>");

            player.updateViewerRule(other -> !(other instanceof Player));

        }

        if (clickedItem.material() == Material.GRAY_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item", "lime_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state", "<green>Show Players</green>");

            player.updateViewerRule(null);
        }

        ReloadContainer.reloadCurrentContainers(player);
    }

    public static void toggleGeriVisibility(Player player, Event event) {
        if (!(event instanceof InventoryPreClickEvent clickEvent)) {
            return;
        }


        ItemStack clickedItem = clickEvent.getClickedItem();

        if (clickedItem.material() == Material.LIME_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item_geri", "gray_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state_geri", "<red>Hide Geri</red>");


        }

        if (clickedItem.material() == Material.GRAY_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item_geri", "lime_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state_geri", "<green>Show Geri</green>");

        }

        ReloadContainer.reloadCurrentContainers(player);
    }

}
