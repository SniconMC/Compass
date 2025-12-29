package rip.snicon.compass.inventory.selector;

import build.buf.gen.minekube.gate.v1.Server;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import nub.wi1helm.smoxy.SMoxy;
import nub.wi1helm.smoxy.SMoxyService;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import nub.wi1helm.template.inventory.items.BackgroundItem;
import nub.wi1helm.template.inventory.items.CloseButton;
import rip.snicon.compass.Main;
import rip.snicon.compass.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class HubSelector extends TemplateInventory {

    private static final int ITEMS_PER_ROW = 7;
    private List<Server> serverList;

    public HubSelector() {
        super(TextUtils.convertStringToComponent("Universe Selector"), InventoryType.CHEST_3_ROW);
        this.serverList = new ArrayList<>();
    }

    @Override
    protected void initialize() {
        fillInventory(new BackgroundItem());
        setItem(22, new CloseButton());
    }

    @Override
    protected void personalize(Player player) {
        if (serverList.isEmpty()) {
            return;
        }

        // Populate each server's information in the GUI
        for (int i = 0; i < Math.max(serverList.size(), ITEMS_PER_ROW); i++) {
            int slot = getSlot(i);

            if (i < serverList.size()) {
                Server server = serverList.get(i);
                // Add the server item to the inventory
                setItem(slot, createServerItem(server, i));
            } else {
                setItem(slot, new TemplateItem(Material.AIR) {
                    @Override
                    protected void initialize() {
                        // Empty item for spacing
                    }

                    @Override
                    protected void personalize(Player player) {
                        // No personalization needed
                    }

                    @Override
                    public void onUse(TemplateInventoryEvent templateInventoryEvent) {
                        // No action on use
                    }

                    @Override
                    public void onDrop(TemplateInventoryEvent templateInventoryEvent) {
                        // No action on drop
                    }
                });
            }
        }
    }

    /**
     * Creates a server item based on server information
     */
    private TemplateItem createServerItem(Server server, int index) {
        String serverName = server.getName();
        int serverNumber = index + 1;
        int playerCount = server.getPlayers();
        boolean isCurrentServer = serverName.equals(SMoxy.serverName);
        boolean isOffline = !server.isInitialized();

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
                lore.add(serverName);
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

                // Connect player to server using ServerRegistry
                SMoxyService.connectPlayerToServer(event.getPlayer().getUsername(), serverName);
                event.getPlayer().sendMessage(TextUtils.convertStringToComponent("<green>Connecting to " + serverName + "...</green>"));

                // Close the inventory
                event.getPlayer().closeInventory();
            }

            @Override
            public void onDrop(TemplateInventoryEvent event) {
                // No action on drop
            }
        };
    }

    /**
     * Opens the server selector for a player using the gRPC API
     */
    public static void openSelector(Player player) {
        Main.logger.info("Opening server selector using gRPC API");

        // Create a new HubSelector instance
        HubSelector selector = new HubSelector();

        try {
            // Get servers from the ServerRegistry
            List<Server> servers = SMoxyService.listServers();
            Main.logger.error(servers.toString());
            if (servers.isEmpty()) {
                Main.logger.error("No servers available from ServerRegistry");
                player.sendMessage(TextUtils.convertStringToComponent("<red>No servers available at this time.</red>"));
                return;
            }

            selector.serverList = servers;

            // Open the inventory for the player
            player.openInventory(selector.constructInventory(player));
        } catch (Exception e) {
            Main.logger.error("Failed to get server information: " + e.getMessage());
            player.sendMessage(TextUtils.convertStringToComponent("<red>Failed to retrieve server list.</red>"));
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
}