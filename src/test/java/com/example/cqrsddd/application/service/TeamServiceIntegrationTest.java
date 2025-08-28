package com.example.cqrsddd.application.service;

import com.example.cqrsddd.application.port.input.AddUserToTeamUseCase;
import com.example.cqrsddd.application.port.input.CreateTeamUseCase;
import com.example.cqrsddd.application.port.output.SaveUserPort;
import com.example.cqrsddd.domain.User;
import com.example.cqrsddd.domain.UserId;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
class TeamServiceIntegrationTest {
    @Inject
    SaveUserPort saveUserPort;
    @Inject
    TeamService teamService;

    @Test
    void it() {
        final var createTeamCommand = CreateTeamUseCase.CreateTeamCommand.builder().name("team-" + UUID.randomUUID()).build();
        final var teamId = teamService.execute(createTeamCommand);
        Assertions.assertNotNull(teamId);

        final var user = User.builder()
            .id(UserId.generate())
            .name("teamName-" + UUID.randomUUID())
            .email("email-" + UUID.randomUUID())
            .build();
        final var userId = saveUserPort.save(user);
        Assertions.assertNotNull(userId);

        final var addUserToTeamCommand = AddUserToTeamUseCase.AddUserToTeamCommand.builder()
            .teamId(teamId)
            .userId(userId)
            .alias("alias-" + UUID.randomUUID())
            .build();
        teamService.execute(addUserToTeamCommand);
    }
}
