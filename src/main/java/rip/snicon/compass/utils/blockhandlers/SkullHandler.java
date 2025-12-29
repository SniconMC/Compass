package rip.snicon.compass.utils.blockhandlers;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class SkullHandler implements BlockHandler {

    public static final Key KEY = Key.key("minecraft:skull");

    @Override
    public @NotNull Collection<Tag<?>> getBlockEntityTags() {
        // The profile field will hold the NBT contents of ItemComponent.PROFILE
        Tag<?> profileTag = Tag.NBT("profile");

        return Collections.singletonList(profileTag);
    }

    @Override
    public Key getKey() {
        return KEY;
    }
}