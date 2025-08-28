package com.example.cqrsddd.application.service;

import com.example.cqrsddd.application.port.input.AddUserToTeamUseCase;
import com.example.cqrsddd.application.port.input.CreateTeamUseCase;
import com.example.cqrsddd.application.port.output.FindTeamByIdPort;
import com.example.cqrsddd.application.port.output.FindUserByIdPort;
import com.example.cqrsddd.application.port.output.SaveTeamPort;
import com.example.cqrsddd.domain.Team;
import com.example.cqrsddd.domain.TeamId;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import java.util.Collections;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
class TeamService implements CreateTeamUseCase, AddUserToTeamUseCase {
    private final SaveTeamPort saveTeamPort;
    private final FindTeamByIdPort findTeamByIdPort;
    private final FindUserByIdPort findUserByIdPort;

    @Override
    @Transactional
    public TeamId execute(CreateTeamCommand command) {
        final var team = Team.builder()
            .id(TeamId.generate())
            .name(command.name())
            .members(Collections.emptySet())
            .build();
        return saveTeamPort.save(team);
    }

    @Override
    @Transactional
    public void execute(AddUserToTeamCommand command) {
        final var team = findTeamByIdPort.findTeamById(command.teamId()).orElseThrow();
        final var user = findUserByIdPort.findUserById(command.userId()).orElseThrow();
        saveTeamPort.save(team.addMember(user.id(), command.alias()));
    }
}
