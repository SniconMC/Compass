package rip.snicon.compass.player;


import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.item.component.FireworkExplosion;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.other.LevelUp;
import rip.snicon.compass.player.handler.*;
import rip.snicon.compass.player.settings.PlayerSetting;
import rip.snicon.compass.utils.FireworkUtility;
import rip.snicon.compass.utils.TabUtils;
import rip.snicon.compass.utils.TextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MysteryPlayer extends Player {

    private static final Map<UUID, MysteryPlayer> playerCache = new HashMap<>();


    private final MysteryDataHandler dataHandler;
    private final MysterySettingsHandler settingsHandler;
    private final MysteryRegionHandler regionHandler;
    private final MysteryStatisticHandler statisticHandler;
    private final MysteryCosmeticHandler cosmeticHandler;
    private final MysteryBundleHandler bundleHandler;

    public MysteryPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);

        playerCache.put(uuid, this);
        this.dataHandler = new MysteryDataHandler(uuid);
        this.settingsHandler = new MysterySettingsHandler(uuid);
        this.regionHandler = new MysteryRegionHandler(uuid);
        this.statisticHandler = new MysteryStatisticHandler(uuid);
        this.cosmeticHandler = new MysteryCosmeticHandler(uuid);
        this.bundleHandler = new MysteryBundleHandler(uuid);

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

    public MysteryBundleHandler getBundleHandler(){
        return bundleHandler;
    }

    public String getProfessionDisplay() {
        boolean showIcon = settingsHandler.getSetting(PlayerSetting.SHOW_ICON);

        if (showIcon) {
            return dataHandler.getProfession().getIconData().icon(); // Show icon
        } else {
            return TextUtils.capitalizeFirstLetter(dataHandler.getProfession().name()); // Show name
        }
    }

    public String getRankDisplayName() {
        String rankName = dataHandler.getRank().name();
        String rankColor = dataHandler.getRank().getColor();

        // MiniMessage format
        return String.format(
                "<dark_gray>[<%s>%s</%s>]</dark_gray> <white>%s</white>",
                rankColor, TextUtils.capitalizeFirstLetter(rankName), rankColor, getUsername()
        );
    }

    public void updateDisplayName() {
        // Fetch the setting for this player's profession display

        // Loop through all online players and update their tab entries for this player
        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(receiver -> {
            if (receiver instanceof MysteryPlayer mysteryReceiver) {

                boolean showIcon = settingsHandler.getSetting(PlayerSetting.SHOW_ICON);

                String professionDisplay = showIcon ? mysteryReceiver.getDataHandler().getProfession().getIconData().icon() : TextUtils.capitalizeFirstLetter(mysteryReceiver.getDataHandler().getProfession().name());;


                // Format the display name for the tab entry
                String formattedDisplayName = String.format(
                        "<dark_gray>[<gray>%s</gray>]</dark_gray> %s",
                        professionDisplay,
                        mysteryReceiver.getRankDisplayName()
                );

                // Prepare player properties (e.g., skin)
                List<PlayerInfoUpdatePacket.Property> properties = mysteryReceiver.getSkin() != null
                        ? List.of(new PlayerInfoUpdatePacket.Property("textures", mysteryReceiver.getSkin().textures(), mysteryReceiver.getSkin().signature()))
                        : List.of();

                // Create the PlayerInfoUpdatePacket entry
                PlayerInfoUpdatePacket.Entry entry = new PlayerInfoUpdatePacket.Entry(
                        mysteryReceiver.getUuid(),
                        mysteryReceiver.getUsername(),
                        properties,
                        true, // Visible in tablist
                        0, // Ping
                        GameMode.SURVIVAL,
                        TextUtils.convertStringToComponent(formattedDisplayName),
                        null
                );

                // Create the packet to update the display name
                PlayerInfoUpdatePacket packet = new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, entry);

                // Send the packet to the receiver
                this.sendPacket(packet);
            }
        });
    }


    public void updateRegion() {
        regionHandler.updateRegion();
    }

    public void onLevelUp() {
        TabUtils.setPlayerTab(this);
        FireworkUtility.instantFirework(FireworkExplosion.Shape.SMALL_BALL, List.of(
                TextColor.color(255, 0, 0),   // Red
                TextColor.color(255, 165, 0), // Orange
                TextColor.color(255, 255, 0), // Yellow
                TextColor.color(0, 255, 0),   // Green
                TextColor.color(0, 0, 255),   // Blue
                TextColor.color(128, 0, 128)  // Purple
        ), this);
        LevelUp.trigger(this, 0.5);
    }

}
