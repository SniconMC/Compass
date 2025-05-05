package rip.snicon.compass.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerPluginMessageEvent;
import rip.snicon.compass.inventory.selector.HubSelector;

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
                // Read the offline servers
                String offlineServers = in.readUTF();
                String[] offlineServerList = offlineServers.split(" ");
                // Read the player's current server
                String currentServer = in.readUTF();

                // Directly call createLobbySelector with all necessary data
                HubSelector.updateSelectorWithServerInfo(event.getPlayer(), List.of(serverList), currentServer, playerCountsArray, List.of(offlineServerList));
            }
        });
    }
}