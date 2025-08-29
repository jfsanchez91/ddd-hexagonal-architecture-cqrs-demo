package com.example.cqrsddd.infra.persistance.memory;

import com.example.cqrsddd.application.port.output.FindUserByIdPort;
import com.example.cqrsddd.application.port.output.SaveUserPort;
import com.example.cqrsddd.domain.User;
import com.example.cqrsddd.domain.UserId;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
@Requires(missingBeans = {FindUserByIdPort.class, SaveUserPort.class})
class MemoryUserRepositoryAdapter implements FindUserByIdPort, SaveUserPort {
    @Override
    public Optional<User> findUserById(UserId id) {
        return Optional.ofNullable(MemoryHolder.USERS.get(id));
    }

    @Override
    public UserId save(User user) {
        MemoryHolder.USERS.put(user.id(), user);
        return user.id();
    }

    @Override
    public void save(Collection<User> users) {
        users.forEach(this::save);
    }
}
