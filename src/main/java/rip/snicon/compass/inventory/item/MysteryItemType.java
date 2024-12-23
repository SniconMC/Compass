package rip.snicon.compass.inventory.item;

import rip.snicon.compass.inventory.item.items.containers.*;
import rip.snicon.compass.inventory.item.items.containers.profile.*;
import rip.snicon.compass.player.MysteryPlayer;

public enum MysteryItemType {
    MINIGAME_SELECTOR(new MinigameSelector()),
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
    EXAMPLE_ITEM(new ExampleItem());

    private final MysteryItem item;

    MysteryItemType(MysteryItem item) {
        this.item = item;
        this.item.setItemKey(this.name()); // Automatically set the type
    }

    public MysteryItem getItem(MysteryPlayer player) {
        item.populateForPlayer(player);
        return item;
    }
}