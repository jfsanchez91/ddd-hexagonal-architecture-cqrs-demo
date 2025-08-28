package com.example.cqrsddd.application.port.input;

import com.example.cqrsddd.domain.TeamId;
import com.example.cqrsddd.domain.UserId;
import java.util.Optional;
import java.util.Set;
import lombok.Builder;

public interface FindTeamByIdWithUserViewQuery {
    Optional<Projection> execute(Query query);

    @Builder
    record Projection(
        TeamId teamId,
        String name,
        Set<UserView> members
    ) {
        @Builder
        public record UserView(
            UserId userId,
            String name,
            String email,
            String alias
        ) {
        }
    }

    @Builder
    record Query(
        TeamId teamId
    ) {
    }
}
