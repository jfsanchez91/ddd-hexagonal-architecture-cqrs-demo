package com.example.cqrsddd.application.service;

import com.example.cqrsddd.application.port.input.FindTeamByIdWithUserViewQuery;
import com.example.cqrsddd.application.port.output.FindTeamByIdWithUserViewPort;
import com.example.cqrsddd.application.port.output.TransactionManagementPort;
import jakarta.inject.Singleton;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
class FindTeamByIdWithUserViewQueryImpl implements FindTeamByIdWithUserViewQuery {
    private final FindTeamByIdWithUserViewPort port;
    private final TransactionManagementPort transactionManagement;

    @Override
    public Optional<Projection> execute(Query query) {
        return transactionManagement.executeRead(() -> port.execute(query));
    }
}
