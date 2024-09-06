package rip.snicon.gandalf.utils;

import com.github.sniconmc.oblivion.OblivionMain;
import com.github.sniconmc.utils.text.TextUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import org.jetbrains.annotations.NotNull;
import rip.snicon.Main;

import java.util.*;

public class TabUtils {

    private static final Map<Player, Map<Player, UUID>> playerUUIDMap = new HashMap<>();

    public static void setTabPlayer(Player player, Player playerToAdd, PlayerSkin playerSkin, String prefix, String suffix) {

        UUID uuid = UUID.randomUUID();

        List<PlayerInfoUpdatePacket.Property> properties;
        properties = List.of(new PlayerInfoUpdatePacket.Property("textures", playerSkin.textures(), playerSkin.signature()));

        PlayerInfoUpdatePacket.Entry fakePlayerTab = new PlayerInfoUpdatePacket.Entry(uuid, playerToAdd.getUsername(), properties, true, 0, GameMode.SURVIVAL, Component.empty(), null);

        player.sendPacket(new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.ADD_PLAYER, fakePlayerTab));

        player.sendPacket(new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.UPDATE_LISTED, fakePlayerTab));

        Map<Player, UUID> uuidMap = playerUUIDMap.getOrDefault(player, new HashMap<>());
        uuidMap.put(playerToAdd, uuid);
        playerUUIDMap.put(player, uuidMap);
    }

    public static void removePlayerTab(Player viewer, Player playerToBeRemoved) {
        Map<Player, UUID> fakePlayerUUIDMap = playerUUIDMap.get(viewer); // Get the fake player's UUID
        List<PlayerInfoUpdatePacket.Property> properties = new ArrayList<>();
        if (fakePlayerUUIDMap == null) {
            // Always remove the real player from the tab
            UUID realPlayerUUID = playerToBeRemoved.getUuid();
            viewer.sendPacket(new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.UPDATE_LISTED, new PlayerInfoUpdatePacket.Entry(realPlayerUUID, playerToBeRemoved.getUsername(), properties, false, 0, GameMode.SURVIVAL, Component.empty(), null)));
            return;
        }
        UUID fakePlayerUUID  = fakePlayerUUIDMap.get(playerToBeRemoved);

        if (fakePlayerUUID != null && viewer != playerToBeRemoved) {
            // Remove the fake player from the tab of the viewer
            viewer.sendPacket(new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.UPDATE_LISTED, new PlayerInfoUpdatePacket.Entry(fakePlayerUUID, playerToBeRemoved.getUsername(), properties, false, 0, GameMode.SURVIVAL, Component.empty(), null)));
            viewer.sendPacket(new PlayerInfoRemovePacket(fakePlayerUUID));
        }

        // Always remove the real player from the tab
        UUID realPlayerUUID = playerToBeRemoved.getUuid();
        viewer.sendPacket(new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.UPDATE_LISTED, new PlayerInfoUpdatePacket.Entry(realPlayerUUID, playerToBeRemoved.getUsername(), properties, false, 0, GameMode.SURVIVAL, Component.empty(), null)));

    }

}

