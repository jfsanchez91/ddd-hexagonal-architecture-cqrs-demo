package com.example.cqrsddd.infra.persistance.memory;

import com.example.cqrsddd.application.port.input.ListTeamsQuery;
import com.example.cqrsddd.application.port.output.ListTeamsPort;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
@Requires(missingBeans = {ListTeamsPort.class})
class MemoryListTeamsAdapter implements ListTeamsPort {
    @Override
    public Collection<ListTeamsQuery.Projection> execute(ListTeamsQuery.Query query) {
        return MemoryHolder.TEAMS.entrySet().stream().map(entry -> {
            final var team = entry.getValue();
            final var members = team.members().stream().map(it -> {
                final var user = MemoryHolder.USERS.get(it.userId());
                if (user == null) {
                    return null;
                }
                return ListTeamsQuery.Projection.UserView.builder()
                    .userId(user.id())
                    .alias(it.alias())
                    .build();
            }).collect(Collectors.toSet());
            return ListTeamsQuery.Projection.builder()
                .teamId(team.id())
                .teamName(team.name())
                .members(members)
                .build();
        }).collect(Collectors.toSet());
    }
}
