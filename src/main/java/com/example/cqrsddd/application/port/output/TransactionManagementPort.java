package com.example.cqrsddd.application.port.output;

import java.util.function.Supplier;

public interface TransactionManagementPort {
    <T> T executeWrite(Supplier<T> action);

    <T> T executeRead(Supplier<T> action);
}
