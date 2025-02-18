package rip.snicon.compass.player.data.bundle;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.Material;
import net.minestom.server.registry.StaticProtocolObject;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.Main;
import rip.snicon.compass.player.data.MysteryRarities;
import rip.snicon.compass.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MysteryBundle {

    private final String name;
    private final MysteryRarities rarity;
    private final List<Material> materials;
    private final List<EntityType> entities;
    private final String displayItem;
    private int amount; // Number of times this bundle can be used
    private final boolean isInfinite; // Determines if it has unlimited uses

    public MysteryBundle(@NotNull String name, @NotNull MysteryRarities rarity,
                         @NotNull List<Material> materials, @NotNull List<EntityType> entities,
                         @NotNull String displayItem, int amount, boolean isInfinite) {
        this.name = name;
        this.rarity = rarity;
        this.materials = materials;
        this.entities = entities;
        this.displayItem = displayItem;
        this.amount = amount;
        this.isInfinite = isInfinite;
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
        return entities.stream()
                .map(StaticProtocolObject::name) // Ensure consistency
                .collect(Collectors.toList());
    }

    public String getDisplayItem() {
        return displayItem;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isInfinite() {
        return isInfinite;
    }

    public void useBundle() {
        if (!isInfinite && amount > 0) {
            amount--;
        }
    }

    public void addAmount(int additional) {
        this.amount += additional;
    }

    public static MysteryBundle createBundleFromType(MysteryBundleTypes bundleType, int amount, boolean isInfinite) {
        return new MysteryBundle(
                bundleType.getDisplayName(),
                bundleType.getRarity(),
                bundleType.getMaterials(),
                bundleType.getEntities(),
                bundleType.getDisplayItem().name(),
                amount,
                isInfinite
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

    public Component getDisplayNameComponent() {
        return TextUtils.convertStringToComponent(
                String.format(
                        "<color:%s>%s</color>",
                        rarity.getColor(),
                        name.replace("_", " ")
                )
        );
    }



    public List<Component> getLoreComponent() {
        return TextUtils.convertStringToComponent(List.of(
                "<gray>This bundle is a consumable and </gray>",
                "<gray>can be repurchased at various locations.</gray>",
                "",
                "<yellow>Amount: <aqua>" + (isInfinite ? "Infinite" : amount) + "</aqua></yellow>",
                "",
                "<bold><color:" + rarity.getColor() + ">" + rarity.name().toUpperCase() + "</color></bold>"
        ));
    }


}

