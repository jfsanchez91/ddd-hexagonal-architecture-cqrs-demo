package com.example.cqrsddd.application.port.output;

import com.example.cqrsddd.domain.Team;
import com.example.cqrsddd.domain.TeamId;

public interface SaveTeamPort {
    TeamId save(Team team);
}
