package rip.snicon.compass.listeners.player;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.*;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.sound.SoundEvent;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.inventory.inventories.DefaultInventory;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.data.cosmetics.PlayerPerk;
import rip.snicon.compass.sidebar.MysterySidebar;
import rip.snicon.compass.utils.TabUtils;

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
        handlePlayerFlyEvents();
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
                player.getRegionHandler().updateRegion();
                new DefaultInventory().constructPlayerInventory(player);
                TabUtils.setPlayerTab(player);
                player.getDataHandler().checkForProfessionLevelUp();
                player.getCosmeticHandler().applyEnabledCosmetics();
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
                // Allow flying if the player has FLY or JUMPBOOST enabled
                if (player.isOnGround() && !player.isAllowFlying()) {
                    if (player.getCosmeticHandler().isEnabled(PlayerPerk.FLY) || player.getCosmeticHandler().isEnabled(PlayerPerk.JUMPBOOST)) {
                        player.setAllowFlying(true);
                    }
                }
            }
        });
    }

    private void handlePlayerFlyEvents() {
        // Handle stop flying event
        this.mysteryPlayerNode.addListener(PlayerStopFlyingEvent.class, event -> {
            if (event.getPlayer() instanceof MysteryPlayer player) {
                if (player.isFlying()) {
                    player.setFlying(false);
                }
            }
        });

        // Handle start flying event
        this.mysteryPlayerNode.addListener(PlayerStartFlyingEvent.class, event -> {
            if (event.getPlayer() instanceof MysteryPlayer player) {
                // Check if the player has FLY or JUMPBOOST enabled
                if (player.getCosmeticHandler().isEnabled(PlayerPerk.FLY)) {
                    // Allow the player to fly
                    player.setAllowFlying(true);
                    player.setFlying(true);
                    player.sendMessage("You are now flying!");
                } else if (player.getCosmeticHandler().isEnabled(PlayerPerk.JUMPBOOST)) {
                    // Apply a jump boost instead of flying
                    player.setFlying(false);
                    player.setAllowFlying(false);

                    // Apply a jump boost velocity
                    player.setVelocity(player.getPosition().direction().mul(30).add(0,2,0));
                    player.playSound(Sound.sound(SoundEvent.ENTITY_FIREWORK_ROCKET_BLAST, Sound.Source.MASTER, 1, 0.2f));
                } else {
                    // Disable flying if neither perk is enabled
                    player.setFlying(false);
                    player.setAllowFlying(false);
                }
            }
        });
    }


    /**
     * Handles the PlayerDisconnectEvent, saving player state and clearing caches.
     */
    private void handlePlayerDisconnectEvent() {
        this.mysteryPlayerNode.addListener(PlayerDisconnectEvent.class, event -> {
            if (event.getPlayer() instanceof MysteryPlayer player) {
                player.getDataHandler().saveDataToDatabase();
                player.getRegionHandler().saveRegionsToDatabase();
                player.getBundleHandler().saveBundlesToDatabase();
                player.getCosmeticHandler().saveCosmeticsToDatabase();
                player.getToggleHandler().saveTogglesToDatabase();

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
                player.getSettingsHandler().fetchSettingsFromDatabase();
                player.getBundleHandler().fetchBundlesFromDatabase();
                player.getCosmeticHandler().fetchCosmeticsFromDatabase();
                player.getToggleHandler().fetchTogglesFromDatabase();
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
            event.setCancelled(true);
        });

        // Prevent block placing
        this.mysteryPlayerNode.addListener(PlayerBlockPlaceEvent.class, event -> {
            event.setCancelled(true);
        });
    }

}
