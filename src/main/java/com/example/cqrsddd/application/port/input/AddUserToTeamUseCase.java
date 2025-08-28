package com.example.cqrsddd.application.port.input;

import com.example.cqrsddd.domain.TeamId;
import com.example.cqrsddd.domain.UserId;
import lombok.Builder;

public interface AddUserToTeamUseCase {
    void execute(AddUserToTeamCommand command);

    @Builder
    record AddUserToTeamCommand(
        TeamId teamId,
        UserId userId,
        String alias
    ) {
    }
}
