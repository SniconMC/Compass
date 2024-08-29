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
import org.jetbrains.annotations.NotNull;
import rip.snicon.Main;
import rip.snicon.modules.container.json.*;
import rip.snicon.utils.ColorUtils;
import rip.snicon.utils.SkinUtils;
import rip.snicon.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

import static rip.snicon.utils.MaterialUtils.convertMaterialToNamespaceId;
import static rip.snicon.utils.MaterialUtils.convertToNamespaceIdMaterial;

/**
 * Utility class for handling item stack creation and conversion between custom {@link Item} objects
 * and {@link ItemStack} objects used by the Minestom server.
 *
 * <p>This class provides methods to create an {@link ItemStack} from an {@link Item} and to convert
 * an {@link ItemStack} back into a custom {@link Item} object. It handles setting various item properties
 * like name, lore, count max stack size, custom data, player head skins, and colors.</p>
 *
 * @see Item
 * @see ItemStack
 * @see net.minestom.server.item.ItemComponent
 * @see net.minestom.server.entity.Player
 * @see rip.snicon.utils.TextUtils
 *
 * @author Wi1helm
 * @author znopp
 * @author Snicon
 */
public class ItemStackUtils {

    /**
     * Creates an {@link ItemStack} based on the provided {@link Item}, {@link Player} and {@link InventorySettings}.
     * The method converts the item's ID to a corresponding {@link Material} and sets various properties on the
     * ItemStack such as name, lore, count max stack size, custom data and specific properties like player head skin
     * or dyed color if applicable.
     *
     * <p>If the item's material is invalid (null), an {@link ItemStack} of {@link Material#AIR} with a quantity of 1
     * will be returned, and an error message will be logged.</p>
     *
     * <p>This method also handles setting a custom player head profile if the item is a player head and applying any
     * enchantment glint or tooltip visibility settings specified in the item properties.</p>
     *
     * @param item The {@link Item} object representing the item to be converted into an {@link ItemStack}
     * @param player The {@link Player} for whom the item stack is being created. This is used for player-specific
     *               data like skins
     * @param settings The {@link InventorySettings} is currently not being used for anything.
     * @return A newly created {@link ItemStack} configured based on the input parameters.
     *
     * @see #convertToItem(ItemStack, Player)
     * @see net.minestom.server.item.ItemStack#of(Material, int)
     *
     * @author Wi1helm
     * @author znopp
     */
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
        var customData = getEntries(item);

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

    /**
     * Builds a {@link CompoundBinaryTag} containing custom data entries for the provided {@link Item}.
     * This method checks for null values and conditionally adds data to the tag based on the item's properties.
     *
     * @param item The {@link Item} containing data that needs to be converted into a {@link CompoundBinaryTag}.
     * @return A {@link CompoundBinaryTag} containing the custom data for the item.
     *
     * @see net.kyori.adventure.nbt.CompoundBinaryTag
     *
     * @author znopp
     */
    private static @NotNull CompoundBinaryTag getEntries(Item item) {
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
        return customDataBuilder.build();
    }

    /**
     * Converts a {@link ItemStack} back into a custom {@link Item} object. This method extracts data from
     * the {@link ItemStack}, such as material ID, container ID, item count, display properties (name, lore),
     * skin data (if the item is a player head), color, and other settings to recreate the original {@link Item}.
     *
     * @param itemStack The {@link ItemStack} to be converted into a custom {@link Item}.
     * @param player The {@link Player} context, which may affect the conversion process.
     * @return A new {@link Item} object constructed based on the properties extracted from the {@link ItemStack}.
     *
     * @see #createItemStack(Item, Player, InventorySettings)
     * @see Item
     *
     * @author Wi1helm
     * @author znopp
     */
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
