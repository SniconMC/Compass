package rip.snicon.utils.inventory;

import net.minestom.server.inventory.InventoryType;

/**
 * Utility class for working with inventories in Minestom.
 *
 * <p>This class provides helper methods for inventory management, such as determining the appropriate
 * {@link InventoryType} based on specified parameters like the number of rows. It aims to simplify
 * interactions with Minestom's inventory API by abstracting common logic into reusable methods.</p>
 *
 * @see InventoryType
 * @see net.minestom.server.inventory.Inventory
 *
 * @author Wi1helm
 */
public class InventoryUtils {

    /**
     * Determines the appropriate {@link InventoryType} based on the specified number of rows.
     *
     * <p>This method is primarily used to create chest inventories with varying row counts. If the number
     * of rows provided does not match any of the predefined chest sizes (1 to 6), it defaults to
     * {@link InventoryType#BEACON}.</p>
     *
     * @param rows the number of rows in the desired inventory, ranging from 1 to 6 for chest inventories.
     * @return The corresponding {@link InventoryType} for the given row count, or {@link InventoryType#BEACON}
     *         if the row count is outside the valid range (1-6).
     *
     * @see InventoryType#CHEST_1_ROW
     * @see InventoryType#CHEST_2_ROW
     * @see InventoryType#CHEST_3_ROW
     * @see InventoryType#CHEST_4_ROW
     * @see InventoryType#CHEST_5_ROW
     * @see InventoryType#CHEST_6_ROW
     * @see InventoryType#BEACON
     *
     * @author Wi1helm
     */
    public static InventoryType getInventoryType(int rows) {

        return switch (rows) {
            case 1 -> InventoryType.CHEST_1_ROW;
            case 2 -> InventoryType.CHEST_2_ROW;
            case 3 -> InventoryType.CHEST_3_ROW;
            case 4 -> InventoryType.CHEST_4_ROW;
            case 5 -> InventoryType.CHEST_5_ROW;
            case 6 -> InventoryType.CHEST_6_ROW;
            default -> InventoryType.BEACON;
        };
    }

}
