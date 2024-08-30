package rip.snicon.utils;

import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import rip.snicon.Main;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Utility class for managing player skins with caching support.
 *
 * <p>This class provides methods to obtain a {@link PlayerSkin} for a given player or set of
 * skin parameters. It caches skin data to optimize performance and reduce redundant skin requests.
 * The cache is updated if the number of requests for a particular skin exceeds a threshold.</p>
 *
 * @see PlayerSkin
 * @see <a href="https://javadoc.minestom.net/">Minestom Documentation</a>
 *
 * @author Wi1helm
 * @author znopp
 */
public class SkinUtils {

    /**
     * A map that caches player skins based on a cache key.
     * The cache key is generated from player and skin attributes.
     */
    private static final Map<String, CachedSkin> cachedValues = new HashMap<>();


    /**
     * Retrieves a {@link PlayerSkin} based on the provided parameters, with caching.
     *
     * <p>This method checks if a skin is available in the cache using a generated cache key.
     * If the skin is found and the request count exceeds a threshold, it attempts to update the
     * cached skin. If the skin is not in the cache, a new {@link PlayerSkin} is created based on
     * the provided parameters and cached if valid.</p>
     *
     * @param player   The player requesting the skin.
     * @param username The username associated with the skin, or {@code null} if not available.
     * @param uuid     The UUID associated with the skin, or {@code null} if not available.
     * @param texture  The texture data for the skin, or {@code null} if not available.
     * @param signature The signature for the skin, or {@code null} if not available.
     * @return The {@link PlayerSkin} corresponding to the parameters, or a default skin if invalid.
     *
     * @author Wi1helm
     * @author znopp
     */
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

    /**
     * Creates a {@link PlayerSkin} based on the provided parameters.
     *
     * <p>This method attempts to create a {@link PlayerSkin} using different combinations of
     * username, UUID, texture, and signature. If none of these are valid, a default skin is returned.</p>
     *
     * @param player   The player requesting the skin (used to get the player's username if needed).
     * @param username The username for the skin, or {@code null} if not available.
     * @param uuid     The UUID for the skin, or {@code null} if not available.
     * @param texture  The texture data for the skin, or {@code null} if not available.
     * @param signature The signature for the skin, or {@code null} if not available.
     * @return A {@link PlayerSkin} created from the provided parameters, or a default skin if an error occurs.
     */
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
            Main.logger.error("your motherly mother");
            // Return a default skin indicating an error occurred
            return new PlayerSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQ0OWVjYjVlNmNlNjAwMjY1MzQ4MzZiZjgzYWIzN2NjNGRhZmQzMzNjYmRjYjVjMjFmNjIxNjYxZjVkMDgxYSJ9fX0=","");
        }
    }

    /**
     * Checks if a {@link PlayerSkin} is the default skin used when an error occurs.
     *
     * @param skin The {@link PlayerSkin} to check.
     * @return {@code true} if the skin is the default skin, {@code false} otherwise.
     */
    private static boolean isDefaultSkin(PlayerSkin skin) {
        // Check if the skin is the default one used when an exception occurs
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQ0OWVjYjVlNmNlNjAwMjY1MzQ4MzZiZjgzYWIzN2NjNGRhZmQzMzNjYmRjYjVjMjFmNjIxNjYxZjVkMDgxYSJ9fX0=".equals(skin.textures()) && "".equals(skin.signature());
    }

    /**
     * Generates a cache key based on player and skin attributes.
     *
     * @param player   The player requesting the skin (used if username is "this").
     * @param username The username associated with the skin.
     * @param uuid     The UUID associated with the skin.
     * @param texture  The texture data for the skin.
     * @return A unique cache key for the skin based on the provided parameters.
     *
     * @author znopp
     * @author Wi1helm
     */
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

    /**
     * Fetches an updated {@link PlayerSkin} if needed.
     *
     * <p>This method is used to refresh the cached skin if the request count for a particular
     * cache key exceeds a threshold.</p>
     *
     * @param player   The player requesting the skin.
     * @param username The username associated with the skin.
     * @param uuid     The UUID associated with the skin.
     * @param texture  The texture data for the skin.
     * @param signature The signature for the skin.
     * @return A newly created {@link PlayerSkin} based on the provided parameters.
     *
     * @author Wi1helm
     */
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
