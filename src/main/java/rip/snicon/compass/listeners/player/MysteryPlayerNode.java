package rip.snicon.compass.listeners.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.inventory.InventoryType;
import rip.snicon.compass.Main;
import rip.snicon.compass.instances.MysteryInstanceType;
import rip.snicon.compass.inventory.MysteryInventoryType;
import rip.snicon.compass.inventory.item.MysteryItemType;
import rip.snicon.compass.player.MysteryDataHandler;
import rip.snicon.compass.player.MysteryPlayer;
import rip.snicon.compass.sidebar.MysterySidebar;


public class MysteryPlayerNode {

        private final EventNode<PlayerEvent> mysteryPlayerNode;

        public MysteryPlayerNode(EventNode<Event> parent){

            this.mysteryPlayerNode = EventNode.type("mystery_player", EventFilter.PLAYER);

            playerSpawnEvent();
            playerMoveEvent();
            playerDisconnectEvent();
            playerConfigEvent();
            eventsToBeCanceled();

            parent.addChild(mysteryPlayerNode);
       }

        public void playerSpawnEvent() {
            this.mysteryPlayerNode.addListener(PlayerSpawnEvent.class, event -> {
                if (event.getPlayer() instanceof MysteryPlayer player) {
                    player.loadPlayerInventory();
                    player.updateRegion();
                }
            });
        }

        public void playerMoveEvent() {
            this.mysteryPlayerNode.addListener(PlayerMoveEvent.class, event -> {
                if (event.getPlayer() instanceof MysteryPlayer player) {
                    player.updateRegion();
                }
            });
        }

    public void playerDisconnectEvent() {
        this.mysteryPlayerNode.addListener(PlayerDisconnectEvent.class, event -> {
            if (event.getPlayer() instanceof MysteryPlayer player) {
                MysteryDataHandler dataHandler = player.getDataHandler();
                dataHandler.saveDataToDatabase();
                MysteryDataHandler.clearCache(player.getUuid());
                MysterySidebar.getSidebarCache().remove(player.getUuid());
            }
        });
    }

    public void playerConfigEvent() {
        this.mysteryPlayerNode.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            if (event.getPlayer() instanceof MysteryPlayer player) {
                player.getDataHandler().fetchDataFromDatabase();
                event.setSpawningInstance(MysteryInstanceType.HUB.getInstance());
                event.getPlayer().setRespawnPoint(MysteryInstanceType.HUB.getInstance().getSpawnPos());
            }
        });
    }



    public void eventsToBeCanceled() {
        mysteryPlayerNode.addListener(ItemDropEvent.class, event -> {
                event.setCancelled(true);
            });
        mysteryPlayerNode.addListener(PlayerBlockBreakEvent.class, event -> {

                if (event.getPlayer() instanceof MysteryPlayer player) {
                    player.addProfessionXp(2000);
                    player.addItem(MysteryItemType.EXAMPLE_ITEM);
                }


                event.setCancelled(true);
            });
        mysteryPlayerNode.addListener(PlayerBlockPlaceEvent.class, event -> {
            event.setCancelled(true);
        });
        }
}
