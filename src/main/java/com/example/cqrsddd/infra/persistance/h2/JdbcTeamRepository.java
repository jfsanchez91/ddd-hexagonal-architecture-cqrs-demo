package com.example.cqrsddd.infra.persistance.h2;

import io.micronaut.context.annotation.Requires;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import java.util.UUID;

@Requires(env = "h2")
@JdbcRepository(dialect = Dialect.H2)
interface JdbcTeamRepository extends CrudRepository<TeamEntity, UUID> {
}
