package com.example.redis;


import java.util.List;

public interface RedisOperation {
    public void put(String prefix, String key, Object value, long expirationInSeconds);
    public List<Object> getAllValues(String hashKey, Class<?> valueType);
    public <T> T get(String prefix, String key, Class<T> valueType);
    public void update(String prefix, String key, Object newValue, long expirationInSeconds);
    public void delete(String prefix, String key);
    public void clearAll(String prefix);
    public boolean exists(String prefix, String key);
    public boolean setExpiration(String prefix, long seconds, Object value);
}
