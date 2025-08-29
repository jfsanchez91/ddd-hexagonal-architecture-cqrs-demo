package com.example.cqrsddd.infra.persistance.memory;

import com.example.cqrsddd.application.port.output.FindTeamByIdPort;
import com.example.cqrsddd.application.port.output.SaveTeamPort;
import com.example.cqrsddd.domain.Team;
import com.example.cqrsddd.domain.TeamId;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
@Requires(missingBeans = {FindTeamByIdPort.class, SaveTeamPort.class})
class MemoryTeamRepositoryAdapter implements FindTeamByIdPort, SaveTeamPort {
    @Override
    public Optional<Team> findTeamById(TeamId id) {
        return Optional.ofNullable(MemoryHolder.TEAMS.get(id));
    }

    @Override
    public TeamId save(Team team) {
        MemoryHolder.TEAMS.put(team.id(), team);
        return team.id();
    }
}
