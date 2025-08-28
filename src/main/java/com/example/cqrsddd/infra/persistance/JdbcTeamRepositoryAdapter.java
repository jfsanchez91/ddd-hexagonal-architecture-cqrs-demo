package com.example.cqrsddd.infra.persistance;

import com.example.cqrsddd.application.port.output.FindTeamByIdPort;
import com.example.cqrsddd.application.port.output.SaveTeamPort;
import com.example.cqrsddd.domain.Team;
import com.example.cqrsddd.domain.TeamId;
import com.example.cqrsddd.domain.UserId;
import jakarta.inject.Singleton;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
class JdbcTeamRepositoryAdapter implements FindTeamByIdPort, SaveTeamPort {
    private final JdbcTeamRepository teamRepository;
    private final JdbcTeamUserRepository teamUserRepository;

    @Override
    public Optional<Team> findTeamById(TeamId id) {
        return teamRepository.findById(id.value()).map(entity -> Team.builder()
            .id(TeamId.of(id.value()))
            .name(entity.name())
            .members(entity.members().stream()
                .map(it -> Team.Member.of(UserId.of(it.userId()), it.alias()))
                .collect(Collectors.toSet())
            )
            .build());
    }

    @Override
    public TeamId save(Team team) {
        final var entity = teamRepository.findById(team.id().value())
            .orElseGet(() -> teamRepository.save(new TeamEntity(
                team.id().value(),
                team.name(),
                Collections.emptySet()
            )));
        final var members = team.members().stream()
            .map(it -> TeamUserEntity.of(team.id(), it.userId(), it.alias())).collect(Collectors.toSet());
        teamUserRepository.saveAll(members);
        return TeamId.of(entity.id());
    }
}
