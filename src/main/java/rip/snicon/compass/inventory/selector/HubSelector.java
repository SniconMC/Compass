package rip.snicon.compass.inventory.selector;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import nub.wi1helm.template.inventory.items.BackgroundItem;
import nub.wi1helm.template.inventory.items.CloseButton;
import rip.snicon.compass.Main;
import rip.snicon.compass.utils.TextUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HubSelector extends TemplateInventory {

    private static final int ITEMS_PER_ROW = 7;
    private List<ServerInfo> serverInfoList;
    private String currentServer;

    public HubSelector() {
        super(TextUtils.convertStringToComponent("Universe Selector"), InventoryType.CHEST_3_ROW);
        this.serverInfoList = new ArrayList<>();
    }

    @Override
    protected void initialize() {
        fillInventory(new BackgroundItem());
        setItem(22, new CloseButton());
    }

    @Override
    protected void personalize(Player player) {
        if (serverInfoList.isEmpty()) {
            return;
        }

        // Populate each server's information in the GUI
        for (int i = 0; i < Math.max(serverInfoList.size(),ITEMS_PER_ROW); i++) {



            int slot = getSlot(i);

            if (i < serverInfoList.size()) {
                ServerInfo info = serverInfoList.get(i);
                // Add the server item to the inventory
                setItem(slot, createServerItem(info));
            } else {
                setItem(slot, new TemplateItem(Material.AIR) {
                    @Override
                    protected void initialize() {

                    }

                    @Override
                    protected void personalize(Player player) {

                    }

                    @Override
                    public void onUse(TemplateInventoryEvent templateInventoryEvent) {

                    }

                    @Override
                    public void onDrop(TemplateInventoryEvent templateInventoryEvent) {

                    }
                });
            }


        }
    }

    /**
     * Creates a server item based on server information
     */
    private TemplateItem createServerItem(ServerInfo info) {
        String serverName = info.serverName;
        int serverNumber = info.number;
        int playerCount = info.playerCount;
        boolean isCurrentServer = serverName.equals(currentServer);
        boolean isOffline = info.isOffline;

        // Determine material based on server status
        Material material;
        if (isOffline) {
            material = Material.NETHERITE_BLOCK;
        } else if (isCurrentServer) {
            material = Material.EMERALD_BLOCK;
        } else {
            material = Material.IRON_BLOCK;
        }

        // Create template item for this server
        return new TemplateItem(material) {
            @Override
            protected void initialize() {
                // Set name based on server status
                if (isOffline) {
                    setName(TextUtils.convertStringToComponent("<red>Lobby #" + serverNumber + " (Offline)"));
                } else if (isCurrentServer) {
                    setName(TextUtils.convertStringToComponent("<green>Lobby #" + serverNumber));
                } else {
                    setName(TextUtils.convertStringToComponent("<yellow>Lobby #" + serverNumber));
                }

                // Set lore based on server status
                List<String> lore = new ArrayList<>();
                lore.add(info.serverName);
                lore.add("");
                lore.add("<white>Players:</white><aqua> " + playerCount + "/16</aqua>");
                lore.add("");

                if (isOffline) {
                    lore.add("<red>This lobby is currently offline");
                } else if (isCurrentServer) {
                    lore.add("<green>You are connected to this lobby");
                } else {
                    lore.add("<yellow>Click to connect to this lobby");
                }

                setLore(TextUtils.convertStringToComponent(lore));
            }

            @Override
            protected void personalize(Player player) {
                // No personalization needed
            }

            @Override
            public void onUse(TemplateInventoryEvent event) {
                // Don't do anything if server is offline or it's the current server
                if (isOffline || isCurrentServer) {
                    return;
                }

                // Connect player to server
                connectPlayerToServer(event.getPlayer(), serverName);
            }

            @Override
            public void onDrop(TemplateInventoryEvent event) {
                // No action on drop
            }
        };
    }

    /**
     * Connects a player to a specified server
     */
    private void connectPlayerToServer(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        player.sendPluginMessage("bungeecord:main", out.toByteArray());
    }

    /**
     * Requests server information and opens the selector when data is received
     */
    public static void requestServerInfo(Player player) {
        Main.logger.info("Requesting server information");
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ServerSelectorInfo");
        player.sendPluginMessage("bungeecord:main", out.toByteArray());

        // Note: You'll need to implement a listener for the response
        // and call updateSelectorWithServerInfo when the data is received
    }

    /**
     * Updates the selector with server information and opens it for the player
     */
    public static void updateSelectorWithServerInfo(Player player, List<String> servers,
                                                    String currentServer, int[] playerCounts,
                                                    List<String> offlineServers) {
        if (servers == null || servers.isEmpty()) {
            Main.logger.error("No servers available");
            return;
        }

        // Create a new HubSelector instance
        HubSelector selector = new HubSelector();
        selector.currentServer = currentServer;

        // Create ServerInfo objects for each server
        List<ServerInfo> serverInfoList = new ArrayList<>();
        for (int i = 0; i < servers.size(); i++) {
            String serverName = servers.get(i);
            int playerCount = playerCounts[i];
            boolean isOffline = offlineServers.contains(serverName);
            ServerInfo info = new ServerInfo(serverName, playerCount, isOffline, i);
            serverInfoList.add(info);
        }

        selector.serverInfoList = serverInfoList;

        // Open the inventory for the player
        player.openInventory(selector.constructInventory(player));
    }

    /**
     * Helper class to hold server information
     */
    static class ServerInfo {
        String serverName;
        int playerCount;
        boolean isOffline;
        int number;

        public ServerInfo(String serverName, int playerCount, boolean isOffline, int number) {
            this.serverName = serverName;
            this.playerCount = playerCount;
            this.isOffline = isOffline;
            this.number = number;
        }
    }

    /**
     * Calculates the slot position for a server index
     */
    private static int getSlot(int index) {
        int row = index / ITEMS_PER_ROW;  // Row number based on index
        int col = index % ITEMS_PER_ROW;  // Column within the row

        // Calculate slot position: Row offset starts at 10 (second row, second column)
        return 10 + (row * 9) + col;
    }

    /**
     * Extracts the server number from a server name
     */
    public static int extractNumber(String serverName) {
        Pattern pattern = Pattern.compile("-(\\d+)");
        Matcher matcher = pattern.matcher(serverName);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 666; // Return a large value if no match is found
    }
}