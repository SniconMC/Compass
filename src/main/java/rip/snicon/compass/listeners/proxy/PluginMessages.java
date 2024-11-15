package rip.snicon.compass.listeners.proxy;

import com.github.sniconmc.gandalf.GandalfManager;
import com.github.sniconmc.gandalf.database.DatabasePlayer;
import com.github.sniconmc.gandalf.utils.CalculateProfession;
import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerPluginMessageEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import rip.snicon.compass.Main;
import rip.snicon.compass.interactions.containers.LobbySelector;
import rip.snicon.compass.listeners.placeholders.enums.HubExplorerEnum;

import java.util.List;

public class PluginMessages {

    private final EventNode<Event> placeholderNode;

    public PluginMessages(EventNode<Event> parentNode) {
        this.placeholderNode = EventNode.all("pluginMessages");
        onPluginMessage();
        parentNode.addChild(placeholderNode);
    }

    public void onPluginMessage() {
        placeholderNode.addListener(PlayerPluginMessageEvent.class, event -> {
            Main.logger.info("Hello");
            if (!event.getIdentifier().equals("bungeecord:main")) {
                return;
            }
            ByteArrayDataInput in = ByteStreams.newDataInput(event.getMessage());
            String subchannel = in.readUTF();

            if (subchannel.equals("ServerSelectorInfo")) {
                // Read the list of server names
                String servers = in.readUTF();
                String[] serverList = servers.split(" ");

                // Read player counts for each server
                String playerCounts = in.readUTF();
                String[] playerCountList = playerCounts.split(" ");
                int[] playerCountsArray = new int[playerCountList.length];
                for (int i = 0; i < playerCountList.length; i++) {
                    playerCountsArray[i] = Integer.parseInt(playerCountList[i]);
                }

                // Read the player's current server
                String currentServer = in.readUTF();

                // Log the information for debugging
                Main.logger.info("Servers: " + servers);
                Main.logger.info("Player Counts: " + playerCounts);
                Main.logger.info("Current Server: " + currentServer);

                // Directly call createLobbySelector with all necessary data
                LobbySelector.createLobbySelector(event.getPlayer(), List.of(serverList), currentServer, playerCountsArray);
            }
        });
    }
}

