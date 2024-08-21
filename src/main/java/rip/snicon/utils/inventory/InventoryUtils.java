package rip.snicon.utils.inventory;

import net.minestom.server.inventory.InventoryType;

public class InventoryUtils {

    public static InventoryType getInventoryType(int rows) {

        return switch (rows) {
            case 1 -> InventoryType.CHEST_1_ROW;
            case 2 -> InventoryType.CHEST_2_ROW;
            case 3 -> InventoryType.CHEST_3_ROW;
            case 4 -> InventoryType.CHEST_4_ROW;
            case 5 -> InventoryType.CHEST_5_ROW;
            case 6 -> InventoryType.CHEST_6_ROW;
            default -> InventoryType.WINDOW_3X3;
        };
    }

}
