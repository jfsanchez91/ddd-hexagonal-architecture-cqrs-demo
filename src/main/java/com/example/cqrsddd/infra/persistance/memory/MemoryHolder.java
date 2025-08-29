package com.example.cqrsddd.infra.persistance.memory;

import com.example.cqrsddd.domain.Team;
import com.example.cqrsddd.domain.TeamId;
import com.example.cqrsddd.domain.User;
import com.example.cqrsddd.domain.UserId;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class MemoryHolder {
    public static final ConcurrentHashMap<TeamId, Team> TEAMS = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<UserId, User> USERS = new ConcurrentHashMap<>();
}
