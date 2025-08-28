package com.example.cqrsddd.infra.web;

import com.example.cqrsddd.application.port.input.AddUserToTeamUseCase;
import com.example.cqrsddd.application.port.input.CreateTeamUseCase;
import com.example.cqrsddd.application.port.input.FindTeamByIdWithUserViewQuery;
import com.example.cqrsddd.application.port.input.ListTeamsQuery;
import com.example.cqrsddd.domain.TeamId;
import com.example.cqrsddd.domain.UserId;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.serde.annotation.Serdeable;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Controller("/api/v1/teams")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
class TeamControllerV1 {
    private final ListTeamsQuery listTeamsQuery;
    private final FindTeamByIdWithUserViewQuery findTeamByIdWithUserViewQuery;
    private final CreateTeamUseCase createTeamUseCase;
    private final AddUserToTeamUseCase addUserToTeamUseCase;

    @Get
    Collection<TeamDto> list() {
        return listTeamsQuery.execute(ListTeamsQuery.Query.EMPTY).stream()
            .map(TeamDto::from)
            .collect(Collectors.toSet());
    }

    @Get("/{id}")
    Optional<TeamWithUserViewDto> findWithUserView(@PathVariable UUID id) {
        final var query = FindTeamByIdWithUserViewQuery.Query.builder()
            .teamId(TeamId.of(id))
            .build();
        return findTeamByIdWithUserViewQuery.execute(query).map(TeamWithUserViewDto::from);
    }

    @Post
    @Consumes(MediaType.APPLICATION_JSON)
    CreateTeamResponse create(@Body CreateTeamRequest newTeam) {
        final var command = CreateTeamUseCase.CreateTeamCommand.builder()
            .name(newTeam.name)
            .build();
        final var teamId = createTeamUseCase.execute(command);
        return CreateTeamResponse.builder()
            .teamId(teamId.value())
            .name(newTeam.name)
            .build();
    }

    @Post("/{id}/users")
    @Consumes(MediaType.APPLICATION_JSON)
    AddUserToTeamResponse addUserToTeam(@PathVariable UUID id, @Body AddUserToTeamRequest request) {
        final var command = AddUserToTeamUseCase.AddUserToTeamCommand.builder()
            .teamId(TeamId.of(id))
            .userId(UserId.of(request.userId()))
            .alias(request.alias)
            .build();
        addUserToTeamUseCase.execute(command);
        return AddUserToTeamResponse.builder()
            .teamId(id)
            .userId(request.userId)
            .alias(request.alias)
            .build();
    }

    @Builder
    @Serdeable
    record TeamDto(
        UUID teamId,
        String name,
        Set<TeamMemberDto> teamMembers
    ) {
        @Builder
        @Serdeable
        record TeamMemberDto(
            UUID userId,
            String alias
        ) {
            public static TeamMemberDto from(ListTeamsQuery.Projection.UserView view) {
                return TeamMemberDto.builder()
                    .userId(view.userId().value())
                    .alias(view.alias())
                    .build();
            }
        }

        public static TeamDto from(ListTeamsQuery.Projection projection) {
            return TeamDto.builder()
                .teamId(projection.teamId().value())
                .name(projection.teamName())
                .teamMembers(projection.members().stream().map(TeamMemberDto::from).collect(Collectors.toSet()))
                .build();
        }
    }

    @Builder
    @Serdeable
    record TeamWithUserViewDto(
        UUID teamId,
        String name,
        Set<UserView> members
    ) {
        @Builder
        @Serdeable
        public record UserView(
            UUID userId,
            String name,
            String email,
            String alias
        ) {
            public static UserView from(FindTeamByIdWithUserViewQuery.Projection.UserView view) {
                return UserView.builder()
                    .userId(view.userId().value())
                    .name(view.name())
                    .email(view.email())
                    .alias(view.alias())
                    .build();
            }
        }

        public static TeamWithUserViewDto from(FindTeamByIdWithUserViewQuery.Projection projection) {
            return TeamWithUserViewDto.builder()
                .teamId(projection.teamId().value())
                .name(projection.name())
                .members(projection.members().stream().map(UserView::from).collect(Collectors.toSet()))
                .build();
        }
    }

    @Builder
    @Serdeable
    record CreateTeamRequest(
        String name
    ) {
    }

    @Builder
    @Serdeable
    record CreateTeamResponse(
        UUID teamId,
        String name
    ) {
    }

    @Builder
    @Serdeable
    record AddUserToTeamRequest(
        UUID userId,
        String alias
    ) {
    }

    @Builder
    @Serdeable
    record AddUserToTeamResponse(
        UUID teamId,
        UUID userId,
        String alias
    ) {
    }
}
