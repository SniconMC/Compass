package rip.snicon.listeners.inventory;

import com.google.gson.Gson;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import rip.snicon.Main;
import rip.snicon.modules.container.ContainerCreator;
import rip.snicon.modules.container.json.Item;
import rip.snicon.utils.function.FunctionUtils;
import rip.snicon.utils.item.ItemStackUtils;

public class Container {

    private final EventNode<Event> containerNode;

    public Container(EventNode<Event> node) {
        this.containerNode = EventNode.all("container");
        node.addChild(containerNode);
        onInventoryClick();
        onHotbarClick();
    }

    public void onInventoryClick(){
        containerNode.addListener(InventoryPreClickEvent.class, event -> {
            Player player = event.getPlayer();

            ItemStack itemStack = event.getClickedItem();

            var data = itemStack.get(ItemComponent.CUSTOM_DATA);
            if (data == null){
                event.setCancelled(true);
                return;
            }
            var nbtTag = data.nbt();
            String function = nbtTag.getString("function");
            if (!function.isEmpty()) {
                new FunctionUtils(player, event, function);
                event.setCancelled(true);
                return;
            }

            String redirect = nbtTag.getString("redirect");
            if (!redirect.isEmpty()){
                event.setCancelled(true);
                if (redirect.equals("close")){

                    player.closeInventory();
                    return;
                }
                ContainerCreator.openContainer(player, redirect);
                return;
            }

            event.setCancelled(true);

        });
    }

    public void onHotbarClick(){
        containerNode.addListener(PlayerUseItemEvent.class, event -> {
            Player player = event.getPlayer();

            ItemStack itemStack = event.getItemStack();

            var data = itemStack.get(ItemComponent.CUSTOM_DATA);
            if (data == null){
                return;
            }
            var nbtTag = data.nbt();
            String redirect = nbtTag.getString("redirect");

            ContainerCreator.openContainer(player, redirect);
        });
        containerNode.addListener(PlayerBlockPlaceEvent.class, event -> {
            Player player = event.getPlayer();

            ItemStack itemStack = event.getEntity().getItemInMainHand();

            var data = itemStack.get(ItemComponent.CUSTOM_DATA);
            if (data == null){
                return;
            }
            var nbtTag = data.nbt();
            String redirect = nbtTag.getString("redirect");
            event.setCancelled(true);
            ContainerCreator.openContainer(player, redirect);

        });
    }
}
