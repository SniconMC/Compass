package rip.snicon.gandalf.utils;

import com.github.sniconmc.oblivion.OblivionMain;
import com.github.sniconmc.utils.text.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.*;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import org.jetbrains.annotations.NotNull;
import rip.snicon.Main;

import java.util.*;

public class TabUtils {

    public static void setExistingPlayer(Player viewer, Player playerToSet) {

        // Create the packet with the action to update the display name
        PlayerInfoUpdatePacket packet = new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, playerToSet);

        String displayName = getPlayerDisplayName(playerToSet);

        playerToSet.setDisplayName(TextUtils.convertStringToComponent(displayName));

        // Send the packet to update the player's display name
        viewer.sendPacket(packet);


    public static String getPlayerDisplayName(Player player){
       Map<String, String> dataRanksFileJSONData
    }
}

