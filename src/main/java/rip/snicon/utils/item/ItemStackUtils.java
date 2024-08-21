package rip.snicon.utils.item;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.RGBLike;
import net.minestom.server.entity.EquipmentSlotGroup;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.attribute.AttributeModifier;
import net.minestom.server.entity.attribute.AttributeOperation;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.AttributeList;
import net.minestom.server.item.component.CustomData;
import net.minestom.server.item.component.DyedItemColor;
import net.minestom.server.item.component.HeadProfile;
import rip.snicon.Main;
import rip.snicon.modules.container.json.InventorySettings;
import rip.snicon.modules.container.json.Item;
import rip.snicon.utils.ColorUtils;
import rip.snicon.utils.SkullUtils;
import rip.snicon.utils.TextUtils;
import rip.snicon.utils.json.Text;

import java.util.ArrayList;
import java.util.List;

import static rip.snicon.utils.MaterialUtils.convertToMaterial;

public class ItemStackUtils {

    public static ItemStack createItemStack(Item item, Player player, InventorySettings settings) {
        Material material = convertToMaterial(item.getId(), player);
        if (material == null) {
            Main.logger.error("Material for this item is invalid: " + item.getId());
            return ItemStack.of(Material.AIR, 1);
        }

        Component itemName = TextUtils.convertToComponentWithPlaceholders(item.getDisplay().getName(), player);
        List<Component> itemLore = new ArrayList<>();
        for (List<Text> row : item.getDisplay().getLore()){
            itemLore.add(TextUtils.convertToComponentWithPlaceholders(row,player));
        }
        int itemCount = item.getCount().getCurrent();
        int itemMaxCount = item.getCount().getMax();

        // Build the CompoundBinaryTag with null checks
        var customDataBuilder = CompoundBinaryTag.builder();
        // Add data conditionally
        String page = item.getData().getPage();
        if (page != null) {
            customDataBuilder.putString("redirect", page);
        }

        String function = item.getData().getFunction();
        if (function != null) {
            customDataBuilder.putString("function", function);
        }

        String containerId = item.getContainerId();
        if (containerId != null) {
            customDataBuilder.putString("container_id", containerId);
        }
        var customData = customDataBuilder.build();

        var itemInASlot = ItemStack.builder(material).set(ItemComponent.ITEM_NAME, itemName).set(ItemComponent.LORE, itemLore).amount(itemCount).maxStackSize(itemMaxCount).set(ItemComponent.CUSTOM_DATA, new CustomData(customData));

        if (material == Material.PLAYER_HEAD) {
            itemInASlot.set(ItemComponent.PROFILE, new HeadProfile(SkullUtils.getSkin(player, item.getSkin().getPlayer(), "", item.getSkin().getTexture(), "")));
        }
        String color = item.getDisplay().getDye_color();
        if (!color.isEmpty()) {
            itemInASlot.set(ItemComponent.DYED_COLOR, new DyedItemColor(ColorUtils.StringToRgb(color), false));
        }
        itemInASlot.set(ItemComponent.ENCHANTMENT_GLINT_OVERRIDE, item.getDisplay().isGlint());
        if (!item.getDisplay().isShow_tooltip()) {
            itemInASlot.set(ItemComponent.HIDE_TOOLTIP);
        }
        itemInASlot.set(ItemComponent.ATTRIBUTE_MODIFIERS, new AttributeList(new AttributeList.Modifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier("dummy", 0, AttributeOperation.ADD_VALUE), EquipmentSlotGroup.ANY), false));
        return itemInASlot.build();
    }
}
