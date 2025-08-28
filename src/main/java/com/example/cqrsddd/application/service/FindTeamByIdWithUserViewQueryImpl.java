package com.example.cqrsddd.application.service;

import com.example.cqrsddd.application.port.input.FindTeamByIdWithUserViewQuery;
import com.example.cqrsddd.application.port.output.FindTeamByIdWithUserViewPort;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
class FindTeamByIdWithUserViewQueryImpl implements FindTeamByIdWithUserViewQuery {
    private final FindTeamByIdWithUserViewPort port;

    @Override
    @Transactional(readOnly = true)
    public Optional<Projection> execute(Query query) {
        return port.execute(query);
    }
}
