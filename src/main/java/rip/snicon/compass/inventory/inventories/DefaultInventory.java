package rip.snicon.compass.inventory.inventories;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import rip.snicon.compass.inventory.TemplateInventory;
import rip.snicon.compass.inventory.TemplateItem;
import rip.snicon.compass.inventory.item.items.containers.BackgroundItem;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.utils.TextUtils;

import java.util.List;

public class DefaultInventory extends TemplateInventory {

    public DefaultInventory() {
        super(TextUtils.convertStringToComponent("Default Inventory"), InventoryType.CHEST_4_ROW);
    }

    @Override
    protected void initialize() {
        // Define items directly in the inventory setup
        setItem(3, new TemplateItem(Material.TOTEM_OF_UNDYING) {

            @Override
            protected void initialize() {
                setName(TextUtils.convertStringToComponent("<gold>Profession</gold>"));
                setLore(TextUtils.convertStringToComponent(List.of("Track your profession progress and XP.")));
            }

            @Override
            protected void personalize(Player player) {

            }

            @Override
            public void onUse(Player player) {
                MysteryPlayer mysteryPlayer = (MysteryPlayer) player;

            }

            @Override
            public void onDrop(Player player) {
                // Optional: Add logic for dropping this item
            }
        });

        setItem(4, new TemplateItem(Material.COMPASS) {

            @Override
            protected void initialize() {
                setName(TextUtils.convertStringToComponent("<light_purple>Minigame Selector</light_purple>"));
                setLore(TextUtils.convertStringToComponent(List.of("Select a minigame to play!")));
            }

            @Override
            protected void personalize(Player player) {

            }

            @Override
            public void onUse(Player player) {
                MysteryPlayer mysteryPlayer = (MysteryPlayer) player;
            }

            @Override
            public void onDrop(Player player) {
                // Optional: Add logic for dropping this item
            }
        });

        setItem(5, new TemplateItem(Material.PLAYER_HEAD) {

            @Override
            protected void initialize() {
                setName(TextUtils.convertStringToComponent("<gold>Profile</gold>"));
                setLore(TextUtils.convertStringToComponent(List.of("View your profile and stats.")));
            }

            @Override
            protected void personalize(Player player) {
                setSkin(player.getSkin());
            }

            @Override
            public void onUse(Player player) {
                MysteryPlayer mysteryPlayer = (MysteryPlayer) player;
                TemplateInventory inventory = new ProfileContainer();
                inventory.fillInventory(new BackgroundItem());
                mysteryPlayer.openInventory(inventory.constructInventory(player));
            }

            @Override
            public void onDrop(Player player) {}
        });

        setItem(8, new TemplateItem(Material.NETHER_STAR) {
            {
                setName(TextUtils.convertStringToComponent("<light_purple>Universe Selector</light_purple>"));
                setLore(TextUtils.convertStringToComponent(List.of("Select a universe to travel to.")));
            }

            @Override
            protected void initialize() {

            }

            @Override
            protected void personalize(Player player) {

            }

            @Override
            public void onUse(Player player) {
                System.out.println("Opening Universe Selector GUI...");
            }

            @Override
            public void onDrop(Player player) {}
        });
    }

    @Override
    protected void personalize(Player player) {}
}
