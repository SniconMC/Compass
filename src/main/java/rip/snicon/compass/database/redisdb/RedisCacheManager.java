package rip.snicon.compass.database.redisdb;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisCacheManager {
    private final JedisPool jedisPool;

    public RedisCacheManager(String host, int port) {
        jedisPool = new JedisPool(host, port);
    }

    public String fetch(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    public void save(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        }
    }

    public void delete(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        }
    }

    public void shutdown() {
        jedisPool.close();
    }
}
