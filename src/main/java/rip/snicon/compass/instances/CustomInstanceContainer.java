package rip.snicon.compass.instances;

import net.minestom.server.instance.IChunkLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rip.snicon.compass.instances.worlds.WorldInfo;

import java.util.UUID;

public class CustomInstanceContainer extends InstanceContainer {

    private final WorldInfo worldInfo; // Store WorldInfo
    private final String worldName; // Store the name of the world

    // Custom constructor accepting WorldInfo and worldName
    public CustomInstanceContainer(@NotNull AnvilLoader loader, @NotNull WorldInfo worldInfo) {
        super(UUID.randomUUID(), DimensionType.OVERWORLD, loader, NamespaceID.from(worldInfo.getName()));
        this.worldInfo = worldInfo;
        this.worldName = worldInfo.getName();
    }

    // Getter for WorldInfo
    public WorldInfo getWorldInfo() {
        return worldInfo;
    }

    // Getter for World Name
    public String getWorldName() {
        return worldName;
    }
}
