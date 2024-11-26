package rip.snicon.compass.listeners;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import rip.snicon.compass.listeners.player.ShadowPlayerNode;

public class Global {

    private final GlobalEventHandler GNode = MinecraftServer.getGlobalEventHandler();


    public Global() {
        ShadowPlayerNode shadowPN = new ShadowPlayerNode(GNode);
    }
}
