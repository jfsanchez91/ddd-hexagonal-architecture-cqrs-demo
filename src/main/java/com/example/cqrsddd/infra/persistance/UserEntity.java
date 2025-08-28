package com.example.cqrsddd.infra.persistance;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import java.util.UUID;

@MappedEntity("users")
record UserEntity(
    @Id UUID id,
    String name,
    String email
) {
}
