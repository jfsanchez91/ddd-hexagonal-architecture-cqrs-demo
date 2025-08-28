package com.example.cqrsddd.domain;

import lombok.Builder;

@Builder
public record User(
    UserId id,
    String name,
    String email
) {
    public static User of(String name, String email) {
        return User.builder()
            .id(UserId.generate())
            .name(name)
            .email(email)
            .build();
    }
}
