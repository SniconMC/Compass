package rip.snicon.compass.player.data.bundle;

import net.minestom.server.entity.EntityType;
import net.minestom.server.item.Material;
import net.minestom.server.registry.StaticProtocolObject;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.Main;
import rip.snicon.compass.utils.MysteryRarities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MysteryBundle {

    private final String name;
    private final MysteryRarities rarity;
    private final List<Material> materials; // List for block materials
    private final List<EntityType> entities;  // List for entities
    private final String displayItem;     // The item to represent the bundle in menus

    public MysteryBundle(@NotNull String name, @NotNull MysteryRarities rarity,
                         @NotNull List<Material> materials, @NotNull List<EntityType> entities,
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

    public List<Material> getMaterials() {
        return materials;
    }

    public List<EntityType> getEntities() {
        return entities;
    }

    public List<String> getMaterialsNames() {
        return materials.stream()
                .map(Material::name) // Convert Material to String
                .collect(Collectors.toList());
    }

    public List<String> getEntitiesNames() {
        if (entities == null) return List.of();

        return entities.stream()
                .map(StaticProtocolObject::name) // Ensure consistency
                .collect(Collectors.toList());
    }


    public String getDisplayItem() {
        return displayItem;
    }

    public static MysteryBundle createBundleFromType(MysteryBundleTypes bundleType) {
        return new MysteryBundle(
                bundleType.getDisplayName(),
                bundleType.getRarity(),
                bundleType.getMaterials(),
                bundleType.getEntities(),
                bundleType.getDisplayItem().name()
        );
    }

    public static List<Material> getSpawnEggForEntity(List<EntityType> entityTypes) {
        if (entityTypes == null) return new ArrayList<>();
        List<Material> m = new ArrayList<>();

        entityTypes.forEach(entityType -> {
            Main.logger.debug(entityType.toString());
            String spawnEggId = entityType.toString() + "_spawn_egg";
            Material material = Material.fromNamespaceId(spawnEggId);
            Main.logger.debug(spawnEggId);
            m.add(material);
        });


        // If material doesn't exist, return AIR as a fallback
        return m;
    }

    public static List<Material> convertBlockListToMaterials(List<String> blockNames) {
        return blockNames.stream()
                .map(Material::fromNamespaceId)
                .filter(material -> material != null && material.isBlock()) // Ensure it's a block
                .collect(Collectors.toList());
    }

    public static List<EntityType> convertEntityListToEntityTypes(List<String> entities) {
        return entities.stream()
                .map(EntityType::fromNamespaceId)
                .filter(Objects::nonNull) // Ensure it's a block
                .collect(Collectors.toList());
    }
}
