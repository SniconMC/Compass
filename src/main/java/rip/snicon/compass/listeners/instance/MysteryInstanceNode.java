package rip.snicon.compass.listeners.instance;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.trait.PlayerEvent;
import rip.snicon.compass.instances.regions.MysteryRegion;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.player.PlayerRank;

import java.util.Arrays;

public class MysteryInstanceNode {

    private final EventNode<PlayerEvent> mysteryInstanceNode;


    public MysteryInstanceNode(EventNode<Event> parent){

        this.mysteryInstanceNode = EventNode.type("mystery_instance",EventFilter.PLAYER);


        parent.addChild(mysteryInstanceNode);

    }
}
