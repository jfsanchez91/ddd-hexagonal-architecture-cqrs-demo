package com.example.cqrsddd.infra.web;

import com.example.cqrsddd.application.port.input.CreateUserUseCase;
import com.example.cqrsddd.application.port.input.ListUsersQuery;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.serde.annotation.Serdeable;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Controller("/api/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
class UserControllerV1 {
    private final ListUsersQuery listUsersQuery;
    private final CreateUserUseCase createUserUseCase;

    @Get
    Collection<UserDto> list() {
        return listUsersQuery.execute(ListUsersQuery.Query.EMPTY).stream()
            .map(UserDto::from)
            .collect(Collectors.toSet());
    }

    @Post
    @Consumes(MediaType.APPLICATION_JSON)
    CreateUserResponse create(@Body CreateUserRequest newUser) {
        final var command = CreateUserUseCase.CreateUserCommand.builder()
            .name(newUser.name())
            .email(newUser.email())
            .build();
        final var userId = createUserUseCase.execute(command);
        return CreateUserResponse.builder()
            .userId(userId.value())
            .name(newUser.name())
            .email(newUser.email())
            .build();
    }

    @Builder
    @Serdeable
    record UserDto(
        UUID userId,
        String name,
        String email,
        Set<TeamMembershipDto> teams
    ) {
        @Builder
        @Serdeable
        record TeamMembershipDto(
            UUID teamId,
            String alias
        ) {
            public static TeamMembershipDto from(ListUsersQuery.Projection.TeamView view) {
                return TeamMembershipDto.builder()
                    .teamId(view.teamId().value())
                    .alias(view.alias())
                    .build();
            }
        }

        public static UserDto from(ListUsersQuery.Projection projection) {
            return UserDto.builder()
                .userId(projection.userId().value())
                .name(projection.name())
                .email(projection.email())
                .teams(projection.teams().stream().map(TeamMembershipDto::from).collect(Collectors.toSet()))
                .build();
        }
    }

    @Builder
    @Serdeable
    record CreateUserRequest(
        String name,
        String email
    ) {
    }

    @Builder
    @Serdeable
    record CreateUserResponse(
        UUID userId,
        String name,
        String email
    ) {
    }
}
