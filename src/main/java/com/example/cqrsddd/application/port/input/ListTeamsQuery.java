package com.example.cqrsddd.application.port.input;

import com.example.cqrsddd.domain.TeamId;
import com.example.cqrsddd.domain.UserId;
import java.util.Collection;
import java.util.Set;
import lombok.Builder;

public interface ListTeamsQuery {
    Collection<Projection> execute(Query query);

    @Builder
    record Projection(
        TeamId teamId,
        String teamName,
        Set<UserView> members
    ) {
        @Builder
        public record UserView(
            UserId userId,
            String alias
        ) {
        }
    }

    @Builder
    record Query() {
        public static final Query EMPTY = new Query();
    }
}
