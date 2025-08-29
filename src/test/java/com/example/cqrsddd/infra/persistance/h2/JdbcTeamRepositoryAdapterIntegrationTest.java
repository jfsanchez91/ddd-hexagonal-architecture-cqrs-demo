package com.example.cqrsddd.infra.persistance.h2;

import com.example.cqrsddd.domain.Team;
import com.example.cqrsddd.domain.TeamId;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.Collections;
import org.junit.jupiter.api.Test;

@MicronautTest(environments = "h2")
class JdbcTeamRepositoryAdapterIntegrationTest {
    @Inject
    private JdbcTeamRepositoryAdapter adapter;

    @Test
    void it() {
        final var team = Team.builder()
            .id(TeamId.generate())
            .name("Team Name")
            .members(Collections.emptySet())
            .build();
        adapter.save(team);
    }
}
