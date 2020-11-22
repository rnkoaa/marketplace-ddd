package com.marketplace.domain.repository;

/*
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
*/

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;

public class RedisTemplate implements Closeable, AutoCloseable {
//    private final RedisClient redisClient;
//    private final StatefulRedisConnection<String, String> connection;
//    RedisCommands<String, String> redisCommands;
    private final RedisConverter redisConverter;

    public RedisTemplate(/*RedisClient redisClient,*/ RedisConverter redisConverter) {
//        this.redisClient = redisClient;
        this.redisConverter = redisConverter;
//        this.connection = this.redisClient.connect();
//        this.redisCommands = connection.sync();
    }

    public boolean exists(String key) {
//        return redisCommands.exists(key) > 0;
        return false;
    }

    public void put(String key, Object object) {
        String serialize = redisConverter.serialize(object);
//        redisCommands.set(key, serialize);
    }

    public <T> Optional<T> get(String key, Class<T> clzz) {
//        String response = redisCommands.get(key);
//        if (response == null || response.isEmpty()) {
//            return Optional.empty();
//        }
//        return redisConverter.deserialize(response, clzz);
        return Optional.empty();
    }

    @Override
    public void close() throws IOException {
//        connection.close();
//        redisClient.shutdown();
    }
}
