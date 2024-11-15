package rip.snicon.compass.interactions.containers;

import com.github.sniconmc.container.ContainerMain;
import com.github.sniconmc.container.ContainerManager;
import com.github.sniconmc.container.config.ContainerItem;
import com.github.sniconmc.container.config.ContainerItemDisplay;
import com.github.sniconmc.container.creators.ContainerCreator;
import com.github.sniconmc.utils.item.ItemStackBuilder;
import com.github.sniconmc.utils.item.ItemStackDestroyer;
import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import com.github.sniconmc.utils.text.TextUtils;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.item.Material;
import rip.snicon.compass.Main;
import rip.snicon.compass.listeners.proxy.PluginMessages;

import java.util.ArrayList;
import java.util.List;

public class LobbySelector {

    public static void requestServerInfo(Player player) {
        Main.logger.info("Click");
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ServerSelectorInfo");
        player.sendPluginMessage("bungeecord:main", out.toByteArray());
    }

    public static void createLobbySelector(Player player, List<String> servers, String currentServer, int[] playerCounts) {
        if (servers == null || servers.isEmpty()) {
            return;
        }

        Gson gson = new Gson();
        List<String> jsonItems = new ArrayList<>();
        int server_count = servers.size();
        Main.logger.error(String.valueOf(server_count));

        // Calculate the required number of rows
        int rows = calculateRows(server_count);
        int itemsPerRow = 7;

        // Populate each server's information in the GUI
        for (int i = 0; i < servers.size(); i++) {
            String serverName = servers.get(i);
            int playerCount = playerCounts[i];
            int slot = getSlot(i);

            // Determine if this is the current server
            boolean isCurrentServer = serverName.equals(currentServer);

            // Set the display name and color
            String displayName = isCurrentServer
                    ? "<green>Server: " + serverName  // Green color code for current server
                    : "<yellow>Server: " + serverName;

            // Set lore based on whether the player is on the server
            List<List<String>> lore = isCurrentServer
                    ? List.of(
                    List.of("Players: " + playerCount),
                    List.of(""),
                    List.of("<green>You are connected to this server")
            )
                    : List.of(
                    List.of("Players: " + playerCount),
                    List.of(""),
                    List.of("<yellow>Click to connect to server")
            );

            // Set item material based on current server
            String itemMaterial = isCurrentServer ? "minecraft:emerald" : "minecraft:quartz";
            Main.logger.error(displayName);

            // Create the container item
            ContainerItem item = new ContainerItem(slot, itemMaterial, 1);
            item.setDisplay(new ContainerItemDisplay(
                    List.of(displayName),
                    lore,
                    false, "", true
            ));

            String jsonString = gson.toJson(item, ContainerItem.class);
            jsonItems.add(jsonString);
        }

        // Calculate the number of "air" items needed to fill the last row
        int totalSlotsUsed = server_count % itemsPerRow;
        int airItemsNeeded = (totalSlotsUsed == 0) ? 0 : itemsPerRow - totalSlotsUsed;

        // Add "air" items to fill the remaining slots in the last row
        for (int i = 0; i < airItemsNeeded; i++) {
            ContainerItem airItem = new ContainerItem(getSlot(server_count + i), "minecraft:air", 1);
            airItem.setDisplay(new ContainerItemDisplay(
                    List.of(""),
                    List.of(),
                    false, "", true
            ));
            String airJsonString = gson.toJson(airItem, ContainerItem.class);
            jsonItems.add(airJsonString);
        }

        // Log the JSON data for debugging
        Main.logger.info(String.valueOf(rows));

        // Set placeholders and open the container
        PlaceholderManager.setPlaceholderToPlayer(player, "lobbies", jsonItems.toString());
        PlaceholderManager.setPlaceholderToPlayer(player, "lobby_selector_rows", String.valueOf(rows));
        ContainerCreator.openContainer(player, "lobby_selector");
    }


    private static int getSlot(int index) {
        int itemsPerRow = 7;
        int row = index / itemsPerRow;  // Row number based on index
        int col = index % itemsPerRow;  // Column within the row

        // Calculate slot position
        // Row offset starts at 9 (first row filled with "X" background) + (row * 9) to move down
        return 9 + (row * 9) + (col + 1);
    }

    public static int calculateRows(int numServers) {
        // Calculate minimum rows needed based on server count
        // Using ceiling division to ensure we have enough rows

        // For 15 servers:
        // We need at least Math.ceil(15/7) = 3 rows to fit all servers
        // But since we don't want more than 5 servers in incomplete rows,
        // we need additional rows

        // Check if we need extra rows due to the 5-server limit per incomplete row
        int remainingServers = numServers;
        int rows = 0;
        while (remainingServers > 0) {
            if (remainingServers >= 7) {
                remainingServers -= 7;
                rows += 1;
            } else if (remainingServers > 5) {
                // If we have 6 servers, we need 2 rows (3 servers each)
                rows += 2;
                remainingServers = 0;
            } else {
                rows += 1;
                remainingServers = 0;
            }
            if (remainingServers > 0) {
                rows += 1;
            }
        }
        Main.logger.error(String.valueOf(rows));
        // Enforce minimum of 3 rows and maximum of 6 rows
        return Math.min(6, Math.max(3, rows));
    }
}
