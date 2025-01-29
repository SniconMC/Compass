package rip.snicon.compass.inventory.debug;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import nub.wi1helm.template.inventory.items.BackgroundItem;
import nub.wi1helm.template.inventory.items.CloseButton;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.bundle.MysteryBundleTypes;
import rip.snicon.compass.utils.TextUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DebugContainer extends TemplateInventory {
    public DebugContainer() {
        super(TextUtils.convertStringToComponent("Debug Menu"), InventoryType.CHEST_3_ROW);
    }

    @Override
    protected void initialize() {
        setItem(22, new CloseButton());
        fillInventory(new BackgroundItem());
    }

    @Override
    protected void personalize(Player player) {
        final MysteryPlayer p = (MysteryPlayer) player;



        setItem(11, new TemplateItem(Material.EMERALD) {
            @Override
            protected void initialize() {
                setName(TextUtils.convertStringToComponent("<green>Give 100 Emeralds</green>"));
            }

            @Override
            protected void personalize(Player player) {
                double emeralds = p.getDataHandler().getEmeralds();
                List<String> lore = List.of("A debug button that gives emeralds", " ", "<white>Emeralds:</white> <green>" + emeralds + "</green>");

                setLore(TextUtils.convertStringToComponent(lore));
            }

            @Override
            public void onUse(TemplateInventoryEvent event) {
                final MysteryPlayer player = (MysteryPlayer) event.getPlayer();
                player.getDataHandler().updateEmeralds(100,true);
                personalize(player);
                if (getHostInventory() != null) {
                    this.getHostInventory().setItemStack(getHostSlot(), constructItemStack(player));
                }
            }

            @Override
            public void onDrop(TemplateInventoryEvent event) {

            }
        });

        setItem(13, new TemplateItem(Material.GOLD_INGOT) {
            @Override
            protected void initialize() {
                setName(TextUtils.convertStringToComponent("<green>Give 100 Profession Xp</green>"));
            }

            @Override
            protected void personalize(Player player) {
                double xp = p.getDataHandler().getProfessionXp();
                List<String> lore = List.of("A debug button that gives profession xp", " ", "<white>Profession Xp:</white> <green>" + xp + "</green>");

                setLore(TextUtils.convertStringToComponent(lore));
            }

            @Override
            public void onUse(TemplateInventoryEvent event) {
                final MysteryPlayer player = (MysteryPlayer) event.getPlayer();
                player.getDataHandler().updateProfessionXp(100,true);
                personalize(player);
                if (getHostInventory() != null) {
                    this.getHostInventory().setItemStack(getHostSlot(), constructItemStack(player));
                }
            }

            @Override
            public void onDrop(TemplateInventoryEvent event) {

            }
        });

        setItem(15, new TemplateItem(Material.GRASS_BLOCK) {
            @Override
            protected void initialize() {
                setName(TextUtils.convertStringToComponent("<green>Give Random Bundle</green>"));
            }

            @Override
            protected void personalize(Player player) {
            }

            @Override
            public void onUse(TemplateInventoryEvent event) {
                final MysteryPlayer player = (MysteryPlayer) event.getPlayer();
                // Get a random bundle type
                int randomIndex = ThreadLocalRandom.current().nextInt(MysteryBundleTypes.values().length);
                MysteryBundleTypes randomBundleType = MysteryBundleTypes.values()[randomIndex];

                player.getBundleHandler().addBundle(randomBundleType);
                player.sendMessage("I gave you 1 random bundle. bundle was " + randomBundleType.getDisplayName());
            }

            @Override
            public void onDrop(TemplateInventoryEvent event) {

            }
        });
    }
}
