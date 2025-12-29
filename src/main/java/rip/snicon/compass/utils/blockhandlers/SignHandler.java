package rip.snicon.compass.utils.blockhandlers;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public class SignHandler implements BlockHandler {
    public static final Key KEY = Key.key("minecraft:sign");

    @Override
    public @NotNull Collection<Tag<?>> getBlockEntityTags() {
        return Set.of(
                Tag.Byte("is_waxed"),
                Tag.NBT("front_text"),
                Tag.NBT("back_text"));
    }

    @Override
    public Key getKey() {
        return KEY;
    }
}