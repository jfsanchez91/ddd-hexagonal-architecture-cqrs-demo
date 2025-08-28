package com.example.cqrsddd.application.port.output;

import com.example.cqrsddd.domain.User;
import com.example.cqrsddd.domain.UserId;
import java.util.Optional;

public interface FindUserByIdPort {
    Optional<User> findUserById(UserId id);
}
