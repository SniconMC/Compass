package rip.snicon.compass.listeners.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.trait.PlayerEvent;
import rip.snicon.compass.player.ShadowPlayer;
import rip.snicon.compass.utils.TextUtils;

import static rip.snicon.compass.player.ShadowPlayerUtility.startOfflineAnimation;

public class ShadowPlayerNode {

        private final EventNode<PlayerEvent> shadowPlayerNode;

        public ShadowPlayerNode(EventNode<Event> parent){

            this.shadowPlayerNode = EventNode.type("shadow_player", EventFilter.PLAYER);

            preLoginEvent();
            playerSpawnEvent();
            eventsToBeCanceled();

            parent.addChild(shadowPlayerNode);
       }


       public void preLoginEvent(){
            this.shadowPlayerNode.addListener(AsyncPlayerPreLoginEvent.class, event -> {
                final ShadowPlayer player = new ShadowPlayer(event.getPlayer());
            });
       }

        public void playerSpawnEvent() {
            this.shadowPlayerNode.addListener(PlayerSpawnEvent.class, event -> {
                if (event.getPlayer() instanceof ShadowPlayer player) {
                    if (player.isOfflineMode()) {
                        startOfflineAnimation(player);

                    }
                }
            });
        }


    public void eventsToBeCanceled() {
            shadowPlayerNode.addListener(ItemDropEvent.class, event -> {
                event.setCancelled(true);
            });
            shadowPlayerNode.addListener(PlayerBlockBreakEvent.class, event -> {
                event.setCancelled(true);
            });
        }
}
