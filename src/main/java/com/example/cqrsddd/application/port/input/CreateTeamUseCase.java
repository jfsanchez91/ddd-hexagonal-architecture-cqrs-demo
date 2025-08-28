package com.example.cqrsddd.application.port.input;

import com.example.cqrsddd.domain.TeamId;
import lombok.Builder;

public interface CreateTeamUseCase {
    TeamId execute(CreateTeamCommand command);

    @Builder
    record CreateTeamCommand(
        String name
    ) {
    }
}
