package com.example.redis;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestFormatRedis implements Serializable {
    private static final long serialVersionUID = 1L;
    private String prefix;
    private String key;
    private Object value;
    private long expirationTime;
    private String operation;
}