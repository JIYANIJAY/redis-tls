package com.example.redis;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedisImplementation implements RedisOperation{

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private HashOperations<String, String, String> hashOperations;

    @Autowired
    private ValueOperations<String, String> valueOperations;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${spring.data.redis.cache.time-to-live}")
    Long defaultExpirationSeconds;


    /**
     * Save an object in a Redis hash with a specified expiration time.
     *
     * @param prefix              The prefix for the Redis hash.
     * @param key                 The key within the Redis hash.
     * @param value               The object to be stored in Redis.
     * @param expirationInSeconds The expiration time for the key-value pair in
     *                            seconds.
     */

    public void put(String prefix, String key, Object value, long expirationInSeconds) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            hashOperations.put(prefix, key, jsonValue);
            long ttl = expirationInSeconds > 0 ? expirationInSeconds : defaultExpirationSeconds;
            if (ttl > 0) {
                redisTemplate.expire(prefix, ttl, TimeUnit.SECONDS);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve all values from a Redis hash and deserialize them to a list of
     * objects.
     *
     * @param hashKey   The key of the Redis hash.
     * @param valueType The type of objects to deserialize to.
     * @return A list of deserialized objects.
     */

    public List<Object> getAllValues(String hashKey, Class<?> valueType) {
        List<Object> result = new ArrayList<>();
        try {
            Map<String, String> hashEntries = hashOperations.entries(hashKey);
            for (String jsonValue : hashEntries.values()) {
                Object deserializedValue = objectMapper.readValue(jsonValue, valueType);
                result.add(deserializedValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Retrieve an object by its key from a Redis hash and deserialize it to the
     * specified type.
     *
     * @param prefix    The prefix for the Redis hash.
     * @param key       The key within the Redis hash.
     * @param valueType The type of object to deserialize to.
     * @return The deserialized object, or null if the key is not found.
     */

    public <T> T get(String prefix, String key, Class<T> valueType) {
        try {
            String jsonValue = (String) hashOperations.get(prefix, key);
            if (jsonValue != null) {
                return objectMapper.readValue(jsonValue, valueType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update an object in a Redis hash by its key with a specified expiration time.
     *
     * @param prefix              The prefix for the Redis hash.
     * @param key                 The key within the Redis hash.
     * @param newValue            The new value to be stored in Redis.
     * @param expirationInSeconds The expiration time for the key-value pair in
     *                            seconds.
     * @throws RuntimeException If the key does not exist in Redis.
     */

    public void update(String prefix, String key, Object newValue, long expirationInSeconds) {
        try {
            if (hashOperations.hasKey(prefix, key)) {
                String jsonValue = objectMapper.writeValueAsString(newValue);
                hashOperations.put(prefix, key, jsonValue);
                long ttl = expirationInSeconds > 0 ? expirationInSeconds : defaultExpirationSeconds;
                if (ttl > 0) {
                    redisTemplate.expire(prefix, ttl, TimeUnit.SECONDS);
                }
            } else {
                throw new RuntimeException("Key does not exist in Redis: " + key);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a key from a Redis hash.
     *
     * @param prefix The prefix for the Redis hash.
     * @param key    The key to be deleted.
     */

    public void delete(String prefix, String key) {
        redisTemplate.opsForHash().delete(prefix, key);

    }

    /**
     * Clear all data associated with a prefix in Redis.
     *
     * @param prefix The prefix for the Redis hash.
     */

    public void clearAll(String prefix) {
        redisTemplate.delete(prefix);
    }

    /**
     * Check if a key exists in a Redis hash.
     *
     * @param prefix The prefix for the Redis hash.
     * @param key    The key to check for existence.
     * @return True if the key exists, false otherwise.
     */

    public boolean exists(String prefix, String key) {
        return redisTemplate.opsForHash().hasKey(prefix, key);

    }

    /**
     * Set the expiration time for a key-value pair in Redis.
     *
     * @param seconds The expiration time in seconds.
     * @param value   The object to be stored in Redis.
     * @return True if the operation was successful.
     */

    public boolean setExpiration(String prefix, long seconds, Object value) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            valueOperations.set(prefix, jsonValue, seconds, TimeUnit.SECONDS);
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

}