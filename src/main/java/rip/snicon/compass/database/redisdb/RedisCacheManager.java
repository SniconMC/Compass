package rip.snicon.compass.database.redisdb;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisCacheManager {
    private static JedisPool jedisPool;

    public static void initialize(String host, int port) {
        jedisPool = new JedisPool(host, port);
    }

    public static String fetch(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    public static void save(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        }
    }

    public static void delete(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        }
    }

    public static void shutdown() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }
}
