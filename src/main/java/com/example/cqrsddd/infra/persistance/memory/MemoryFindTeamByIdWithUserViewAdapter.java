package com.example.cqrsddd.infra.persistance.memory;

import com.example.cqrsddd.application.port.input.FindTeamByIdWithUserViewQuery;
import com.example.cqrsddd.application.port.output.FindTeamByIdWithUserViewPort;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
@Requires(missingBeans = {FindTeamByIdWithUserViewPort.class})
class MemoryFindTeamByIdWithUserViewAdapter implements FindTeamByIdWithUserViewPort {
    @Override
    public Optional<FindTeamByIdWithUserViewQuery.Projection> execute(FindTeamByIdWithUserViewQuery.Query query) {
        return Optional.ofNullable(MemoryHolder.TEAMS.get(query.teamId()))
            .map(team -> {
                final var members = team.members().stream()
                    .map(it -> {
                        final var user = MemoryHolder.USERS.get(it.userId());
                        if (user == null) {
                            return null;
                        }
                        return FindTeamByIdWithUserViewQuery.Projection.UserView.builder()
                            .alias(it.alias())
                            .userId(user.id())
                            .email(user.email())
                            .build();
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
                return FindTeamByIdWithUserViewQuery.Projection.builder()
                    .teamId(team.id())
                    .name(team.name())
                    .members(members)
                    .build();
            });
    }
}
