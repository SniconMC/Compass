package rip.snicon.compass.player.bundle;

import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.utils.MysteryRarities;

import java.util.List;

public class MysteryBundle {

    private final String name;
    private final MysteryRarities rarity;
    private final List<String> materials; // List for block materials
    private final List<String> entities;  // List for entities
    private final String displayItem;     // The item to represent the bundle in menus

    public MysteryBundle(@NotNull String name, @NotNull MysteryRarities rarity,
                         @NotNull List<String> materials, @NotNull List<String> entities,
                         @NotNull String displayItem) {
        this.name = name;
        this.rarity = rarity;
        this.materials = materials;
        this.entities = entities;
        this.displayItem = displayItem;
    }

    public String getName() {
        return name;
    }

    public MysteryRarities getRarity() {
        return rarity;
    }

    public List<String> getMaterials() {
        return materials;
    }

    public List<String> getEntities() {
        return entities;
    }

    public String getDisplayItem() {
        return displayItem;
    }

    public static MysteryBundle createBundleFromType(MysteryBundleTypes bundleType) {
        return new MysteryBundle(
                bundleType.getDisplayName(),
                bundleType.getRarity(),
                bundleType.getMaterialNames(),
                bundleType.getEntityNames() != null ? bundleType.getEntityNames() : List.of(),
                bundleType.getDisplayItem().name()
        );
    }
}
