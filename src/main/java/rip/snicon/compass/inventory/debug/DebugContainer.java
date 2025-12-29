package rip.snicon.compass.inventory.debug;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import nub.wi1helm.smoxy.SMoxy;
import nub.wi1helm.smoxy.SMoxyService;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import nub.wi1helm.template.inventory.items.BackgroundItem;
import nub.wi1helm.template.inventory.items.CloseButton;
import rip.snicon.compass.Main;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.data.bundle.MysteryBundleTypes;
import rip.snicon.compass.utils.TextUtils;

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



        setItem(10, new TemplateItem(Material.EMERALD) {
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

        setItem(12, new TemplateItem(Material.GOLD_INGOT) {
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

        setItem(14, new TemplateItem(Material.GRASS_BLOCK) {
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

                if (player.getBundleHandler().hasBundle(randomBundleType)) {
                    player.getBundleHandler().addBundleAmount(randomBundleType,1);
                } else {
                    player.getBundleHandler().addBundle(randomBundleType, 1,true);
                }

                player.sendMessage(TextUtils.convertStringToComponent("<green>You got a " + randomBundleType.getDisplayName() + "</green>"));
            }

            @Override
            public void onDrop(TemplateInventoryEvent event) {

            }
        });

        setItem(16, new TemplateItem(Material.TNT_MINECART) {
            @Override
            protected void initialize() {

            }

            @Override
            protected void personalize(Player player) {

                if (Main.joinable) {
                    setName(TextUtils.convertStringToComponent("<red>Disable Server Join</red>"));
                    setMaterial(Material.TNT_MINECART);
                } else {
                    setName(TextUtils.convertStringToComponent("<green>Enable Server Join</green>"));
                    setMaterial(Material.FURNACE_MINECART);
                }
            }

            @Override
            public void onUse(TemplateInventoryEvent event) {
                final MysteryPlayer player = (MysteryPlayer) event.getPlayer();

                if (Main.joinable) {
                    SMoxyService.sendRemoveJoinablePluginMessage(player, SMoxy.serverName);
                    Main.joinable = false;

                } else {

                    SMoxyService.sendAddJoinablePluginMessage(player,SMoxy.serverName);
                    Main.joinable = true;

                }

                if (getHostInventory() != null) {
                    this.getHostInventory().setItemStack(getHostSlot(), constructItemStack(player));
                }

            }

            @Override
            public void onDrop(TemplateInventoryEvent event) {

            }
        });
    }
}
