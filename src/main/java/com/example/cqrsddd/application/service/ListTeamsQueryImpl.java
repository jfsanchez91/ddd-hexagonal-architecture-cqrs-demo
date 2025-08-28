package com.example.cqrsddd.application.service;

import com.example.cqrsddd.application.port.input.ListTeamsQuery;
import com.example.cqrsddd.application.port.output.ListTeamsPort;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import java.util.Collection;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
class ListTeamsQueryImpl implements ListTeamsQuery {
    private final ListTeamsPort port;

    @Override
    @Transactional(readOnly = true)
    public Collection<Projection> execute(Query query) {
        return port.execute(query);
    }
}
