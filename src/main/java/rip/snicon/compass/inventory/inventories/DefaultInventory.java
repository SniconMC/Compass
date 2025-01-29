package rip.snicon.compass.inventory.inventories;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import nub.wi1helm.template.inventory.TemplateInventory;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import nub.wi1helm.template.inventory.items.BackgroundItem;
import rip.snicon.compass.inventory.profession.ProfessionContainer;
import rip.snicon.compass.inventory.profile.ProfileContainer;
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
            public void onUse(TemplateInventoryEvent event) {
                TemplateInventory inventory = new ProfessionContainer();
                inventory.fillInventory(new BackgroundItem());
                event.getPlayer().openInventory(inventory.constructInventory(event.getPlayer()));
            }

            @Override
            public void onDrop(TemplateInventoryEvent event) {

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
            public void onUse(TemplateInventoryEvent event) {

            }

            @Override
            public void onDrop(TemplateInventoryEvent event) {

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
            public void onUse(TemplateInventoryEvent event) {
                MysteryPlayer player = (MysteryPlayer) event.getPlayer();
                TemplateInventory inventory = new ProfileContainer();
                inventory.fillInventory(new BackgroundItem());
                player.openInventory(inventory.constructInventory(player));
            }

            @Override
            public void onDrop(TemplateInventoryEvent event) {}
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
            public void onUse(TemplateInventoryEvent event) {

            }

            @Override
            public void onDrop(TemplateInventoryEvent event) {

            }


        });
    }

    @Override
    protected void personalize(Player player) {}
}
