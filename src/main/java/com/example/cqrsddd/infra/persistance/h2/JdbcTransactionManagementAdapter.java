package com.example.cqrsddd.infra.persistance.h2;

import com.example.cqrsddd.application.port.output.TransactionManagementPort;
import io.micronaut.context.annotation.Requires;
import io.micronaut.transaction.SynchronousTransactionManager;
import jakarta.inject.Singleton;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;

@Singleton
@Requires(env = "h2")
@RequiredArgsConstructor
class JdbcTransactionManagementAdapter implements TransactionManagementPort {
    private final SynchronousTransactionManager<Object> delegate;

    @Override
    public <T> T executeWrite(Supplier<T> action) {
        return delegate.executeWrite(ignored -> action.get());
    }

    @Override
    public <T> T executeRead(Supplier<T> action) {
        return delegate.executeRead(ignored -> action.get());
    }
}
