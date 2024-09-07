package rip.snicon.compass.gandalf.utils;

import com.github.sniconmc.utils.placeholder.PlaceholderReplacer;
import com.github.sniconmc.utils.text.TextUtils;
import net.minestom.server.entity.*;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import rip.snicon.compass.gandalf.GandalfManager;
import rip.snicon.compass.gandalf.config.GandalfProfile;
import rip.snicon.compass.gandalf.config.GandalfRank;

import java.util.*;

public class TabUtils {

    public static void setExistingPlayer(Player viewer, Player playerToSet) {

        String displayName = getPlayerDisplayName(viewer, playerToSet);

        PlayerSkin skin = playerToSet.getSkin();
        List<PlayerInfoUpdatePacket.Property> properties = skin != null ? List.of(new PlayerInfoUpdatePacket.Property("textures", skin.textures(), skin.signature())) : List.of();

        PlayerInfoUpdatePacket.Entry entry = new PlayerInfoUpdatePacket.Entry(playerToSet.getUuid(), playerToSet.getUsername(), properties, true, 0, GameMode.SURVIVAL, TextUtils.convertStringToComponent(displayName), null);
        // Create the packet with the action to update the display name
        PlayerInfoUpdatePacket packet = new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, entry);

        // Send the packet to update the player's display name
        viewer.sendPacket(packet);

    }

    public static String getPlayerDisplayName(Player viewer, Player playerToSet) {

        GandalfProfile profile = GandalfManager.getProfiles(viewer);
        if (profile == null) {
            return playerToSet.getUsername();
        }

        String id = profile.getRank_id();

        GandalfRank config = GandalfManager.getRank(id);
        if (config == null) {
            return playerToSet.getUsername();
        }
        String format;

        // Fetch the profession format and convert to lowercase
        String professionFormat = profile.getSettings().getProfession_format().toLowerCase();

        // Switch based on profession format
        format = switch (professionFormat) {
            case "text" ->
                // Use the standard rank format if "text"
                    config.getRankFormat();
            case "icon" ->
                // Use the simplified rank format if "icon"
                    config.getRankFormatSimple();
            default ->
                // Handle any unrecognized format; fall back to a default format if needed
                    "fuck all";  // Assuming you have a default format method
        };

        // Convert the formatted string to a Component and return it
        return PlaceholderReplacer.replacePlaceholders(playerToSet, format);
    }
}

