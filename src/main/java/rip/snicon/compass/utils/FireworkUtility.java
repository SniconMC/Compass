package rip.snicon.compass.utils;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.projectile.FireworkRocketMeta;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.FireworkExplosion;
import net.minestom.server.item.component.FireworkList;
import net.minestom.server.network.packet.server.play.EffectPacket;
import org.w3c.dom.css.RGBColor;
import rip.snicon.compass.player.MysteryPlayer;

import java.util.List;

public class FireworkUtility {


    public static void instantFirework(FireworkExplosion.Shape shape, List<RGBLike> colors, MysteryPlayer player){
        var firework = new Entity(EntityType.FIREWORK_ROCKET);
        firework.setNoGravity(true);

        var explosion = new FireworkExplosion(
                shape,
                colors,
                colors,
                false,
                true);

        firework.editEntityMeta(FireworkRocketMeta.class, meta ->
                meta.setFireworkInfo(ItemStack.of(Material.FIREWORK_ROCKET)
                        .with(ItemComponent.FIREWORKS, new FireworkList(
                                (byte) 0, List.of(explosion)))));

        firework.setInstance(player.getInstance(), player.getPosition().add(0.0D, 2.0D, 0.0D));
        firework.triggerStatus((byte) 17);


        MinecraftServer.getSchedulerManager().scheduleNextTick(firework::remove);
    }
}
