package rip.snicon.compass.interactions.containers;

import com.github.sniconmc.container.ContainerMain;
import com.github.sniconmc.container.ContainerManager;
import com.github.sniconmc.container.config.ContainerItem;
import com.github.sniconmc.container.config.ContainerItemData;
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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LobbySelector {

    public static void requestServerInfo(Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ServerSelectorInfo");
        player.sendPluginMessage("bungeecord:main", out.toByteArray());
    }

    public static void createLobbySelector(Player player, List<String> servers, String currentServer, int[] playerCounts, List<String> offlineServers) {
        if (servers == null || servers.isEmpty()) {
            return;
        }

        Gson gson = new Gson();
        List<String> jsonItems = new ArrayList<>();

        // Create a list of ServerInfo objects to hold server details
        List<ServerInfo> serverInfoList = new ArrayList<>();
        for (int i = 0; i < servers.size(); i++) {
            String serverName = servers.get(i);
            int playerCount = playerCounts[i];
            boolean isOffline = offlineServers.contains(serverName);
            ServerInfo info = new ServerInfo(serverName, playerCount, isOffline);
            serverInfoList.add(info);
        }

        // Sort the serverInfoList by server number extracted from server names
        serverInfoList.sort(Comparator.comparingInt(info -> extractNumber(info.serverName)));

        int server_count = serverInfoList.size();
        Main.logger.error(String.valueOf(server_count));

        // Calculate the required number of rows
        int rows = calculateRows(server_count);
        int itemsPerRow = 7;

        // Populate each server's information in the GUI
        for (int i = 0; i < serverInfoList.size(); i++) {
            ServerInfo info = serverInfoList.get(i);
            String serverName = info.serverName;
            int serverNumber = extractNumber(serverName); // Extract the number from the server name
            int playerCount = info.playerCount;
            int slot = getSlot(i);

            // Determine if this is the current server
            boolean isCurrentServer = serverName.equals(currentServer);

            // Check if the server is offline
            boolean isOffline = info.isOffline;

            String displayName;
            List<List<String>> lore;
            String itemMaterial;
            String clickAction = null; // Default to null; set if server is online

            if (isOffline) {
                // Server is offline
                displayName = "<red>Lobby #" + serverNumber + " (Offline)";
                lore = List.of(
                        List.of("Players: " + playerCount),
                        List.of(""),
                        List.of("<red>This lobby is currently offline")
                );
                itemMaterial = "minecraft:netherite_block"; // Use a barrier block to represent offline servers
                // No click action for offline servers
                clickAction = ""; // Or set to null
            } else {
                // Server is online
                if (isCurrentServer) {
                    displayName = "<green>Lobby #" + serverNumber;
                    lore = List.of(
                            List.of("Players: " + playerCount),
                            List.of(""),
                            List.of("<green>You are connected to this lobby")
                    );
                    itemMaterial = "minecraft:emerald_block";
                } else {
                    displayName = "<yellow>Lobby #" + serverNumber;
                    lore = List.of(
                            List.of("Players: " + playerCount),
                            List.of(""),
                            List.of("<yellow>Click to connect to this lobby")
                    );
                    itemMaterial = "minecraft:iron_block";
                }
                // Set click action to connect to the server
                clickAction = "rip.snicon.compass.proxy.Connect.ConnectPlayerToServer(player," + serverName + ")";
            }

            Main.logger.error(displayName);

            // Create the container item
            ContainerItem item = new ContainerItem(slot, itemMaterial, 1);
            item.setDisplay(new ContainerItemDisplay(
                    List.of(displayName),
                    lore,
                    false, "", true
            ));

            if (clickAction != null && !clickAction.isEmpty()) {
                item.setData(new ContainerItemData(clickAction, "", false));
            } else {
                // For offline servers, set item data to prevent any action
                item.setData(new ContainerItemData("", "", false));
            }

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

        // Log the number of rows for debugging
        Main.logger.info(String.valueOf(rows));

        // Set placeholders and open the container
        PlaceholderManager.setPlaceholderToPlayer(player, "lobbies", jsonItems.toString());
        PlaceholderManager.setPlaceholderToPlayer(player, "lobby_selector_rows", String.valueOf(rows));
        ContainerCreator.openContainer(player, "lobby_selector");
    }

    // Helper class to hold server information
    static class ServerInfo {
        String serverName;
        int playerCount;
        boolean isOffline;

        public ServerInfo(String serverName, int playerCount, boolean isOffline) {
            this.serverName = serverName;
            this.playerCount = playerCount;
            this.isOffline = isOffline;
        }
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
        int rows = 2;
        while (remainingServers > 0) {
            if (remainingServers >= 7) {
                remainingServers -= 7;
                rows += 1;
            } else {
                remainingServers = 0;
                rows += 1;
            }

        }
        Main.logger.error(String.valueOf(rows));
        // Enforce minimum of 3 rows and maximum of 6 rows
        return Math.min(6, Math.max(3, rows));
    }

    public static int extractNumber(String serverName) {
        Pattern pattern = Pattern.compile("-(\\d+)");
        Matcher matcher = pattern.matcher(serverName);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 666; // Return a large value if no match is found
    }
}
