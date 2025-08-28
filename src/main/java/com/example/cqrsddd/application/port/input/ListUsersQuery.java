package com.example.cqrsddd.application.port.input;

import com.example.cqrsddd.domain.TeamId;
import com.example.cqrsddd.domain.UserId;
import java.util.Collection;
import java.util.Set;
import lombok.Builder;

public interface ListUsersQuery {
    Collection<Projection> execute(Query query);

    @Builder
    record Projection(
        UserId userId,
        String name,
        String email,
        Set<TeamView> teams
    ) {
        @Builder
        public record TeamView(
            TeamId teamId,
            String alias
        ) {
        }
    }

    @Builder
    record Query() {
        public static final Query EMPTY = new Query();
    }
}
