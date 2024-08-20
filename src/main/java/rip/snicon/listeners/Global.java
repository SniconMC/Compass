package rip.snicon.listeners;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.ping.ResponseData;
import net.minestom.server.utils.identity.NamedAndIdentified;
import rip.snicon.instances.InstanceCreator;
import rip.snicon.instances.worlds.WorldInfo;
import rip.snicon.listeners.worlds.AFK;
import rip.snicon.listeners.worlds.Hub;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

public class Global {

    public Global(){
        onPlayerConfig();
        onServerPing();
        eventsToBeCanceled();

        Hub hubHandler = new Hub(MinecraftServer.getGlobalEventHandler());
        AFK afkHandler = new AFK(MinecraftServer.getGlobalEventHandler());
    }

    public void onPlayerConfig(){
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();

            Instance instance = InstanceCreator.getInstanceMap().get("hub");
            WorldInfo info = InstanceCreator.getWorldMap().get("hub");
            Pos instanceStartingPos = new Pos(info.getSpawnX(), info.getSpawnY(), info.getSpawnZ(), info.getSpawnYaw(), info.getSpawnPitch());

            event.setSpawningInstance(instance);
            player.setRespawnPoint(instanceStartingPos);
        });
    }



    public void onServerPing() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(ServerListPingEvent.class, serverListPingEvent -> {

            // magic to make server favicon work
            String base64String = "";

            try {
                BufferedImage image = ImageIO.read(new File("resources/favicon/obama.png"));
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(image, "png", outputStream);
                base64String = Base64.getEncoder().encodeToString(outputStream.toByteArray());
                outputStream.close();
            } catch (IOException e) {
                System.out.println("Error" + e);
            }

            ResponseData responseData = serverListPingEvent.getResponseData();

            // server icon
            responseData.setFavicon("data:image/png;base64," + base64String);

            // description
            responseData.setDescription(Component.text("haha yes very cool"));

            // fake players in server list
            responseData.addEntry(NamedAndIdentified.of("Notch", UUID.randomUUID()));
            responseData.addEntry(NamedAndIdentified.of("Deadmau5", UUID.randomUUID()));
            responseData.addEntry(NamedAndIdentified.of("jeb_", UUID.randomUUID()));
            responseData.addEntry(NamedAndIdentified.of("Grumm", UUID.randomUUID()));

            // add all online players to server list
            responseData.addEntries(MinecraftServer.getConnectionManager().getOnlinePlayers());

            // set online count to all online players
            responseData.setOnline(responseData.getEntries().size());

            // max server size always one more than online count
            responseData.setMaxPlayer(responseData.getEntries().size() + 1);
        });
    }

    public void eventsToBeCanceled() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(PlayerBlockBreakEvent.class, event -> {
            event.setCancelled(true);
        });
        globalEventHandler.addListener(PlayerBlockInteractEvent.class, event -> {
            Player player = event.getPlayer();
            player.getInventory().update();
            event.setCancelled(true);
        });
    }
}
