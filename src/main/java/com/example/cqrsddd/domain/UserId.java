package com.example.cqrsddd.domain;

import java.util.UUID;

public record UserId(
    UUID value
) {
    public static UserId generate() {
        return of(UUID.randomUUID());
    }

    public static UserId of(UUID value) {
        return new UserId(value);
    }
}
