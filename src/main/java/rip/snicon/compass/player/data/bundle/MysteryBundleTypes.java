package rip.snicon.compass.player.data.bundle;

import net.minestom.server.entity.EntityType;
import net.minestom.server.item.Material;
import rip.snicon.compass.utils.MysteryRarities;

import java.util.List;
import java.util.stream.Collectors;

public enum MysteryBundleTypes {

    STARTER_BUNDLE(
            "Starter Bundle",
            MysteryRarities.COMMON,
            Material.GRASS_BLOCK, // Display item
            List.of(Material.STONE, Material.GRASS_BLOCK, Material.DIRT),
            List.of(EntityType.ZOMBIE)
    ),
    WOODLAND_BUNDLE(
            "Woodland Bundle",
            MysteryRarities.UNCOMMON,
            Material.OAK_LOG, // Display item
            List.of(Material.OAK_LOG, Material.BIRCH_LOG, Material.SPRUCE_LOG, Material.OAK_LEAVES),
            List.of(EntityType.VILLAGER)
    ),
    MINER_BUNDLE(
            "Miner Bundle",
            MysteryRarities.RARE,
            Material.IRON_ORE, // Display item
            List.of(Material.COAL_ORE, Material.IRON_ORE, Material.STONE, Material.GRAVEL),
            List.of()
    ),
    PRECIOUS_BUNDLE(
            "Precious Bundle",
            MysteryRarities.EPIC,
            Material.DIAMOND_BLOCK, // Display item
            List.of(Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.EMERALD_BLOCK, Material.LAPIS_BLOCK),
            List.of()
    ),
    MYTHICAL_BUNDLE(
            "Mythical Bundle",
            MysteryRarities.MYTHIC,
            Material.DRAGON_EGG, // Display item
            List.of(Material.END_STONE, Material.DRAGON_EGG, Material.SHULKER_BOX),
            List.of()
    ),
    UNIQUE_BUNDLE(
            "Unique Bundle",
            MysteryRarities.UNIQUE,
            Material.BEACON, // Display item
            List.of(Material.BEACON, Material.COMMAND_BLOCK),
            List.of(EntityType.VILLAGER, EntityType.WITHER, EntityType.ENDER_DRAGON)
    );

    private final String displayName;
    private final MysteryRarities rarity;
    private final Material displayItem;
    private final List<Material> material;
    private final List<EntityType> entities;

    MysteryBundleTypes(String displayName, MysteryRarities rarity, Material displayItem,
                       List<Material> materials, List<EntityType> entities) {
        this.displayName = displayName;
        this.rarity = rarity;
        this.displayItem = displayItem;
        this.material = materials;
        this.entities = entities;
    }

    public String getDisplayName() {
        return displayName;
    }

    public MysteryRarities getRarity() {
        return rarity;
    }

    public Material getDisplayItem() {
        return displayItem;
    }

    public List<Material> getMaterials() {
        return material;
    }

    public List<EntityType> getEntities() {
        return entities;
    }
}
