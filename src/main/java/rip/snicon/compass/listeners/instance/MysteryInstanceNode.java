package rip.snicon.compass.listeners.instance;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.PlayerEvent;

public class MysteryInstanceNode {

    private final EventNode<PlayerEvent> mysteryInstanceNode;


    public MysteryInstanceNode(EventNode<Event> parent){

        this.mysteryInstanceNode = EventNode.type("mystery_instance",EventFilter.PLAYER);


        parent.addChild(mysteryInstanceNode);

    }
}
