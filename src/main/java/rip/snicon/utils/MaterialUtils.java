package rip.snicon.utils;

import net.minestom.server.item.Material;
import net.minestom.server.entity.Player;
import rip.snicon.Main;

public class MaterialUtils {

    public static Material convertToNamespaceIdMaterial(String itemId, Player player) {
        // Remove "minecraft:" prefix if present and handle placeholders
        String cleanedId;
        if (PlaceholderReplacer.containsPlaceholders(itemId)) {
            String placeholderId = PlaceholderReplacer.replacePlaceholders(player, itemId);
            cleanedId = placeholderId.replace("minecraft:", "").toUpperCase();
        } else {
            cleanedId = itemId.replace("minecraft:", "").toUpperCase();
        }

        // Convert the cleaned ID to a namespace ID format
        String namespaceId = "minecraft:" + cleanedId.toLowerCase();

        try {
            // Attempt to convert the cleaned ID to a Material
            Material material = Material.fromNamespaceId(namespaceId);
            if (material == null) {
                throw new IllegalArgumentException("Invalid material ID: " + namespaceId);
            }
            return material;
        } catch (IllegalArgumentException e) {
            // Handle the case where the cleaned ID is not a valid Material
            Main.logger.error("Invalid material ID: " + namespaceId);
            return Material.AIR; // Return a default or placeholder material if the conversion fails
        }
    }

    public static String convertMaterialToNamespaceId(Material material) {
        if (material == null) {
            return "minecraft:air"; // Return a default or placeholder namespace if the material is null
        }

        // Return the namespace ID in the proper format
        return material.namespace().toString();
    }


}
