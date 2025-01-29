package rip.snicon.compass.inventory.cosmetics;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import nub.wi1helm.template.inventory.items.BackgroundItem;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.cosmetics.PlayerPerk;
import rip.snicon.compass.utils.TextUtils;

import java.util.List;

public class Perks extends TemplateInventory {
    private static final int FLY_PERK_COST = 50; // Cost in emeralds
    private static final int DOUBLE_JUMP_PERK_COST = 75;

    public Perks() {
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
                if (mysteryPlayer.getCosmeticHandler().hasCosmetic(PlayerPerk.FLY)) {
                    setLore(TextUtils.convertStringToComponent(List.of("Unlocked!")));
                } else {
                    setLore(TextUtils.convertStringToComponent(List.of("<red>Locked</red>", "<green> Cost: " + FLY_PERK_COST + " emeralds")));
                }
            }

            @Override
            public void onUse(TemplateInventoryEvent event) {
                if (mysteryPlayer.getCosmeticHandler().hasCosmetic(PlayerPerk.FLY)) {
                    mysteryPlayer.sendMessage("You already own this perk!");
                } else if (mysteryPlayer.getDataHandler().getEmeralds() >= FLY_PERK_COST) {
                    mysteryPlayer.getDataHandler().updateEmeralds(-FLY_PERK_COST, false);
                    mysteryPlayer.getCosmeticHandler().addCosmetic(PlayerPerk.FLY);
                    mysteryPlayer.sendMessage("You have unlocked the Fly Perk!");
                    event.getPlayer().closeInventory();
                } else {
                    mysteryPlayer.sendMessage("You do not have enough emeralds to buy this perk!");
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
                if (mysteryPlayer.getCosmeticHandler().hasCosmetic(PlayerPerk.JUMPBOOST)) {
                    setLore(TextUtils.convertStringToComponent(List.of("Unlocked!")));
                } else {
                    setLore(TextUtils.convertStringToComponent(List.of("<red>Locked</red>", "<green> Cost: " + DOUBLE_JUMP_PERK_COST + " emeralds")));
                }
            }

            @Override
            public void onUse(TemplateInventoryEvent event) {
                if (mysteryPlayer.getCosmeticHandler().hasCosmetic(PlayerPerk.JUMPBOOST)) {
                    mysteryPlayer.sendMessage("You already own this perk!");
                } else if (mysteryPlayer.getDataHandler().getEmeralds() >= DOUBLE_JUMP_PERK_COST) {
                    mysteryPlayer.getDataHandler().updateEmeralds(-DOUBLE_JUMP_PERK_COST, false);
                    mysteryPlayer.getCosmeticHandler().addCosmetic(PlayerPerk.JUMPBOOST);
                    mysteryPlayer.sendMessage("You have unlocked the Double Jump Perk!");
                    event.getPlayer().closeInventory();
                } else {
                    mysteryPlayer.sendMessage("You do not have enough emeralds to buy this perk!");
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
