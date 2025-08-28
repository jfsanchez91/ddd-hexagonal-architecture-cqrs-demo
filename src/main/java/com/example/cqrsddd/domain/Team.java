package com.example.cqrsddd.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;

@Builder(toBuilder = true)
public record Team(
    TeamId id,
    String name,
    Set<Member> members
) {
    public static Team of(String name) {
        return of(TeamId.generate(), name);
    }

    public static Team of(TeamId id, String name) {
        return Team.builder()
            .id(id)
            .name(name)
            .members(Collections.emptySet())
            .build();
    }

    public Team addMember(Member member) {
        final var newMembers = new HashSet<>(members);
        newMembers.add(member);
        return this.toBuilder().members(newMembers).build();
    }

    public Team addMember(UserId userId, String alias) {
        return addMember(Member.of(userId, alias));
    }

    public record Member(
        UserId userId,
        String alias
    ) {
        public static Member of(UserId userId, String alias) {
            return new Member(userId, alias);
        }
    }
}
