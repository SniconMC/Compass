package rip.snicon.compass.listeners.instance;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.Instance;
import rip.snicon.compass.Main;
import rip.snicon.compass.instances.ShadowInstance;

public class ShadowInstanceNode {

    private final EventNode<PlayerEvent> shadowInstanceNode;


    public ShadowInstanceNode(EventNode<Event> parent){

        this.shadowInstanceNode = EventNode.type("shadow_instance",EventFilter.PLAYER);

        parent.addChild(shadowInstanceNode);

    }

}
