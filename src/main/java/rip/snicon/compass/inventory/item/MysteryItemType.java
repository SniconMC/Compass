package rip.snicon.compass.inventory.item;

import net.minestom.server.inventory.Inventory;
import rip.snicon.compass.inventory.item.items.containers.*;
import rip.snicon.compass.inventory.item.items.containers.minigame.BlockhuntItem;
import rip.snicon.compass.inventory.item.items.containers.minigame.ParkourItem;
import rip.snicon.compass.inventory.item.items.containers.profile.*;
import rip.snicon.compass.inventory.item.items.containers.settings.ShowDecimalItem;
import rip.snicon.compass.inventory.item.items.containers.settings.ShowIconItem;
import rip.snicon.compass.player.MysteryPlayer;

public enum MysteryItemType {
    MINIGAME_SELECTOR(new MinigameSelector()),
    UNIVERSE_SELECTOR(new UniverseSelector()),
    PROFESSION_VIEWER(new ProfessionViewer()),
    PROFILE_VIEWER(new ProfileViewer()),
    ACHIEVEMENT_ITEM(new AchievementItem()),
    STATISTICS_ITEM(new StatisticsItem()),
    COSMETICS_ITEM(new CosmeticsItem()),
    SETTINGS_ITEM(new SettingsItem()),
    PROFESSION_ITEM(new ProfessionItem()),
    GUIDES_PHONE_ITEM(new GuidesPhoneItem()),
    CLOSE_ITEM(new CloseItem()),
    BACKGROUND_ITEM(new BackgroundItem()),
    EXAMPLE_ITEM(new ExampleItem()),
    SHOWICON_ITEM(new ShowIconItem()),
    SHOWDECIMAL_ITEM(new ShowDecimalItem()),
    BLOCKHUNT_ITEM(new BlockhuntItem()),
    PARKOUR_ITEM(new ParkourItem());

    private final MysteryItem item;

    MysteryItemType(MysteryItem item) {
        this.item = item;
        this.item.setItemIdentifier(this.name()); // Automatically set the type
    }

    public MysteryItem getItem(MysteryPlayer player) {
        item.populateForPlayer(player);
        return item;
    }
    public MysteryItem getItem(MysteryPlayer player, Inventory hostInventory, int hostSlot) {
        item.populateForPlayer(player);
        item.setHosts(hostInventory, hostSlot);
        return item;
    }

}