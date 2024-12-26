package rip.snicon.compass.listeners.player;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.*;
import net.minestom.server.event.trait.PlayerEvent;
import rip.snicon.compass.Main;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.sidebar.MysterySidebar;

public class MysteryPlayerNode {

    private final EventNode<PlayerEvent> mysteryPlayerNode;

    public MysteryPlayerNode(EventNode<Event> parent) {
        this.mysteryPlayerNode = EventNode.type("mystery_player", EventFilter.PLAYER);
        registerListeners();
        parent.addChild(mysteryPlayerNode);
    }

    /**
     * Registers all event listeners for MysteryPlayerNode.
     */
    private void registerListeners() {
        handlePlayerSpawnEvent();
        handlePlayerMoveEvent();
        handlePlayerDisconnectEvent();
        handlePlayerConfigurationEvent();
        handleCancelledEvents();
    }

    /**
     * Handles the PlayerSpawnEvent, initializing player state on spawn.
     */
    private void handlePlayerSpawnEvent() {
        this.mysteryPlayerNode.addListener(PlayerSpawnEvent.class, event -> {
            if (event.getPlayer() instanceof MysteryPlayer player) {
                player.loadPlayerInventory();
                player.getRegionHandler().updateRegion();
            }
        });
    }

    /**
     * Handles the PlayerMoveEvent, updating the region as the player moves.
     */
    private void handlePlayerMoveEvent() {
        this.mysteryPlayerNode.addListener(PlayerMoveEvent.class, event -> {
            if (event.getPlayer() instanceof MysteryPlayer player) {
                player.getRegionHandler().updateRegion();
            }
        });
    }

    /**
     * Handles the PlayerDisconnectEvent, saving player state and clearing caches.
     */
    private void handlePlayerDisconnectEvent() {
        this.mysteryPlayerNode.addListener(PlayerDisconnectEvent.class, event -> {
            if (event.getPlayer() instanceof MysteryPlayer player) {
                player.unloadPlayerInventory();
                player.getDataHandler().saveDataToDatabase();
                player.getRegionHandler().saveRegionsToDatabase();
                player.getInventoryHandler().saveInventoryToDatabase();

                // Clear cached sidebar and other player data
                MysterySidebar.getSidebarCache().remove(player.getUuid());
            }
        });
    }

    /**
     * Handles the AsyncPlayerConfigurationEvent, fetching data and setting the spawn point.
     */
    private void handlePlayerConfigurationEvent() {
        this.mysteryPlayerNode.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            if (event.getPlayer() instanceof MysteryPlayer player) {
                player.getDataHandler().fetchDataFromDatabase();
                player.getRegionHandler().fetchRegionsFromDatabase();
                player.getInventoryHandler().fetchInventoryFromDatabase();
                player.getSettingsHandler().fetchSettingsFromDatabase();
                event.setSpawningInstance(MysteryInstanceType.HUB.getInstance());
                event.getPlayer().setRespawnPoint(MysteryInstanceType.HUB.getInstance().getSpawnPos());
            }
        });
    }

    /**
     * Handles events to be canceled or modified based on server rules.
     */
    private void handleCancelledEvents() {
        // Prevent block breaking with additional actions
        this.mysteryPlayerNode.addListener(PlayerBlockBreakEvent.class, event -> {
            if (event.getPlayer() instanceof MysteryPlayer player) {
                player.getDataHandler().updateProfessionXp(2000, true);
                player.getInventoryHandler().addItem(player.getInventory(), MysteryItemType.EXAMPLE_ITEM);
            }
            event.setCancelled(true);
        });

        // Prevent block placing
        this.mysteryPlayerNode.addListener(PlayerBlockPlaceEvent.class, event -> {
            event.setCancelled(true);
        });
    }
}
