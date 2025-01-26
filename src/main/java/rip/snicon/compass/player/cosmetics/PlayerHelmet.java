package rip.snicon.compass.player.cosmetics;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public enum PlayerHelmet implements BaseCosmetic {
    DIAMOND_HELMET("diamond_helmet", ItemStack.of(Material.DIAMOND_HELMET)),
    IRON_HELMET("iron_helmet", ItemStack.of(Material.IRON_HELMET));

    private final String name;
    private final ItemStack itemStack; // Helmet item representation
    private final Set<Object> conflicts = new HashSet<>();

    PlayerHelmet(String name, ItemStack itemStack) {
        this.name = name;
        this.itemStack = itemStack;
    }

    static {
        for (PlayerHelmet helmet : PlayerHelmet.values()) {
            helmet.conflicts.add(PlayerHelmet.class); // All helmets conflict with the entire PlayerHelmet category
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isOwned() {
        throw new UnsupportedOperationException("Use the handler to check ownership.");
    }

    @Override
    public void setOwned(boolean owned) {
        throw new UnsupportedOperationException("Use the handler to manage ownership.");
    }

    @Override
    public void onEnable(UUID uuid) {
        MysteryPlayer player = MysteryPlayer.getPlayer(uuid);
        if (player != null) {
            player.getInventory().setHelmet(itemStack); // Apply the helmet to the player's inventory
        } else {
            throw new IllegalStateException("Player not found for UUID: " + uuid);
        }
    }

    @Override
    public void onDisable(UUID uuid) {
        MysteryPlayer player = MysteryPlayer.getPlayer(uuid);
        if (player != null) {
            player.getInventory().setHelmet(ItemStack.AIR); // Remove the helmet from the player's inventory
        } else {
            throw new IllegalStateException("Player not found for UUID: " + uuid);
        }
    }

    @Override
    public Set<Object> getConflicts() {
        return Collections.unmodifiableSet(conflicts);
    }
}
