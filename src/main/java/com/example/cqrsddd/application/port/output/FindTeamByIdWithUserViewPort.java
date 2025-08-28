package com.example.cqrsddd.application.port.output;

import com.example.cqrsddd.application.port.input.FindTeamByIdWithUserViewQuery;
import java.util.Optional;

public interface FindTeamByIdWithUserViewPort {
    Optional<FindTeamByIdWithUserViewQuery.Projection> execute(FindTeamByIdWithUserViewQuery.Query query);
}
