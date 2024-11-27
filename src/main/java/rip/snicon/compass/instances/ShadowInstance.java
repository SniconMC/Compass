package rip.snicon.compass.instances;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;

import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.NotNull;
import rip.snicon.compass.utils.TextUtils;
import rip.snicon.compass.utils.TimeUtils;
import rip.snicon.compass.utils.WeatherUtils;


import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

public class ShadowInstance extends InstanceContainer {

    private String name;
    private boolean defaultSpawn;
    private double spawnX, spawnY, spawnZ;
    private float spawnPitch, spawnYaw;
    private boolean hasVoidLimit;
    private double voidLimitHeight;
    private String weather, time, defaultGamemode;
    private boolean doDaylightCycle;

    // Constructor
    public ShadowInstance(@NotNull AnvilLoader loader) {
        super(UUID.randomUUID(), DimensionType.OVERWORLD, loader);
    }


    // Method to populate fields from JSON
    public void populateFromJson(JsonObject json) {
        this.name = json.get("name").getAsString();
        this.defaultSpawn = json.get("defaultSpawn").getAsBoolean();
        this.spawnX = json.get("spawnX").getAsDouble();
        this.spawnY = json.get("spawnY").getAsDouble();
        this.spawnZ = json.get("spawnZ").getAsDouble();
        this.spawnPitch = json.get("spawnPitch").getAsFloat();
        this.spawnYaw = json.get("spawnYaw").getAsFloat();
        this.hasVoidLimit = json.get("hasVoidLimit").getAsBoolean();
        this.voidLimitHeight = json.get("voidLimitHeight").getAsDouble();
        this.weather = json.get("weather").getAsString();
        this.time = json.get("time").getAsString();
        this.defaultGamemode = json.get("defaultGamemode").getAsString();
        this.doDaylightCycle = json.get("doDaylightCycle").getAsBoolean();
    }

    // Initialize custom settings
    public void initialize() {

        this.setTime(TimeUtils.convertTime(this.time));
        this.setWeather(WeatherUtils.convertWeather(this.weather));
        this.setTimeRate(this.doDaylightCycle ? 1 : 0);

        // If default instance. Make the player spawn there.
        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent.class, event -> {


            if (this.defaultSpawn) event.setSpawningInstance(this);


        });

        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, event -> {
            if (event.getPlayer().getInstance() instanceof ShadowInstance instance && instance == this) {
                event.getPlayer().teleport(new Pos(this.spawnX, this.spawnY, this.spawnZ, this.spawnYaw, this.spawnPitch));
                event.getPlayer().setGameMode(defaultGamemode != null ? GameMode.valueOf(defaultGamemode) : GameMode.SURVIVAL);

            }

        });


        MinecraftServer.getGlobalEventHandler().addListener(PlayerMoveEvent.class, event -> {
            if (event.getPlayer().getInstance() instanceof ShadowInstance instance && instance != this) {
                return;
            }

            if (event.getPlayer().getPosition().y() <= this.voidLimitHeight && this.hasVoidLimit) {
                event.getPlayer().teleport(new Pos(this.spawnX, this.spawnY, this.spawnZ, this.spawnYaw, this.spawnPitch));
                event.getPlayer().sendMessage(TextUtils.convertStringToComponent("<gray>oops.. the <light_purple>Void</light_purple>, sent you back!</gray>"));
            }
        });

    }

    // Factory method to load from JSON
    public static ShadowInstance fromJson(String jsonContent, @NotNull AnvilLoader loader) {
        try {
            // Parse the JSON into a JsonObject
            JsonObject jsonObject = JsonParser.parseString(jsonContent).getAsJsonObject();

            // Create a new ShadowInstance using the constructor
            ShadowInstance instance = new ShadowInstance(loader);

            // Populate the instance fields with JSON data
            instance.populateFromJson(jsonObject);

            // Initialize any additional logic
            instance.initialize();

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create ShadowInstance: " + e.getMessage(), e);
        }
    }
}
