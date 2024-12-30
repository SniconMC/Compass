package rip.snicon.compass.inventory.item;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.AttributeList;
import net.minestom.server.item.component.DyedItemColor;
import net.minestom.server.item.component.HeadProfile;
import net.minestom.server.tag.Tag;
import rip.snicon.compass.Main;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.utils.ColorUtils;
import rip.snicon.compass.utils.TextUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class MysteryItem {
    private Material material;

    // Count
    private int stackCount = 1;
    private int maxCount = 1;

    // Display properties
    private String name = "";
    private List<String> lore = Collections.emptyList();
    private Integer modelData = null;
    private boolean glint = false;
    private String dyeColor = "";
    private boolean showTooltip = true;

    // Skin object
    private PlayerSkin skin;

    private MysteryItemOrigin origin;
    private String itemIdentifier;

    // Identification properties
    private int hostSlot;
    private Inventory hostInventory;

    // Constructors for different use cases
    public MysteryItem(Material material) {
        this.material = material;
        registerEvent();
    }

    public MysteryItem(Material material, String name) {
        this(material);
        this.name = name;
    }

    public MysteryItem(Material material, String name, List<String> lore) {
        this(material, name);
        this.lore = lore;
    }

    public MysteryItem(Material material, String name, List<String> lore, int stackCount, MysteryItemOrigin origin) {
        this(material, name, lore);
        this.stackCount = stackCount;
        this.origin = origin;
    }

    public MysteryItem(Material material, MysteryItemOrigin origin) {
        this(material);
        this.origin = origin;
    }

    // Getters and setters for the fields


    public void setMaterial(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public int getStackCount() {
        return stackCount;
    }

    public void setStackCount(int count) {
        this.stackCount = count;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public Integer getModelData() {
        return modelData;
    }

    public void setModelData(Integer modelData) {
        this.modelData = modelData;
    }

    public boolean hasGlint() {
        return glint;
    }

    public void setGlint(boolean glint) {
        this.glint = glint;
    }

    public String getDyeColor() {
        return dyeColor;
    }

    public void setDyeColor(String dyeColor) {
        this.dyeColor = dyeColor;
    }

    public boolean isShowTooltip() {
        return showTooltip;
    }

    public void setShowTooltip(boolean showTooltip) {
        this.showTooltip = showTooltip;
    }

    public PlayerSkin getSkin() {
        return skin;
    }

    public void setSkin(PlayerSkin skin) {
        this.skin = skin;
    }

    public MysteryItemOrigin getOrigin() {
        return origin;
    }

    public void setOrigin(MysteryItemOrigin origin) {
        this.origin = origin;
    }

    public String getItemIdentifier() {
        return itemIdentifier;
    }

    public void setItemIdentifier(String itemKey) {
        this.itemIdentifier = itemKey;
    }

    public int getHostSlot() {
        return hostSlot;
    }

    public Inventory getHostInventory() {
        return hostInventory;
    }

    public void setHosts(Inventory hostInventory, int hostSlot) {
        this.hostInventory = hostInventory;
        this.hostSlot = hostSlot;
    }


    /**
     * Populate the item dynamically for a player.
     */
    abstract public void populateForPlayer(MysteryPlayer player);

    /**
     * Creates the ItemStack representation of this item.
     */
    public ItemStack createItemStack() {
        ItemStack.Builder builder = ItemStack.builder(material)
                .amount(stackCount)
                .maxStackSize(maxCount)
                .set(ItemComponent.ITEM_NAME, TextUtils.convertStringToComponent(name))
                .set(ItemComponent.LORE, TextUtils.convertStringToComponent(lore))
                .glowing(glint)
                .set(ItemComponent.DYED_COLOR, new DyedItemColor(ColorUtils.StringToRgb(dyeColor), false))
                .set(ItemComponent.ATTRIBUTE_MODIFIERS, new AttributeList(AttributeList.EMPTY.modifiers(), false));
        if (!showTooltip) {
            builder.set(ItemComponent.HIDE_TOOLTIP);
        }
        if (modelData != null) {
            builder.set(ItemComponent.CUSTOM_MODEL_DATA, modelData);
        }

        if (origin != null) {
            builder.setTag(Tag.String(MysteryItemTags.ITEM_ORIGIN.name()), origin.name());
        }

        if (itemIdentifier != null) {
            builder.setTag(Tag.String(MysteryItemTags.ITEM_IDENTIFIER.name()), itemIdentifier);
        }

        if (skin != null && material == Material.PLAYER_HEAD) {
            builder.set(ItemComponent.PROFILE, new HeadProfile(skin));
        }

        return builder.build();
    }


    public void registerEvent() {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerUseItemEvent.class, event -> {
            if (event.getPlayer() instanceof MysteryPlayer player) {
                // Only trigger this specific item

                String itemType = event.getItemStack().getTag(Tag.String(MysteryItemTags.ITEM_IDENTIFIER.name()));
                String itemOrigin = event.getItemStack().getTag(Tag.String(MysteryItemTags.ITEM_ORIGIN.name()));
                if (Objects.equals(itemType, this.itemIdentifier)) {
                    onUse(player);
                    if (Objects.equals(itemOrigin, MysteryItemOrigin.CONTAINER.name())) {

                        event.setCancelled(true);
                    }

                }

            }
        });

        MinecraftServer.getGlobalEventHandler().addListener(ItemDropEvent.class, event -> {
            if (event.getPlayer() instanceof MysteryPlayer player) {
                // Only trigger this specific item

                String itemType = event.getItemStack().getTag(Tag.String(MysteryItemTags.ITEM_IDENTIFIER.name()));
                String itemOrigin = event.getItemStack().getTag(Tag.String(MysteryItemTags.ITEM_ORIGIN.name()));
                if (Objects.equals(itemType, this.itemIdentifier)) {
                    onDrop(player);
                    if (Objects.equals(itemOrigin, MysteryItemOrigin.CONTAINER.name())) {

                        event.setCancelled(true);
                    }

                }

            }
        });


        MinecraftServer.getGlobalEventHandler().addListener(InventoryPreClickEvent.class, event -> {
            if (event.getPlayer() instanceof MysteryPlayer player) {
                // Only trigger this specific item



                String itemType = event.getClickedItem().getTag(Tag.String(MysteryItemTags.ITEM_IDENTIFIER.name()));
                String itemOrigin = event.getClickedItem().getTag(Tag.String(MysteryItemTags.ITEM_ORIGIN.name()));



                if (Objects.equals(itemType, this.itemIdentifier)) {
                    if (Objects.equals(itemOrigin, MysteryItemOrigin.CONTAINER.name())) {
                        onUse(player);
                        event.setCancelled(true);
                        return;
                    }
                    if (event.getClickType() == ClickType.DROP) {
                        player.getInventoryHandler().removeInventoryItem(event.getSlot());
                    }

                }


            }
        });
    }

    public abstract void onUse(MysteryPlayer player);
    public abstract void onDrop(MysteryPlayer player);
}
