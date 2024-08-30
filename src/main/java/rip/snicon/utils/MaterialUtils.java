package rip.snicon.utils;

import net.minestom.server.item.Material;
import net.minestom.server.entity.Player;
import rip.snicon.Main;

/**
 * Utility class for handling operations related to {@link Material} in a Minecraft server.
 *
 * <p>This class provides methods to convert string representations of item IDs to
 * {@link Material} instances, and vice versa, ensuring that item IDs conform to the
 * correct namespace format ("minecraft:") used within the game.</p>
 *
 * @see Material
 * @see Player
 * @see <a href="https://javadoc.minestom.net/">Minestom Documentation</a>
 *
 * @author Wi1helm
 * @author znopp
 */
public class MaterialUtils {

    /**
     * Converts a string representation of an item ID to a {@link Material} object, ensuring
     * the correct namespace prefix ("minecraft:"). If the conversion fails, a default
     * material ({@link Material#AIR}) is returned.
     *
     * @param itemId The string representation of the item ID to convert (e.g., "diamond_sword").
     * @param player The {@link Player} for whom the conversion is being performed. This can be
     *               useful for context-specific error handling or logging.
     * @return The corresponding {@link Material} object, or {@link Material#AIR} if the conversion fails.
     *
     * @author Wi1helm
     * @author znopp
     */
    public static Material convertToNamespaceIdMaterial(String itemId, Player player) {

        // Convert the ID to lowercase and ensure it has the "minecraft:" prefix
        String namespaceId = itemId.toLowerCase();
        if (!namespaceId.startsWith("minecraft:")) {
            namespaceId = "minecraft:" + namespaceId;
        }

        try {
            // Attempt to convert the cleaned ID to a Material
            Material material = Material.fromNamespaceId(namespaceId);
            if (material == null) {
                throw new IllegalArgumentException("Invalid material ID: " + namespaceId);
            }
            return material;
        } catch (IllegalArgumentException e) {
            // Handle the case where the cleaned ID is not a valid Material
            Main.logger.warn("Invalid material ID: " + namespaceId);
            Main.logger.warn("Defaulting to Air");
            return Material.AIR; // Return a default or placeholder material if the conversion fails
        }
    }


    /**
     * Converts a {@link Material} object to its corresponding string representation in
     * namespace ID format ("minecraft:item_id").
     *
     * <p>If the provided {@link Material} is null, the method defaults to returning
     * "minecraft:air".</p>
     *
     * @param material The {@link Material} object to convert to a namespace ID string.
     * @return The namespace ID string of the material (e.g., "minecraft:diamond_sword"), or
     *         "minecraft:air" if the material is null.
     *
     * @author Wi1helm
     */
    public static String convertMaterialToNamespaceId(Material material) {
        if (material == null) {
            return "minecraft:air"; // Return a default or placeholder namespace if the material is null
        }

        // Return the namespace ID in the proper format
        return material.namespace().toString();
    }


}
