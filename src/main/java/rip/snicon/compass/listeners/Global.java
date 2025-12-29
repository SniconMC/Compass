package rip.snicon.compass.listeners;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import rip.snicon.compass.listeners.blockhunt.BundleBotNode;
import rip.snicon.compass.listeners.instance.MysteryInstanceNode;
import rip.snicon.compass.listeners.player.MysteryPlayerNode;

public class Global {

    private final GlobalEventHandler GNode = MinecraftServer.getGlobalEventHandler();


    public Global() {

        MysteryPlayerNode playerNode = new MysteryPlayerNode(GNode);
        MysteryInstanceNode instanceNode = new MysteryInstanceNode(GNode);
        BundleBotNode bundleBotNode = new BundleBotNode(GNode);
    }
}
