package rip.snicon.compass.inventory.item.items.containers.profile;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.item.MysteryItem;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.inventory.MysteryInventoryType;

import java.util.List;

public class GuidesPhoneItem extends MysteryItem {

    public GuidesPhoneItem() {
        super(Material.PLAYER_HEAD, "<gold>Guide's Phone</gold>", List.of(
                "<gray>Browse helpful information and tips.</gray>",
                "",
                "<yellow>Click to call the Guide!</yellow>"

        ), 1, 1, MysteryItemOrigin.CONTAINER);

        setShowTooltip(true);
        setGlint(false);
        setSkin(new PlayerSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTQ3YmJkOThmNzM2NmY5MWRkODRiOGIyNTY1NDYwNDkyYjNjMzBjNjUwOTk5YjQ1MjYxMGQ5ZTkxZWQxMTI2ZiJ9fX0=",""));
    }

    @Override
    public void populateForPlayer(MysteryPlayer player) {
        // Add any player-specific customizations, if needed
    }

    @Override
    public void onUse(MysteryPlayer player) {
        // Open the Guide inventory/menu

    }
}
