package rip.snicon.compass.inventory.blockhunt;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import nub.wi1helm.template.inventory.items.BackgroundItem;
import nub.wi1helm.template.inventory.items.CloseButton;
import rip.snicon.compass.inventory.blockhunt.bundels.BundleViewerContainer;
import rip.snicon.compass.inventory.blockhunt.items.BlockhuntPlayItem;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.data.MysteryToggles;
import rip.snicon.compass.utils.TextUtils;

import java.util.List;

public class BlockhuntMenu extends TemplateInventory {
    public BlockhuntMenu() {
        super(TextUtils.convertStringToComponent("Blockhunt Menu"), InventoryType.CHEST_5_ROW);
    }

    @Override
    protected void initialize() {
        fillInventory(new BackgroundItem());
        setItem(40,new CloseButton());
        setItem(13, new BlockhuntPlayItem());
        setItem(20, new TemplateItem(Material.SHULKER_SHELL) {
            @Override
            protected void initialize() {
                setName(TextUtils.convertStringToComponent("You'r Bundels"));
            }

            @Override
            protected void personalize(Player player) {

            }

            @Override
            public void onUse(TemplateInventoryEvent templateInventoryEvent) {
                templateInventoryEvent.getPlayer().openInventory(new BundleViewerContainer().constructInventory(templateInventoryEvent.getPlayer()));
            }

            @Override
            public void onDrop(TemplateInventoryEvent templateInventoryEvent) {

            }
        });
        setItem(24, new TemplateItem(Material.MINECART) {
            @Override
            protected void initialize() {

            }

            @Override
            protected void personalize(Player player) {
                MysteryPlayer p = (MysteryPlayer) player;
                boolean bool = p.getToggleHandler().hasToggle(MysteryToggles.TRAVEL_TO_BUNDLE_BOT);

                if (!bool) {
                    setName(TextUtils.convertStringToComponent("<red>Travel To Bundle Bot</red>"));
                    setLore(TextUtils.convertStringToComponent(List.of("<red>LOCKED</red>")));
                } else {
                    setName(TextUtils.convertStringToComponent("<green>Travel To Bundle Bot</green>"));
                    setLore(TextUtils.convertStringToComponent(List.of("<yellow>Click to Travel</yellow>")));
                }
            }

            @Override
            public void onUse(TemplateInventoryEvent templateInventoryEvent) {
                MysteryPlayer player = (MysteryPlayer) templateInventoryEvent.getPlayer();
                boolean bool = player.getToggleHandler().hasToggle(MysteryToggles.TRAVEL_TO_BUNDLE_BOT);

                if (bool) {
                    player.teleport(new Pos(9.5,6,102.5,-90,0));
                    player.sendMessage(TextUtils.convertStringToComponent("<green>Traveled To Bundle Bot<green>"));
                } else {
                    player.sendMessage(TextUtils.convertStringToComponent("<red>Not Unlocked<red>"));
                    player.closeInventory();
                }
            }

            @Override
            public void onDrop(TemplateInventoryEvent templateInventoryEvent) {

            }
        });

    }

    @Override
    protected void personalize(Player player) {

    }
}
