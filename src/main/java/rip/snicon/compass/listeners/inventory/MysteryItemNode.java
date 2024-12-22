package rip.snicon.compass.listeners.inventory;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.item.EntityEquipEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.trait.ItemEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.tag.Tag;
import rip.snicon.compass.inventory.item.MysteryItemOrigin;
import rip.snicon.compass.inventory.item.MysteryItemTags;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.Objects;

public class MysteryItemNode {

    private final EventNode<ItemEvent> mysteryItemNode;

    public MysteryItemNode(EventNode<Event> parent){

        this.mysteryItemNode = EventNode.type("mystery_item", EventFilter.ITEM);

        eventsToBeCanceled();

        parent.addChild(mysteryItemNode);
    }

        public void eventsToBeCanceled() {
        mysteryItemNode.addListener(ItemDropEvent.class, event -> {
            event.setCancelled(true);
        });
    }
}
