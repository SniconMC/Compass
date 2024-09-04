package rip.snicon.listeners;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerEntityInteractEvent;

public class InteractListener {

    public static void onYourMother(){
        GlobalEventHandler global = MinecraftServer.getGlobalEventHandler();
        global.addListener(PlayerEntityInteractEvent.class, event -> {
           event.getPlayer().sendMessage("Click!");
        });
    }

}
