package rip.snicon.compass.instances;

import com.google.gson.*;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.Weather;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.utils.TextUtils;

import java.util.UUID;

public class MysteryInstance extends InstanceContainer {

    // Constants
    private static final double SPAWN_X = 29.5;
    private static final double SPAWN_Y = 9;
    private static final double SPAWN_Z = 28.5;
    private static final float SPAWN_PITCH = 0;
    private static final float SPAWN_YAW = 0;
    private static final double VOID_LIMIT_HEIGHT = -64;
    private static final Weather DEFAULT_WEATHER = Weather.CLEAR;
    private static final long TIME = 1000;
    private static final GameMode DEFAULT_GAMEMODE = GameMode.CREATIVE;
    private static final boolean DO_DAYLIGHT_CYCLE = false;

    public MysteryInstance(@NotNull AnvilLoader loader) {
        super(UUID.randomUUID(), DimensionType.OVERWORLD, loader);

        // Initialize Instance
        this.setTime(TIME);
        this.setWeather(DEFAULT_WEATHER);
        this.setTimeRate(DO_DAYLIGHT_CYCLE ? 1 : 0);

        // Register instance
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        instanceManager.registerInstance(this);

        // Global event handler
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();


        // Player spawn event
        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            if (event.getPlayer().getInstance() instanceof MysteryInstance instance && instance == this) {
                event.getPlayer().setGameMode(DEFAULT_GAMEMODE);
            }
        });

        // Player move event
        globalEventHandler.addListener(PlayerMoveEvent.class, event -> {
            if (!(event.getPlayer().getInstance() instanceof MysteryInstance instance) || instance != this) {
                return;
            }

            if (event.getPlayer().getPosition().y() <= VOID_LIMIT_HEIGHT) {
                event.getPlayer().teleport(new Pos(SPAWN_X, SPAWN_Y, SPAWN_Z, SPAWN_YAW, SPAWN_PITCH));
                event.getPlayer().sendMessage(TextUtils.convertStringToComponent(
                        "<gray>Oops... the <light_purple>Void</light_purple> sent you back!</gray>"
                ));
            }
        });
    }

    public Pos getSpawnPos() {
        return new Pos(SPAWN_X, SPAWN_Y, SPAWN_Z, SPAWN_YAW, SPAWN_PITCH);
    }
}
