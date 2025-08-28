package com.example.cqrsddd.infra.persistance;

import com.example.cqrsddd.application.port.output.FindUserByIdPort;
import com.example.cqrsddd.application.port.output.SaveUserPort;
import com.example.cqrsddd.domain.User;
import com.example.cqrsddd.domain.UserId;
import jakarta.inject.Singleton;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
class JdbcUserRepositoryAdapter implements FindUserByIdPort, SaveUserPort {
    private final JdbcUserRepository repository;

    @Override
    public Optional<User> findUserById(UserId id) {
        return repository.findById(id.value()).map(entity -> User.builder()
            .id(UserId.of(entity.id()))
            .name(entity.name())
            .email(entity.email())
            .build());
    }

    @Override
    public UserId save(User user) {
        final var entity = new UserEntity(
            user.id().value(),
            user.name(),
            user.email()
        );
        return UserId.of(repository.save(entity).id());
    }

    @Override
    public void save(Collection<User> users) {
        repository.saveAll(users.stream().map(user -> new UserEntity(
            user.id().value(),
            user.name(),
            user.email()
        )).toList());
    }
}
