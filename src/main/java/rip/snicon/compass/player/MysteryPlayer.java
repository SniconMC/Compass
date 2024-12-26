package rip.snicon.compass.player;


import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.Main;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.handler.*;
import rip.snicon.compass.player.settings.PlayerSetting;
import rip.snicon.compass.utils.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MysteryPlayer extends Player {

    private static final Map<UUID, MysteryPlayer> playerCache = new HashMap<>();


    private final MysteryDataHandler dataHandler;
    private final MysteryInventoryHandler inventoryHandler;
    private final MysterySettingsHandler settingsHandler;
    private final MysteryRegionHandler regionHandler;
    private final MysteryStatisticHandler statisticHandler;
    private final MysteryCosmeticHandler cosmeticHandler;

    public MysteryPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);

        playerCache.put(uuid, this);
        this.dataHandler = new MysteryDataHandler(uuid);
        this.inventoryHandler = new MysteryInventoryHandler(uuid);
        this.settingsHandler = new MysterySettingsHandler(uuid);
        this.regionHandler = new MysteryRegionHandler(uuid);
        this.statisticHandler = new MysteryStatisticHandler(uuid);
        this.cosmeticHandler = new MysteryCosmeticHandler(uuid);


    }

    public static MysteryPlayer getPlayer(UUID uuid) {
        return playerCache.get(uuid);
    }

    public static void clearCache(UUID uuid) {
        playerCache.remove(uuid);
    }

    public MysteryDataHandler getDataHandler() {
        return dataHandler;
    }

    public MysteryInventoryHandler getInventoryHandler() {
        return inventoryHandler;
    }

    public MysterySettingsHandler getSettingsHandler() {
        return settingsHandler;
    }

    public MysteryRegionHandler getRegionHandler() {
        return regionHandler;
    }

    public MysteryStatisticHandler getStatisticHandler() {
        return statisticHandler;
    }

    public MysteryCosmeticHandler getCosmeticHandler() {
        return cosmeticHandler;
    }

    public String getProfessionDisplay() {
        boolean showIcon = settingsHandler.getSetting(PlayerSetting.SHOW_ICON);

        if (showIcon) {
            return dataHandler.getProfession().getIcon().icon(); // Show icon
        } else {
            return TextUtils.capitalizeFirstLetter(dataHandler.getProfession().name()); // Show name
        }
    }

    public String getFullDisplayName() {
        String icon = getProfessionDisplay();
        String rankName = dataHandler.getRank().name();
        String rankColor = dataHandler.getRank().getColor();

        // MiniMessage format
        return String.format(
                "<dark_gray>[<gray>%s</gray>] [<%s>%s</%s>]</dark_gray> <white>%s</white>",
                icon, rankColor, TextUtils.capitalizeFirstLetter(rankName), rankColor, getUsername()
        );
    }

    public void updateRegion() {
        regionHandler.updateRegion();
    }

    public void unloadPlayerInventory() {
        inventoryHandler.saveInventory(this.getInventory());
    }

    public void loadPlayerInventory() {
        inventoryHandler.loadInventory(this.getInventory());
    }

    public void addItem(MysteryItemType item) {
        inventoryHandler.addItem(this.getInventory(), item);
    }

}
