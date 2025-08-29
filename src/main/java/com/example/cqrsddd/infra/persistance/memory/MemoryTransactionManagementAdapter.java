package com.example.cqrsddd.infra.persistance.memory;

import com.example.cqrsddd.application.port.output.TransactionManagementPort;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
@Requires(missingBeans = {TransactionManagementPort.class})
class MemoryTransactionManagementAdapter implements TransactionManagementPort {
    @Override
    public <T> T executeWrite(Supplier<T> action) {
        return action.get();
    }

    @Override
    public <T> T executeRead(Supplier<T> action) {
        return action.get();
    }
}
