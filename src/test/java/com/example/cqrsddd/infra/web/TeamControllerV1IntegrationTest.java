package com.example.cqrsddd.infra.web;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@MicronautTest
class TeamControllerV1IntegrationTest {
    @Inject
    @Client("/api/v1/teams")
    HttpClient httpClient;

    @Test
    @DisplayName("it creates a new team when request is valid")
    void it_creates_new_team() {
        final var newTeam = TeamControllerV1.CreateTeamRequest.builder()
            .name("New-Team-" + UUID.randomUUID())
            .build();
        final var createdTeam = httpClient.toBlocking().exchange(
            HttpRequest.POST("/", newTeam),
            TeamControllerV1.CreateTeamResponse.class
        ).body();
        Assertions.assertNotNull(createdTeam);
        Assertions.assertNotNull(createdTeam.teamId());
        Assertions.assertEquals(newTeam.name(), createdTeam.name());

        final var teamWithUserView = httpClient.toBlocking().exchange(
            HttpRequest.GET("/" + createdTeam.teamId()),
            TeamControllerV1.TeamWithUserViewDto.class
        ).body();
        Assertions.assertNotNull(teamWithUserView);
        Assertions.assertEquals(createdTeam.name(), teamWithUserView.name());
        Assertions.assertEquals(createdTeam.teamId(), teamWithUserView.teamId());
        Assertions.assertNull(teamWithUserView.members());
    }
}
