package com.example.cqrsddd.infra.persistance;

import com.example.cqrsddd.application.port.input.ListTeamsQuery;
import com.example.cqrsddd.application.port.output.ListTeamsPort;
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
class JdbcListTeamsAdapter implements ListTeamsPort {
    private final QueryRepository queryRepository;

    @Override
    public Collection<ListTeamsQuery.Projection> execute(ListTeamsQuery.Query query) {
        final var rows = queryRepository.execute();
        if (rows.isEmpty()) {
            return Collections.emptyList();
        }
        final var map = rows.stream()
            .collect(Collectors.groupingBy(row -> TeamId.of(row.teamId())));
        return map.entrySet().stream()
            .map(entry -> {
                final var teamId = entry.getKey();
                final var teamRows = entry.getValue();
                final var teamName = teamRows.getFirst().teamName();
                final var members = teamRows.stream()
                    .filter(row -> Objects.nonNull(row.userId()))
                    .map(row -> ListTeamsQuery.Projection.UserView.builder()
                        .userId(UserId.of(row.userId()))
                        .alias(row.userAlias())
                        .build())
                    .collect(Collectors.toSet());
                return ListTeamsQuery.Projection.builder()
                    .teamId(teamId)
                    .teamName(teamName)
                    .members(members)
                    .build();
            })
            .toList();
    }

    @JdbcRepository(dialect = Dialect.H2)
    private interface QueryRepository extends GenericRepository<QueryRepository.Row, Object> {
        @Query("""
                SELECT t.id as team_id,
                       t.name as team_name,
                       u.id as user_id,
                       tu.alias as user_alias
                FROM TEAMS t
                LEFT JOIN TEAM_USERS tu ON t.id = tu.team_id
                LEFT JOIN USERS u ON tu.user_id = u.id
            """)
        Collection<Row> execute();

        @Introspected
        record Row(
            UUID teamId,
            String teamName,
            @Nullable UUID userId,
            @Nullable String userAlias
        ) {
        }
    }
}
