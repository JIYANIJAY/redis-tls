package com.example.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.FileCopyUtils;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Date;

@SpringBootApplication
public class RedisWithSslApplication {

    public static void main(String[] args) throws IOException, KeyStoreException {
        SpringApplication.run(RedisWithSslApplication.class, args);
//        System.setProperty("javax.net.ssl.keyStore", "/media/root319/Data/Learning/Spring/redis-with-ssl/src/main/resources/redis-user-keystore.p12");
//        System.setProperty("javax.net.ssl.keyStorePassword", "123456");
//
//        System.setProperty("javax.net.ssl.trustStore","/media/root319/Data/Learning/Spring/redis-with-ssl/src/main/resources/root_ca_1.p12");
//        System.setProperty("javax.net.ssl.trustStorePassword","123456");

        URI uri = URI.create("rediss://localhost:6379");

        Jedis jedis = new Jedis(uri);
        jedis.auth("jay","pass");
//        System.out.println(jedis.get("user"));
        System.out.println(jedis.info());
    }
}
