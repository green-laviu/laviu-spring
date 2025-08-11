package com.metacoding.laviu._core.utils;

import java.util.UUID;

public class StreamKeyGenerator {

    public static String generate() {
        return UUID.randomUUID().toString();
    }
}
