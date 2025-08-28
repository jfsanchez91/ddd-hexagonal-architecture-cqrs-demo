package com.example.cqrsddd.application.service;

import com.example.cqrsddd.application.port.input.CreateUserUseCase;
import com.example.cqrsddd.application.port.output.SaveUserPort;
import com.example.cqrsddd.domain.User;
import com.example.cqrsddd.domain.UserId;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
class UserService implements CreateUserUseCase {
    private final SaveUserPort saveUserPort;

    @Override
    @Transactional
    public UserId execute(CreateUserCommand command) {
        final var user = User.builder()
            .id(UserId.generate())
            .name(command.name())
            .email(command.email())
            .build();
        return saveUserPort.save(user);
    }
}
