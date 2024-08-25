package rip.snicon.utils.item;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minestom.server.entity.EquipmentSlotGroup;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
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
import rip.snicon.modules.container.json.*;
import rip.snicon.utils.ColorUtils;
import rip.snicon.utils.SkinUtils;
import rip.snicon.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

import static rip.snicon.utils.MaterialUtils.convertMaterialToNamespaceId;
import static rip.snicon.utils.MaterialUtils.convertToNamespaceIdMaterial;

public class ItemStackUtils {

    public static ItemStack createItemStack(Item item, Player player, InventorySettings settings) {
        Material material = convertToNamespaceIdMaterial(item.getId(), player);
        if (material == null) {
            Main.logger.error("Material for this item is invalid: " + item.getId());
            return ItemStack.of(Material.AIR, 1);
        }

        Component itemName = TextUtils.convertStringToComponent(item.getDisplay().getName());

        List<Component> itemLore = new ArrayList<>();
        for (List<String> row : item.getDisplay().getLore()){
            itemLore.add(TextUtils.convertStringToComponent(row));
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
            itemInASlot.set(ItemComponent.PROFILE, new HeadProfile(SkinUtils.getSkin(player, item.getSkin().getPlayer(), "", item.getSkin().getTexture(), "")));
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

    public static Item convertToItem(ItemStack itemStack, Player player) {
        Item item = new Item();

        // Extract and convert Material ID to Item ID
        String itemId = convertMaterialToNamespaceId(itemStack.material());
        item.setId(itemId);

        // Extract and set the container ID if available
        CustomData data = itemStack.get(ItemComponent.CUSTOM_DATA);
        if (data != null) {
            CompoundBinaryTag customData = data.nbt();
            String containerId = null;
            containerId = customData.getString("container_id");
            item.setContainerId(containerId);
        }


        // Extract and set the count (current and max)
        ItemCount itemCount = new ItemCount(itemStack.amount(), itemStack.maxStackSize());
        item.setCount(itemCount);

        // Extract and convert display name and lore
        ItemDisplay display = new ItemDisplay();
        Component itemName = itemStack.get(ItemComponent.ITEM_NAME);
        if (itemName == null) {
            itemName = Component.text("Empty Name");
        }
        display.setName(TextUtils.convertComponentToString(itemName)); // Assuming reverse of convertToComponentWithPlaceholders

        List<Component> itemLoreComponents = itemStack.get(ItemComponent.LORE);
        if (itemLoreComponents == null) {
            itemLoreComponents = new ArrayList<>();
        }

        List<List<String>> itemLore = new ArrayList<>();
        for (Component component : itemLoreComponents) {
            itemLore.add(TextUtils.convertComponentToString(component)); // Assuming reverse method
        }
        display.setLore(itemLore);

        // Extract and set the skin if material is PLAYER_HEAD
        if (itemStack.material() == Material.PLAYER_HEAD) {
            HeadProfile profile = itemStack.get(ItemComponent.PROFILE);
            if (profile != null) {
                ItemSkin skin = new ItemSkin("", "", "");
                skin.setPlayer(profile.name()); // Assuming method in HeadProfile to get player's name
                PlayerSkin playerSkin = profile.skin();
                if (playerSkin != null) {
                    skin.setTexture(playerSkin.textures());   // Assuming method in HeadProfile to get texture
                }
                item.setSkin(skin);
            }
        }

        // Extract and set the color if available
        DyedItemColor dyedColor = itemStack.get(ItemComponent.DYED_COLOR);
        if (dyedColor != null) {
            display.setDye_color(dyedColor.toString()); // Assuming reverse method
        }

        // Set enchantment glint and tooltip visibility
        display.setGlint(Boolean.TRUE.equals(itemStack.get(ItemComponent.ENCHANTMENT_GLINT_OVERRIDE)));
        display.setShow_tooltip(!itemStack.has(ItemComponent.HIDE_TOOLTIP));

        item.setDisplay(display);

        return item;
    }
}
