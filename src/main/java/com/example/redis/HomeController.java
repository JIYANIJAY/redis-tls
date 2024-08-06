package com.example.redis;

import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.Jedis;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@RestController
public class HomeController {

    @Autowired
    private RedisOperation redisOperation;

    @PostMapping("/rest")
    public ResponseEntity putData() throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
//        redisOperation.put("user","user1","kaka",1000000L);
//        System.setProperty("javax.net.ssl.keyStore", "/media/root319/Data/Learning/Spring/redis-with-ssl/src/main/resources/redis-user-keystore.p12");
//        System.setProperty("javax.net.ssl.keyStorePassword", "123456");
//
//        System.setProperty("javax.net.ssl.trustStore","/media/root319/Data/Learning/Spring/redis-with-ssl/src/main/resources/root_ca_1.p12");
//        System.setProperty("javax.net.ssl.trustStorePassword","123456");

        URI uri = URI.create("rediss://127.0.0.1:6379");

        Jedis jedis = new Jedis("localhost", 6379, DefaultJedisClientConfig.builder()
                .user("jay")
                .password("pass")
                .ssl(Boolean.TRUE)
                .build());

        System.out.println(jedis.info("SERVER"));
//        Config config = new Config();
//        config.useSingleServer()
//                .setAddress("rediss://localhost:6379");
//        RedissonClient redisson = Redisson.create(config);
//
//        RKeys keys = redisson.getKeys();
//        System.out.println(keys);

        return new ResponseEntity(HttpStatus.OK);
    }
}
