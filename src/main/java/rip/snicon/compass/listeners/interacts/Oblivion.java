package rip.snicon.compass.listeners.interacts;

import com.github.sniconmc.container.creators.ContainerCreator;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.tag.Tag;
import rip.snicon.compass.utils.function.FunctionUtils;

public class Oblivion {

    private final EventNode<Event> oblivionNode;

    public Oblivion(EventNode<Event> node) {
        this.oblivionNode = EventNode.all("container");

        onNPCInteract();

        node.addChild(oblivionNode);
    }

    public void onNPCInteract(){
        oblivionNode.addListener(PlayerEntityInteractEvent.class, event -> {

            Player player = event.getPlayer();
            Entity clickedEntity = event.getTarget();

            String page = clickedEntity.getTag(Tag.String("page"));
            String function = clickedEntity.getTag(Tag.String("function"));

            if (page == null || function == null) {
                return;
            }

            if (!function.isEmpty()) {
                new FunctionUtils(player, event, function);
                return;
            }
            if (!page.isEmpty()) {
                ContainerCreator.openContainer(player, page);
            }

        });

        oblivionNode.addListener(EntityAttackEvent.class, event -> {

            if (!(event.getEntity() instanceof Player player)) {
                return;
            }

            Entity clickedEntity = event.getTarget();

            String page = clickedEntity.getTag(Tag.String("page"));
            String function = clickedEntity.getTag(Tag.String("function"));

            if (function != null && !function.isEmpty()) {
                new FunctionUtils(player, event, function);
                return;
            }
            if (page != null && !page.isEmpty()) {
                ContainerCreator.openContainer(player, page);
            }

        });
    }
}
