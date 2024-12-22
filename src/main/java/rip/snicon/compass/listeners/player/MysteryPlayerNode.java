package rip.snicon.compass.listeners.player;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.inventory.InventoryType;
import rip.snicon.compass.inventory.MysteryInventoryType;
import rip.snicon.compass.player.MysteryPlayer;


public class MysteryPlayerNode {

        private final EventNode<PlayerEvent> mysteryPlayerNode;

        public MysteryPlayerNode(EventNode<Event> parent){

            this.mysteryPlayerNode = EventNode.type("mystery_player", EventFilter.PLAYER);

            preLoginEvent();
            playerSpawnEvent();
            playerMoveEvent();
            eventsToBeCanceled();

            parent.addChild(mysteryPlayerNode);
       }


       public void preLoginEvent(){
            this.mysteryPlayerNode.addListener(AsyncPlayerPreLoginEvent.class, event -> {
                final MysteryPlayer player = new MysteryPlayer(event.getPlayer());
            });
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


    public void eventsToBeCanceled() {
        mysteryPlayerNode.addListener(ItemDropEvent.class, event -> {
                event.setCancelled(true);
            });
        mysteryPlayerNode.addListener(PlayerBlockBreakEvent.class, event -> {

                if (event.getPlayer() instanceof MysteryPlayer player) {
                    player.addProfessionXp(2000);
                }


                event.setCancelled(true);
            });
        mysteryPlayerNode.addListener(PlayerBlockPlaceEvent.class, event -> {
            event.setCancelled(true);
        });
        }
}
