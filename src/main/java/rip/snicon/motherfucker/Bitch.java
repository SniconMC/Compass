package rip.snicon.motherfucker;

import com.github.sniconmc.container.utils.ReloadContainer;
import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import rip.snicon.Main;
import rip.snicon.gandalf.config.GandalfProfession;
import rip.snicon.gandalf.config.GandalfProfile;
import rip.snicon.gandalf.utils.LoadGandalf;
import rip.snicon.gandalf.utils.TabUtils;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

import static rip.snicon.gandalf.utils.SaveProfile.saveProfileToFile;

public class Bitch {

    private static final Gson gson = new Gson().newBuilder().setPrettyPrinting().create();

    private static final File dataFolderProfession = new File("resources/gandalf/professions");
    private static final File dataProfileFolder = new File("resources/profiles");

    public static void toggleProfession(Player player, Event event) {

        // TODO

        Map<String, String> profileDataJSONData = new LoadGandalf().load(dataProfileFolder);

        String playerProfile = profileDataJSONData.get(player.getUuid().toString());

        Map<String, String> dataProfessionFileJSONData = new LoadGandalf().load(dataFolderProfession);

        GandalfProfile profile = gson.fromJson(playerProfile, GandalfProfile.class);

        GandalfProfession profession = gson.fromJson(dataProfessionFileJSONData.get(profile.getProfession()), GandalfProfession.class);

        // Cast the event to InventoryPreClickEvent if needed
        if (!(event instanceof InventoryPreClickEvent clickEvent)) {
            return;
        }

        ItemStack clickedItem = clickEvent.getClickedItem();

        if (clickedItem.material() == Material.OAK_SIGN) {

            String oldProfessionFormatState = PlaceholderManager.getPlaceholderForPlayer(player, "profession_format_state");

            switch (oldProfessionFormatState) {
                case "Icon" -> {
                    PlaceholderManager.setPlaceholderToPlayer(player, "profession_format_state", "Text");
                    PlaceholderManager.setPlaceholderToPlayer(player, "player_profession", profession.getProfession_style().getFirst());

                    profile.getSettings().setProfession_format("Text");

/*                    for (Player onlinePlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                        if (onlinePlayer != player) {
                            TabUtils.removePlayerTab(player, onlinePlayer);
                            TabUtils.setTabPlayer(player, onlinePlayer, Objects.requireNonNull(onlinePlayer.getSkin()), "", "");
                        }
                    }*/

                }
                case "Text" -> {
                    PlaceholderManager.setPlaceholderToPlayer(player, "profession_format_state", "Icon");
                    PlaceholderManager.setPlaceholderToPlayer(player, "player_profession", profession.getProfession_icon_style().getFirst());

                    profile.getSettings().setProfession_format("Icon");
/*                    for (Player onlinePlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                        if (onlinePlayer != player) {
                            TabUtils.removePlayerTab(player, onlinePlayer);
                            TabUtils.setTabPlayer(player, onlinePlayer, Objects.requireNonNull(onlinePlayer.getSkin()), "", "");
                        }
                    }*/
                }
            }
            saveProfileToFile(player.getUuid().toString(), profile, dataProfileFolder, gson);
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

        UUID geri = UUID.fromString("c600eeb7c7da4bddbff1d26e71001d39");
        ItemStack clickedItem = clickEvent.getClickedItem();

        if (clickedItem.material() == Material.LIME_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item_geri", "gray_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state_geri", "<red>Hide Geri</red>");

            if (MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(geri) != null) {
                player.removeViewer(MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(geri));
            }
        }

        if (clickedItem.material() == Material.GRAY_DYE) {
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_item_geri", "lime_dye");
            PlaceholderManager.setPlaceholderToPlayer(player, "player_visibility_state_geri", "<green>Show Geri</green>");

            if (MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(geri) != null) {
                player.addViewer(MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(geri));
            }

        }

        ReloadContainer.reloadCurrentContainers(player);
    }

}
