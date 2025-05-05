package rip.snicon.compass.listeners.instance;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerPluginMessageEvent;
import net.minestom.server.event.trait.PlayerEvent;
import rip.snicon.compass.inventory.selector.HubSelector;

import java.util.List;

public class MysteryInstanceNode {

    private final EventNode<PlayerEvent> mysteryInstanceNode;


    public MysteryInstanceNode(EventNode<Event> parent) {

        this.mysteryInstanceNode = EventNode.type("mystery_instance", EventFilter.PLAYER);

        parent.addChild(mysteryInstanceNode);

    }
}
