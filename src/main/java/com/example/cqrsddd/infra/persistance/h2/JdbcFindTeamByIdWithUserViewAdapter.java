package com.example.cqrsddd.infra.persistance.h2;

import com.example.cqrsddd.application.port.input.FindTeamByIdWithUserViewQuery;
import com.example.cqrsddd.application.port.output.FindTeamByIdWithUserViewPort;
import com.example.cqrsddd.domain.TeamId;
import com.example.cqrsddd.domain.UserId;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.GenericRepository;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Singleton
@Requires(env = "h2")
@RequiredArgsConstructor
class JdbcFindTeamByIdWithUserViewAdapter implements FindTeamByIdWithUserViewPort {
    private final QueryRepository queryRepository;

    @Override
    public Optional<FindTeamByIdWithUserViewQuery.Projection> execute(FindTeamByIdWithUserViewQuery.Query query) {
        final var rows = queryRepository.execute(query.teamId().value());
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        final var members = rows.stream()
            .map(QueryRepository.Row::asUserView)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        return Optional.of(FindTeamByIdWithUserViewQuery.Projection.builder()
            .teamId(TeamId.of(rows.getFirst().teamId))
            .name(rows.getFirst().teamName)
            .members(members)
            .build());
    }

    @JdbcRepository(dialect = Dialect.H2)
    private interface QueryRepository extends GenericRepository<QueryRepository.Row, Object> {
        @Query("""
            SELECT t.id   as team_id,
                   t.name as team_name,
                   u.id   as user_id,
                   u.name as user_name,
                   u.email as user_email,
                   tu.alias as user_alias
            FROM TEAMS t
            LEFT JOIN TEAM_USERS tu ON t.id = tu.team_id
            LEFT JOIN USERS u ON tu.user_id = u.id
            WHERE t.id = :teamId
            """)
        List<Row> execute(UUID teamId);

        @Introspected
        record Row(
            UUID teamId,
            String teamName,
            @Nullable UUID userId,
            @Nullable String userName,
            @Nullable String userEmail,
            @Nullable String userAlias
        ) {
            @Nullable
            public FindTeamByIdWithUserViewQuery.Projection.UserView asUserView() {
                if (userId == null) {
                    return null;
                }
                return FindTeamByIdWithUserViewQuery.Projection.UserView.builder()
                    .userId(UserId.of(userId))
                    .name(userName)
                    .email(userEmail)
                    .alias(userAlias)
                    .build();
            }
        }
    }
}
