package com.example.cqrsddd.application.service;

import com.example.cqrsddd.application.port.input.ListUsersQuery;
import com.example.cqrsddd.application.port.output.ListUsersPort;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import java.util.Collection;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
class ListUsersQueryImpl implements ListUsersQuery {
    private final ListUsersPort port;

    @Override
    @Transactional(readOnly = true)
    public Collection<Projection> execute(Query query) {
        return port.execute(query);
    }
}
