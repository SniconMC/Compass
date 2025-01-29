package rip.snicon.compass.inventory.profile.items;

import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.Material;
import nub.wi1helm.template.inventory.TemplateInventoryEvent;
import nub.wi1helm.template.inventory.TemplateItem;
import rip.snicon.compass.utils.TextUtils;

import java.util.List;

public class GuidesPhoneItem extends TemplateItem {

    public GuidesPhoneItem() {
        super(Material.PLAYER_HEAD);


    }

    @Override
    protected void initialize() {
        setName(TextUtils.convertStringToComponent("<gold>Guide's Phone</gold>"));
        setLore(TextUtils.convertStringToComponent(List.of(
                "<gray>Browse helpful information and tips.</gray>",
                "",
                "<yellow>Click to call the Guide!</yellow>")));
        setSkin(new PlayerSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTQ3YmJkOThmNzM2NmY5MWRkODRiOGIyNTY1NDYwNDkyYjNjMzBjNjUwOTk5YjQ1MjYxMGQ5ZTkxZWQxMTI2ZiJ9fX0=",""));
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

}
