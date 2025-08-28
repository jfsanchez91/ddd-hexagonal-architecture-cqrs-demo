package com.example.cqrsddd.application.port.output;

import com.example.cqrsddd.domain.User;
import com.example.cqrsddd.domain.UserId;
import java.util.Collection;

public interface SaveUserPort {
    UserId save(User user);

    void save(Collection<User> users);
}
