package com.example.cqrsddd.application.service;

import com.example.cqrsddd.application.port.input.ListTeamsQuery;
import com.example.cqrsddd.application.port.output.ListTeamsPort;
import com.example.cqrsddd.application.port.output.TransactionManagementPort;
import jakarta.inject.Singleton;
import java.util.Collection;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
class ListTeamsQueryImpl implements ListTeamsQuery {
    private final ListTeamsPort port;
    private final TransactionManagementPort transactionManagement;

    @Override
    public Collection<Projection> execute(Query query) {
        return transactionManagement.executeRead(() -> port.execute(query));
    }
}
