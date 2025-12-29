package rip.snicon.compass.listeners.blockhunt;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.trait.PlayerEvent;
import rip.snicon.compass.content.bundlebot.BundleBot;
import rip.snicon.compass.instances.regions.MysteryRegion;
import rip.snicon.compass.inventory.blockhunt.bundlebot.BundleProfessorContainer;
import rip.snicon.compass.player.MysteryPlayer;

public class BundleBotNode {

    private final EventNode<PlayerEvent> bundleBotNode;

    public BundleBotNode(EventNode<Event> parent) {
        this.bundleBotNode = EventNode.type("bundle_bot", EventFilter.PLAYER);
        registerListeners();
        parent.addChild(bundleBotNode);
    }

    private void registerListeners() {
        handleBundleBotInteraction();
        handleEnterBundleBotRegion();
    }

    private void handleBundleBotInteraction() {
        bundleBotNode.addListener(PlayerEntityInteractEvent.class, event -> {
            final MysteryPlayer player = (MysteryPlayer) event.getPlayer();
            if (!player.getRegionHandler().isInRegion(MysteryRegion.BUNDLE_BOT)) return;

            if (event.getTarget() != BundleBot.getEntity()) return;

            player.openInventory(new BundleProfessorContainer().constructInventory(player));
        });
    }

    private void handleEnterBundleBotRegion() {
        bundleBotNode.addListener(PlayerMoveEvent.class, event -> {
            final MysteryPlayer player = (MysteryPlayer) event.getPlayer();

            boolean isInRegion = player.getRegionHandler().isInRegion(MysteryRegion.BUNDLE_BOT);
            boolean wasInRegion = BundleBot.isInRegion(player);

            if (isInRegion && !wasInRegion) {
                BundleBot.enter(player);
            } else if (!isInRegion && wasInRegion) {
                BundleBot.leave(player);
            }
        });
    }
}
