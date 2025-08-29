package com.example.cqrsddd.application.service;

import com.example.cqrsddd.application.port.input.AddUserToTeamUseCase;
import com.example.cqrsddd.application.port.input.CreateTeamUseCase;
import com.example.cqrsddd.application.port.output.FindTeamByIdPort;
import com.example.cqrsddd.application.port.output.FindUserByIdPort;
import com.example.cqrsddd.application.port.output.SaveTeamPort;
import com.example.cqrsddd.application.port.output.TransactionManagementPort;
import com.example.cqrsddd.domain.Team;
import com.example.cqrsddd.domain.TeamId;
import jakarta.inject.Singleton;
import java.util.Collections;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
class TeamService implements CreateTeamUseCase, AddUserToTeamUseCase {
    private final SaveTeamPort saveTeamPort;
    private final FindTeamByIdPort findTeamByIdPort;
    private final FindUserByIdPort findUserByIdPort;
    private final TransactionManagementPort transactionManagement;

    @Override
    public TeamId execute(CreateTeamCommand command) {
        final var team = Team.builder()
            .id(TeamId.generate())
            .name(command.name())
            .members(Collections.emptySet())
            .build();
        return transactionManagement.executeWrite(() -> saveTeamPort.save(team));
    }

    @Override
    public void execute(AddUserToTeamCommand command) {
        final var team = findTeamByIdPort.findTeamById(command.teamId()).orElseThrow();
        final var user = findUserByIdPort.findUserById(command.userId()).orElseThrow();
        transactionManagement.executeWrite(() -> saveTeamPort.save(team.addMember(user.id(), command.alias())));
    }
}
