package com.example.cqrsddd.application.port.output;

import com.example.cqrsddd.domain.Team;
import com.example.cqrsddd.domain.TeamId;
import java.util.Optional;

public interface FindTeamByIdPort {
    Optional<Team> findTeamById(TeamId id);
}
