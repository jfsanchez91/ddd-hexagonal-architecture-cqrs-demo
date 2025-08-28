package com.example.cqrsddd.application.port.output;

import com.example.cqrsddd.application.port.input.ListUsersQuery;
import java.util.Collection;

public interface ListUsersPort {
    Collection<ListUsersQuery.Projection> execute(ListUsersQuery.Query query);
}
