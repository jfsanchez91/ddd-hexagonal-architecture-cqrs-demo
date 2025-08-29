package com.example.cqrsddd.infra.persistance.h2;

import com.example.cqrsddd.application.port.input.FindTeamByIdWithUserViewQuery.Query;
import com.example.cqrsddd.application.port.output.FindTeamByIdPort;
import com.example.cqrsddd.application.port.output.FindUserByIdPort;
import com.example.cqrsddd.application.port.output.SaveTeamPort;
import com.example.cqrsddd.application.port.output.SaveUserPort;
import com.example.cqrsddd.domain.Team;
import com.example.cqrsddd.domain.TeamId;
import com.example.cqrsddd.domain.User;
import com.example.cqrsddd.domain.UserId;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest(environments = "h2")
class JdbcFindTeamByIdWithUserViewAdapterIntegrationTest {
    @Inject
    JdbcFindTeamByIdWithUserViewAdapter adapter;
    @Inject
    SaveTeamPort saveTeamPort;
    @Inject
    SaveUserPort saveUserPort;
    @Inject
    FindUserByIdPort findUserByIdPort;
    @Inject
    FindTeamByIdPort findTeamByIdPort;

    @Test
    void it() {
        final var users = List.of(randomUser(), randomUser(), randomUser(), randomUser());
        saveUserPort.save(users);
        Assertions.assertTrue(findUserByIdPort.findUserById(users.getFirst().id()).isPresent());

        final var team = Team.builder()
            .id(TeamId.generate())
            .name("team-teamName-" + UUID.randomUUID())
            .members(users.stream()
                .map(user -> Team.Member.of(user.id(), "alias-" + UUID.randomUUID()))
                .collect(Collectors.toSet())
            ).build();
        saveTeamPort.save(team);
        Assertions.assertTrue(findTeamByIdPort.findTeamById(team.id()).isPresent());

        final var query = Query.builder()
            .teamId(team.id())
            .build();
        final var projection = adapter.execute(query).orElseThrow();
        Assertions.assertEquals(team.id(), projection.teamId());
        Assertions.assertEquals(team.members().size(), projection.members().size());
    }

    private static User randomUser() {
        return User.builder()
            .id(UserId.generate())
            .name("teamName-" + UUID.randomUUID())
            .email("email-" + UUID.randomUUID())
            .build();
    }
}
