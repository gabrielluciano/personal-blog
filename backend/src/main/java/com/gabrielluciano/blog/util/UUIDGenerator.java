package com.gabrielluciano.blog.util;

import java.util.UUID;

public class UUIDGenerator {

    private UUIDGenerator() {}

    public static String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
