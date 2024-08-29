package rip.snicon.utils;

import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import rip.snicon.Main;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


public class SkinUtils {

    private static final Map<String, CachedSkin> cachedValues = new HashMap<>();

    public static PlayerSkin getSkin(Player player, String username, String uuid, String texture, String signature) {
        String cacheKey = getCacheKey(player, username, uuid, texture);

        // Check if the skin is cached and update the counter
        if (cachedValues.containsKey(cacheKey)) {
            CachedSkin cachedSkin = cachedValues.get(cacheKey);
            cachedSkin.incrementRequestCount();

            // If the request count exceeds 10, attempt to update the cache
            if (cachedSkin.getRequestCount() > 10) {
                PlayerSkin newSkin = fetchUpdatedSkin(player, username, uuid, texture, signature);
                if (isValidSkin(newSkin)) {
                    cachedSkin.updateSkin(newSkin);
                }
            }

            return cachedSkin.getPlayerSkin();
        }
        // Otherwise, create a new skin based on the input parameters

        PlayerSkin skin = createPlayerSkin(player, username, uuid, texture, signature);

        if (skin != null && !isDefaultSkin(skin)) {
            // Cache the new skin only if it's not the default skin (which implies an error didn't occur)
            cachedValues.put(cacheKey, new CachedSkin(skin));
        }

        return skin;
    }

    private static PlayerSkin createPlayerSkin(Player player, String username, String uuid, String texture, String signature) {
        try {
            if (username != null && Objects.equals(username, "this")) {
                return PlayerSkin.fromUsername(player.getUsername());
            }
            if (username != null && !username.isEmpty()) {
                return PlayerSkin.fromUsername(username);
            }
            if (uuid != null && !uuid.isEmpty()) {
                return PlayerSkin.fromUuid(uuid);
            }
            if (texture != null && !texture.isEmpty() && signature != null && !signature.isEmpty()) {
                return new PlayerSkin(texture, signature);
            }
            if (texture != null && !texture.isEmpty()) {
                return new PlayerSkin(texture, "");
            }
            return new PlayerSkin("", "");
        } catch (Exception e) {
            // Return a default skin indicating an error occurred
            return new PlayerSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQ0OWVjYjVlNmNlNjAwMjY1MzQ4MzZiZjgzYWIzN2NjNGRhZmQzMzNjYmRjYjVjMjFmNjIxNjYxZjVkMDgxYSJ9fX0=","");
        }
    }

    private static boolean isDefaultSkin(PlayerSkin skin) {
        // Check if the skin is the default one used when an exception occurs
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQ0OWVjYjVlNmNlNjAwMjY1MzQ4MzZiZjgzYWIzN2NjNGRhZmQzMzNjYmRjYjVjMjFmNjIxNjYxZjVkMDgxYSJ9fX0=".equals(skin.textures()) && "".equals(skin.signature());
    }



    private static String getCacheKey(Player player, String username, String uuid, String texture) {
        if (username.equalsIgnoreCase("this")){
            return player.getUsername();
        }
        if (!username.isEmpty()) {
            return username;
        }
        if(!uuid.isEmpty()) {
            return uuid;
        }
        return texture;
    }

    private static PlayerSkin fetchUpdatedSkin(Player player, String username, String uuid, String texture, String signature) {
        return createPlayerSkin(player, username, uuid, texture, signature);
    }

    private static boolean isValidSkin(PlayerSkin skin) {
        return skin != null && skin.textures() != null && !skin.textures().isEmpty();
    }

    // Nested class to hold cached skin data
    private static class CachedSkin {
        private PlayerSkin playerSkin;
        private final AtomicInteger requestCount;

        public CachedSkin(PlayerSkin playerSkin) {
            this.playerSkin = playerSkin;
            this.requestCount = new AtomicInteger(1); // Initialize to 1 since this is the first request
        }

        public PlayerSkin getPlayerSkin() {
            return playerSkin;
        }

        public int getRequestCount() {
            return requestCount.get();
        }

        public void incrementRequestCount() {
            requestCount.incrementAndGet();
        }

        public void updateSkin(PlayerSkin newSkin) {
            this.playerSkin = newSkin;
            requestCount.set(1); // Reset the request count after updating
        }
    }
}
