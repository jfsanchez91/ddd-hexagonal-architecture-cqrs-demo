package com.example.cqrsddd.infra.persistance.memory;

import com.example.cqrsddd.application.port.input.ListUsersQuery;
import com.example.cqrsddd.application.port.output.ListUsersPort;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
@Requires(missingBeans = {ListUsersPort.class})
class MemoryListUsersAdapter implements ListUsersPort {
    @Override
    public Collection<ListUsersQuery.Projection> execute(ListUsersQuery.Query query) {
        return MemoryHolder.USERS.values().stream().map(user -> {
            final var teams = MemoryHolder.TEAMS.values().stream()
                .map(team -> team.members().stream()
                    .filter(member -> member.userId().equals(user.id()))
                    .findFirst()
                    .map(member -> ListUsersQuery.Projection.TeamView.builder()
                        .teamId(team.id())
                        .alias(member.alias())
                        .build()
                    )
                    .orElse(null)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
            return ListUsersQuery.Projection.builder()
                .userId(user.id())
                .name(user.name())
                .email(user.email())
                .teams(teams)
                .build();
        }).collect(Collectors.toSet());
    }
}
