package rip.snicon.compass.inventory.profile.cosmetics;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import nub.wi1helm.template.inventory.items.BackgroundItem;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.data.cosmetics.PlayerPerk;
import rip.snicon.compass.utils.TextUtils;

import java.util.List;

public class ProfilePerks extends TemplateInventory {

    public ProfilePerks() {
        super(TextUtils.convertStringToComponent("Perks"), InventoryType.CHEST_5_ROW);
    }

    @Override
    protected void initialize() {
        fillInventory(new BackgroundItem());
    }

    @Override
    protected void personalize(Player player) {
        final MysteryPlayer mysteryPlayer = (MysteryPlayer) player;

        // Fly Perk
        setItem(12, new TemplateItem(Material.FEATHER) {
            @Override
            protected void initialize() {
                setName(TextUtils.convertStringToComponent("Fly Perk"));
            }

            @Override
            protected void personalize(Player player) {
                if (mysteryPlayer.getCosmeticHandler().isEnabled(PlayerPerk.FLY)) {
                    setLore(TextUtils.convertStringToComponent(List.of("Enabled!")));
                } else if (mysteryPlayer.getCosmeticHandler().hasCosmetic(PlayerPerk.FLY)) {
                    setLore(TextUtils.convertStringToComponent(List.of("Disabled", "Click to enable")));
                } else {
                    setLore(TextUtils.convertStringToComponent(List.of("<red>Locked</red>", "You must own this perk first.")));
                }
            }

            @Override
            public void onUse(TemplateInventoryEvent event) {
                if (!mysteryPlayer.getCosmeticHandler().hasCosmetic(PlayerPerk.FLY)) {
                    mysteryPlayer.sendMessage("You do not own this perk!");
                } else {
                    if (mysteryPlayer.getCosmeticHandler().isEnabled(PlayerPerk.FLY)) {
                        mysteryPlayer.getCosmeticHandler().disableCosmetic(PlayerPerk.FLY);
                        mysteryPlayer.sendMessage("Fly Perk has been disabled!");
                    } else {
                        mysteryPlayer.getCosmeticHandler().enableCosmetic(PlayerPerk.FLY);
                        mysteryPlayer.sendMessage("Fly Perk has been enabled!");
                    }
                    event.getPlayer().closeInventory();
                }
            }

            @Override
            public void onDrop(TemplateInventoryEvent event) {
                event.setCancelled(true); // Prevent dropping the item
            }
        });

        // Double Jump Perk
        setItem(14, new TemplateItem(Material.RABBIT_FOOT) {
            @Override
            protected void initialize() {
                setName(TextUtils.convertStringToComponent("Double Jump Perk"));
            }

            @Override
            protected void personalize(Player player) {
                if (mysteryPlayer.getCosmeticHandler().isEnabled(PlayerPerk.JUMPBOOST)) {
                    setLore(TextUtils.convertStringToComponent(List.of("Enabled!")));
                } else if (mysteryPlayer.getCosmeticHandler().hasCosmetic(PlayerPerk.JUMPBOOST)) {
                    setLore(TextUtils.convertStringToComponent(List.of("Disabled", "Click to enable")));
                } else {
                    setLore(TextUtils.convertStringToComponent(List.of("<red>Locked</red>", "You must own this perk first.")));
                }
            }

            @Override
            public void onUse(TemplateInventoryEvent event) {
                if (!mysteryPlayer.getCosmeticHandler().hasCosmetic(PlayerPerk.JUMPBOOST)) {
                    mysteryPlayer.sendMessage("You do not own this perk!");
                } else {
                    if (mysteryPlayer.getCosmeticHandler().isEnabled(PlayerPerk.JUMPBOOST)) {
                        mysteryPlayer.getCosmeticHandler().disableCosmetic(PlayerPerk.JUMPBOOST);
                        mysteryPlayer.sendMessage("Double Jump Perk has been disabled!");
                    } else {
                        mysteryPlayer.getCosmeticHandler().enableCosmetic(PlayerPerk.JUMPBOOST);
                        mysteryPlayer.sendMessage("Double Jump Perk has been enabled!");
                    }
                    event.getPlayer().closeInventory();
                }
            }

            @Override
            public void onDrop(TemplateInventoryEvent event) {
                event.setCancelled(true); // Prevent dropping the item
            }
        });
    }
}
