package rip.snicon.listeners;

import com.google.gson.Gson;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.ping.ResponseData;
import net.minestom.server.utils.identity.NamedAndIdentified;
import rip.snicon.Main;
import rip.snicon.instances.InstanceCreator;
import rip.snicon.instances.worlds.WorldInfo;
import rip.snicon.listeners.inventory.Container;
import rip.snicon.listeners.worlds.AFK;
import rip.snicon.listeners.worlds.Hub;
import rip.snicon.modules.container.json.*;
import rip.snicon.modules.placeholders.PlaceHolder;
import rip.snicon.utils.item.ItemStackUtils;
import rip.snicon.utils.json.Text;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class Global {

    public Global(){
        onPlayerConfig();
        onServerPing();
        npcAttackEvent();
        eventsToBeCanceled();

        Hub hubHandler = new Hub(MinecraftServer.getGlobalEventHandler());
        AFK afkHandler = new AFK(MinecraftServer.getGlobalEventHandler());
        Container containerHandler = new Container(MinecraftServer.getGlobalEventHandler());
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

            PlaceHolder.setPlayerPlaceholders(player, "player_name", player.getUsername());
            PlaceHolder.setPlayerPlaceholders(player, "player_world", instance.getDimensionName());
            // test this will be removed
            List<Component> value = new ArrayList<>();
            value.add(Component.text("Click to open!").color(NamedTextColor.YELLOW));
            ItemStack itemStack = ItemStack.of(Material.NETHER_STAR).withAmount(3).with(ItemComponent.ITEM_NAME, Component.text("Skyblock Menu").color(NamedTextColor.GREEN)).with(ItemComponent.LORE, value);
            Item item = ItemStackUtils.convertToItem(itemStack, player);

            String itemId = item.getId();
            String itemCount = new Gson().toJson(item.getCount(), ItemCount.class);
            String itemName = new Gson().toJson(item.getDisplay().getName(), List.class);
            String itemLore = new Gson().toJson(item.getDisplay().getLore(), List.class);
            String newItemLore = itemLore.substring(1, itemLore.length() - 1);
            String itemGlint = String.valueOf(item.getDisplay().isGlint());
            String itemDye = item.getDisplay().getDye_color();
            String itemSkin = new Gson().toJson(item.getSkin(), ItemSkin.class);
            String itemData = new Gson().toJson(item.getData(), ItemData.class);

            String playerRank = "{\"text\":\"[\",\"color\":\"dark_gray\"},{\"text\":\"Obama++\",\"color\":\"dark_red\"},{\"text\":\"]\",\"color\":\"dark_gray\"}";
            String playerUsername = "{\"text\":\" wi1helm_\",\"color\":\"white\"}";
            String price = "2,000";

            Main.logger.info("itemId: " + itemId);
            Main.logger.info("itemCount: " + itemCount);
            Main.logger.info("itemName: " + itemName);
            Main.logger.info("itemLore: " + newItemLore);
            Main.logger.info("itemGlint: " + itemGlint);
            Main.logger.info("itemDye: " + itemDye);
            Main.logger.info("itemSkin: " + itemSkin);
            Main.logger.info("itemData: " + itemData);
            Main.logger.info("playerRank: " + playerRank);
            Main.logger.info("playerUsername: " + playerUsername);
            Main.logger.info("price: " + price);


            PlaceHolder.setPlayerPlaceholders(player, "ah_slot_11_item", itemId);
            PlaceHolder.setPlayerPlaceholders(player, "ah_slot_11_item_count", itemCount);
            PlaceHolder.setPlayerPlaceholders(player, "ah_slot_11_item_name", itemName);
            PlaceHolder.setPlayerPlaceholders(player, "ah_slot_11_item_lore", newItemLore);
            PlaceHolder.setPlayerPlaceholders(player, "ah_slot_11_seller_rank", playerRank);
            PlaceHolder.setPlayerPlaceholders(player, "ah_slot_11_seller_username", playerUsername);
            PlaceHolder.setPlayerPlaceholders(player, "ah_slot_11_price", price);
            PlaceHolder.setPlayerPlaceholders(player, "ah_slot_11_item_glint", itemGlint);
            PlaceHolder.setPlayerPlaceholders(player, "ah_slot_11_item_dye", itemDye);
            PlaceHolder.setPlayerPlaceholders(player, "ah_slot_11_item_skin", itemSkin);
            PlaceHolder.setPlayerPlaceholders(player, "ah_slot_11_item_data", itemData);
            PlaceHolder.setPlayerPlaceholders(player, "player_rank", playerRank);
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
                Main.logger.error("Error converting image to base64: " + e);
            }

            ResponseData responseData = serverListPingEvent.getResponseData();

            // server icon
            responseData.setFavicon("data:image/png;base64," + base64String);

            // description
            responseData.setDescription(Component.text("haha yes very cool"));

            // fake players in server list
            responseData.addEntry(NamedAndIdentified.of("Notch", UUID.randomUUID()));
            responseData.addEntry(NamedAndIdentified.of("jeb_", UUID.randomUUID()));
            responseData.addEntry(NamedAndIdentified.of("Dinnerbone", UUID.randomUUID()));
            responseData.addEntry(NamedAndIdentified.of("Grumm", UUID.randomUUID()));
            responseData.addEntry(NamedAndIdentified.of("Deadmau5", UUID.randomUUID()));

            // add all online players to server list
            responseData.addEntries(MinecraftServer.getConnectionManager().getOnlinePlayers());

            // set online count to all online players
            responseData.setOnline(responseData.getEntries().size());

            // max server size always one more than online count
            responseData.setMaxPlayer(responseData.getEntries().size() + 1);
        });
    }

    public void npcAttackEvent() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(EntityAttackEvent.class, event -> {
            Player player = (Player) event.getTarget();
            player.damage(Damage.fromEntity(event.getEntity(),0.1F));
            player.takeKnockback(1,event.getEntity().getPosition().direction().normalize().neg().x(), event.getEntity().getPosition().direction().normalize().neg().z());
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
