package com.example.cqrsddd.application.port.input;

import com.example.cqrsddd.domain.UserId;
import lombok.Builder;

public interface CreateUserUseCase {
    UserId execute(CreateUserCommand command);

    @Builder
    record CreateUserCommand(
        String name,
        String email
    ) {
    }
}
