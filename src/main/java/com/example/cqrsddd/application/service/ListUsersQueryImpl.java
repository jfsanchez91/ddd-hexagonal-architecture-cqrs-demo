package com.example.cqrsddd.application.service;

import com.example.cqrsddd.application.port.input.ListUsersQuery;
import com.example.cqrsddd.application.port.output.ListUsersPort;
import com.example.cqrsddd.application.port.output.TransactionManagementPort;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import java.util.Collection;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
class ListUsersQueryImpl implements ListUsersQuery {
    private final ListUsersPort port;
    private final TransactionManagementPort transactionManagement;

    @Override
    public Collection<Projection> execute(Query query) {
        return transactionManagement.executeRead(() -> port.execute(query));
    }
}
