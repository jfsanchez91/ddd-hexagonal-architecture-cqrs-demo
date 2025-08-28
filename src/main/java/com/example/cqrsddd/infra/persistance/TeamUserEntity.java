package com.example.cqrsddd.infra.persistance;

import com.example.cqrsddd.domain.TeamId;
import com.example.cqrsddd.domain.UserId;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import java.util.UUID;

@MappedEntity("team_users")
record TeamUserEntity(
    @Id @MappedProperty("team_id") UUID teamId,
    @Id @MappedProperty("user_id") UUID userId,
    String alias
) {
    public static TeamUserEntity of(TeamId teamId, UserId userId, String alias) {
        return new TeamUserEntity(teamId.value(), userId.value(), alias);
    }

    public record TeamUserId(
        UUID teamId,
        UUID userId
    ) {
        public static TeamUserId of(TeamId teamId, UserId userId) {
            return new TeamUserId(teamId.value(), userId.value());
        }
    }
}
