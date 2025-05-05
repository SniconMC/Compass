package rip.snicon.compass.player.handler;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.database.mongodb.MongoDatabaseManager;
import rip.snicon.compass.instances.regions.MysteryRegion;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.utils.TextUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MysteryRegionHandler {

    private final UUID uuid;
    private final Set<MysteryRegion> discoveredRegions = new HashSet<>();
    private MysteryRegion currentRegion;

    public MysteryRegionHandler(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Fetches region data from the database.
     */
    public void fetchRegionsFromDatabase() {
        MongoDatabaseManager.fetch("regions", "uuid", uuid.toString()).thenAccept(document -> {
            if (document != null) {
                loadRegionData(document);
            } else {
                saveRegionsToDatabase();
            }
        });
    }

    /**
     * Saves region data to the database.
     */
    public void saveRegionsToDatabase() {
        Document regionDocument = new Document("uuid", uuid.toString());

        // Save current region if it exists
        if (currentRegion != null) {
            regionDocument.append("currentRegion", currentRegion.name());
        }

        // Save discovered regions
        List<String> discovered = discoveredRegions.stream().map(Enum::name).toList();
        regionDocument.append("discoveredRegions", discovered);

        MongoDatabaseManager.save("regions", "uuid", regionDocument).exceptionally(throwable -> {
            System.err.println("Failed to save region data: " + throwable.getMessage());
            return null;
        });
    }

    /**
     * Loads region data from a database document.
     */
    private void loadRegionData(Document document) {
        // Load current region
        if (document.containsKey("currentRegion")) {
            String regionName = document.getString("currentRegion");
            currentRegion = MysteryRegion.valueOf(regionName);
        }

        // Load discovered regions
        if (document.containsKey("discoveredRegions")) {
            List<String> regionNames = document.getList("discoveredRegions", String.class);
            for (String name : regionNames) {
                discoveredRegions.add(MysteryRegion.valueOf(name));
            }
        }
    }

    /**
     * Updates the player's region based on their position.
     */
    public void updateRegion() {
        MysteryPlayer player = getPlayer();
        if (player == null) {
            return; // Player not online
        }

        //currentRegion.debug(player);

        Pos position = player.getPosition();

        for (MysteryRegion newRegion : MysteryRegion.values()) {
            if (newRegion.isInside(position)) {
                if (currentRegion != newRegion) {
                    currentRegion = newRegion;

                    // Handle region discovery
                    if (!discoveredRegions.contains(newRegion)) {
                        discoverRegion(newRegion);

                        String message = String.format(
                                "You discovered: %s and gained %.1f emeralds and %.1f XP!",
                                newRegion.getDisplayName(), newRegion.getEmeralds(), newRegion.getXp()
                        );
                        player.sendMessage(TextUtils.convertStringToComponent(message));
                    } else {
                        player.sendActionBar(TextUtils.convertStringToComponent(newRegion.getDisplayName()));
                    }

                    saveRegionsToDatabase(); // Save the updated regions to the database
                } else {
                    // Update text for undiscovered neighboring regions
                    for (MysteryRegion neighbor : newRegion.getBorderingRegions()) {
                        if (!discoveredRegions.contains(neighbor)) {
                            neighbor.updateRegionText(player);
                        }
                    }
                }
                break;
            }
        }
    }

    /**
     * Discovers a new region for the player.
     *
     * @param region the new region to be discovered
     */
    private void discoverRegion(MysteryRegion region) {
        MysteryPlayer player = getPlayer();
        if (player == null) {
            return; // Player not online
        }

        discoveredRegions.add(region);

        // Update player stats
        player.getDataHandler().updateEmeralds(region.getEmeralds(), false);
        player.getDataHandler().updateProfessionXp(region.getXp(), false);
    }

    /**
     * Gets the list of regions neighboring the current region.
     *
     * @return a list of neighboring regions
     */
    public List<MysteryRegion> getNeighboringRegions() {
        if (currentRegion == null) {
            return Collections.emptyList();
        }
        return currentRegion.getBorderingRegions();
    }

    /**
     * Checks if the player is currently in a specific region.
     *
     * @param region the region to check
     * @return true if the player is in the specified region, otherwise false
     */
    public boolean isInRegion(MysteryRegion region) {
        return currentRegion == region;
    }

    /**
     * Gets the list of discovered regions.
     *
     * @return an unmodifiable set of discovered regions
     */
    public Set<MysteryRegion> getDiscoveredRegions() {
        return Collections.unmodifiableSet(discoveredRegions);
    }

    /**
     * Gets the current region.
     *
     * @return the current region
     */
    public MysteryRegion getCurrentRegion() {
        if (currentRegion == null) return MysteryRegion.SPAWN;
        return currentRegion;
    }

    /**
     * Sets the current region (used for initialization or debugging purposes).
     *
     * @param region the region to set as current
     */
    public void setCurrentRegion(MysteryRegion region) {
        this.currentRegion = region;
    }

    /**
     * Gets the player instance associated with this handler.
     *
     * @return the player instance, or null if the player is not online
     */
    private MysteryPlayer getPlayer() {
        return (MysteryPlayer) MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);
    }
}
