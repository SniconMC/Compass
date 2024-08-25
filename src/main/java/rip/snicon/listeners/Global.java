package rip.snicon.listeners;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.ping.ResponseData;
import net.minestom.server.utils.identity.NamedAndIdentified;
import rip.snicon.Main;
import rip.snicon.instances.InstanceCreator;
import rip.snicon.instances.worlds.WorldInfo;
import rip.snicon.listeners.inventory.Container;
import rip.snicon.listeners.placeholders.Placeholder;
import rip.snicon.listeners.worlds.AFK;
import rip.snicon.listeners.worlds.Hub;
import rip.snicon.modules.placeholders.PlaceholderManager;
import rip.snicon.utils.motd.MOTD;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Global {

    public Global(){
        onPlayerConfig();
        onServerPing();
        attackEvent();
        eventsToBeCanceled();

        Hub hubHandler = new Hub(MinecraftServer.getGlobalEventHandler());
        AFK afkHandler = new AFK(MinecraftServer.getGlobalEventHandler());
        Container containerHandler = new Container(MinecraftServer.getGlobalEventHandler());
        Placeholder placeholderHandler = new Placeholder(MinecraftServer.getGlobalEventHandler());
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

            // Placeholders
            PlaceholderManager.setPlaceholderToPlayer(player, "player_name", player.getUsername());

            // TODO make un-static
            String playerRank = "<dark_gray>[</dark_gray><dark_red>Obama++</dark_red><dark_gray>]</dark_gray>";

            PlaceholderManager.setPlaceholderToPlayer(player, "player_rank", playerRank);
            PlaceholderManager.setPlaceholderToPlayer(player, "player_item", "minecraft:tnt");
        });
    }



    public void onServerPing() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(ServerListPingEvent.class, serverListPingEvent -> {

            var connection = serverListPingEvent.getConnection().getIdentifier();

            // magic to make server favicon work
            String base64String = "";

            try {
                BufferedImage image = ImageIO.read(new File("resources/favicon/znopp logo 8x8 emblem 64x.png"));
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(image, "png", outputStream);
                base64String = Base64.getEncoder().encodeToString(outputStream.toByteArray());
                outputStream.close();
            } catch (IOException e) {
                Main.logger.error("Error converting image to base64: " + e);
            }

            ResponseData responseData = serverListPingEvent.getResponseData();

            // server icon
            responseData.setFavicon("data:image/png;base64," + base64String);

            // ⥊⥋ ⥏⥑
            // ←→ ↑↓

            // Set the server description
            responseData.setDescription(MOTD.createMOTD());

            // fake players in server list
            responseData.addEntry(NamedAndIdentified.of("Notch", UUID.randomUUID()));
            responseData.addEntry(NamedAndIdentified.of("jeb_", UUID.randomUUID()));
            responseData.addEntry(NamedAndIdentified.of("Grumm", UUID.randomUUID()));
            responseData.addEntry(NamedAndIdentified.of("Dinnerbone", UUID.randomUUID()));
            responseData.addEntry(NamedAndIdentified.of("C418", UUID.randomUUID()));

            // add all online players to server list
            responseData.addEntries(MinecraftServer.getConnectionManager().getOnlinePlayers());

            // set online count to all online players
            responseData.setOnline(responseData.getEntries().size());

            // max server size always one more than online count
            responseData.setMaxPlayer(responseData.getEntries().size() + 1);
        });
    }

    public void attackEvent() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(EntityAttackEvent.class, event -> {
            EntityCreature entityCreature = (EntityCreature) event.getTarget();
            entityCreature.damage(Damage.fromEntity(event.getEntity(),0.1F));
            entityCreature.takeKnockback(1,event.getEntity().getPosition().direction().normalize().neg().x(), event.getEntity().getPosition().direction().normalize().neg().z());
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
