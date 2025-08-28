package com.example.cqrsddd.domain;

import java.util.UUID;

public record TeamId(
    UUID value
) {
    public static TeamId generate() {
        return of(UUID.randomUUID());
    }

    public static TeamId of(UUID value) {
        return new TeamId(value);
    }
}
