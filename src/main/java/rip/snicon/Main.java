package rip.snicon;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.instance.block.Block;
import net.minestom.server.ping.ResponseData;
import net.minestom.server.utils.identity.NamedAndIdentified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.rmi.UnexpectedException;
import java.util.*;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        // Initialize the server
        MinecraftServer minecraftServer = MinecraftServer.init();

        // Register Events (set spawn instance, teleport player at spawn)
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

        instanceContainer.setChunkLoader(new AnvilLoader("src/main/resources/worlds/hub"));

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(5.5, 66, 21.5, -180, 0));
        });

        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
           Player player = event.getPlayer();
           Set<Player> players = instanceContainer.getPlayers();

           for (Player onlinePlayer : players) {
                onlinePlayer.sendMessage("[+] " + player.getUsername());
           }
        });


        globalEventHandler.addListener(ServerListPingEvent.class, serverListPingEvent -> {

            // magic to make server favicon work
            String base64String = "";

            try {
                BufferedImage image = ImageIO.read(new File("src/main/resources/favicon/obama.png"));
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(image, "png", outputStream);
                base64String = Base64.getEncoder().encodeToString(outputStream.toByteArray());
                outputStream.close();
            } catch (IOException e) {
                    logger.error("" + e);
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


        // Start the server
        MojangAuth.init();
        minecraftServer.start("0.0.0.0", 25565);

    }
}