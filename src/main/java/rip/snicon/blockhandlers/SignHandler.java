package rip.snicon.blockhandlers;

import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class SignHandler implements BlockHandler {

    @Override
    public @NotNull NamespaceID getNamespaceId() {
        return NamespaceID.from("minecraft:oak_sign");
    }

    @Override
    public @NotNull Collection<Tag<?>> getBlockEntityTags() {
        // The profile field will hold the NBT contents of ItemComponent.PROFILE
        Tag<?> profileTag = Tag.NBT("profile");

        return Collections.singletonList(profileTag);
    }
}
