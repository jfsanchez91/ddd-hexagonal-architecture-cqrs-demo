package com.example.cqrsddd.infra.persistance;

import com.example.cqrsddd.application.port.input.ListUsersQuery;
import com.example.cqrsddd.application.port.output.ListUsersPort;
import com.example.cqrsddd.domain.TeamId;
import com.example.cqrsddd.domain.UserId;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.GenericRepository;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
class JdbcListUsersAdapter implements ListUsersPort {
    private final QueryRepository queryRepository;

    @Override
    public Collection<ListUsersQuery.Projection> execute(ListUsersQuery.Query query) {
        final var rows = queryRepository.execute();
        if (rows.isEmpty()) {
            return Collections.emptyList();
        }
        final var map = rows.stream()
            .collect(Collectors.groupingBy(row -> UserId.of(row.userId())));
        return map.entrySet().stream()
            .map(entry -> {
                final var userId = entry.getKey();
                final var userRows = entry.getValue();
                final var userName = userRows.getFirst().userName();
                final var userEmail = userRows.getFirst().userEmail();
                final var teams = userRows.stream()
                    .filter(row -> Objects.nonNull(row.teamId()))
                    .map(row -> ListUsersQuery.Projection.TeamView.builder()
                        .teamId(TeamId.of(row.teamId()))
                        .alias(row.userAlias())
                        .build())
                    .collect(Collectors.toSet());
                return ListUsersQuery.Projection.builder()
                    .userId(userId)
                    .name(userName)
                    .email(userEmail)
                    .teams(teams)
                    .build();
            })
            .toList();
    }

    @JdbcRepository(dialect = Dialect.H2)
    private interface QueryRepository extends GenericRepository<QueryRepository.Row, Object> {
        @Query("""
                SELECT u.id as user_id,
                       u.name as user_name,
                       u.email as user_email,
                       t.id as team_id,
                       tu.alias as user_alias
                FROM USERS u
                LEFT JOIN TEAM_USERS tu ON tu.user_id = u.id
                LEFT JOIN TEAMS t ON t.id = tu.team_id
            """)
        Collection<Row> execute();

        @Introspected
        record Row(
            UUID userId,
            String userName,
            String userEmail,
            @Nullable UUID teamId,
            @Nullable String userAlias
        ) {
        }
    }
}
