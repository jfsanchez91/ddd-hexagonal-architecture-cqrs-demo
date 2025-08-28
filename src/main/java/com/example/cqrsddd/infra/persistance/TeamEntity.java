package com.example.cqrsddd.infra.persistance;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import java.util.Set;
import java.util.UUID;

@MappedEntity("teams")
record TeamEntity(
    @Id UUID id,
    String name,
    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "id.teamId")
    Set<TeamUserEntity> members
) {
}
